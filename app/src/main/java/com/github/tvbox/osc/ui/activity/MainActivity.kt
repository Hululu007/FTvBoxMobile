package com.github.tvbox.osc.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.KeyEvent
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.*
import androidx.navigation.*
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.fbox.mobile.MobileFrame
import com.fbox.mobile.auth.ConfigWebLoginScreen
import com.github.tvbox.osc.base.ScreenController
import com.github.tvbox.osc.util.AppManager
import java.util.UUID

class ScreenEntryState(val state: SavedStateHandle) : ViewModel()
class ScreenArguments(val state: SavedStateHandle) : ViewModel()

/** Entry Intents always target the one Android Activity, including receiver and local-file launches. */
object ScreenIntents {
    const val EXTRA_SCREEN = "fbox.screen"
    fun create(context: Context?, type: Class<out ScreenController>): Intent =
        Intent(context ?: com.github.tvbox.osc.base.App.getInstance(), MainActivity::class.java).putExtra(EXTRA_SCREEN, type.name)
}

class MainActivity : AppCompatActivity() {
    var currentScreen by mutableStateOf<ScreenController?>(null)
        private set
    private var navigation: NavHostController? = null
    private var initialRoute = "home"
    private val screens = linkedMapOf<String, ScreenController>()
    private val states = mutableMapOf<String, ScreenEntryState>()
    private val payloads by lazy { ViewModelProvider(this)[ScreenArguments::class.java].state }
    private val factories: Map<String, () -> ScreenController> = linkedMapOf(
        "home" to { HomeScreen() }, "search" to { SearchScreen() },
        "fast_search" to { FastSearchScreen() }, "live" to { LiveScreen() },
        "detail" to { DetailScreen() }, "setting" to { SettingsScreen() },
        "history" to { HistoryScreen() }, "collect" to { CollectionScreen() },
        "local_file" to { LocalFileScreen() }, "push" to { PushScreen() },
        "web_login" to { ConfigWebLoginScreen() }
    )
    private val classes = mapOf(
        HomeScreen::class.java.name to "home", SearchScreen::class.java.name to "search",
        FastSearchScreen::class.java.name to "fast_search", LiveScreen::class.java.name to "live",
        DetailScreen::class.java.name to "detail", SettingsScreen::class.java.name to "setting",
        HistoryScreen::class.java.name to "history", CollectionScreen::class.java.name to "collect",
        LocalFileScreen::class.java.name to "local_file", PushScreen::class.java.name to "push",
        ConfigWebLoginScreen::class.java.name to "web_login"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.getInstance().addActivity(this)
        window.setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        initialRoute = savedInstanceState?.getString("initial_route")
            ?: classes[intent.getStringExtra(ScreenIntents.EXTRA_SCREEN)] ?: "home"
        if (savedInstanceState == null) payloads["root"] = intent.extras ?: Bundle()
        setContent {
            val nav = rememberNavController()
            DisposableEffect(nav) {
                navigation = nav
                onDispose { if (navigation === nav) navigation = null }
            }
            val entry by nav.currentBackStackEntryAsState()
            BackHandler { currentScreen?.onBackPressed() ?: finish() }
            MobileFrame(
                screen = currentScreen,
                route = entry?.destination?.route?.substringBefore('?') ?: "home",
                onNavigate = { route -> navigate(route, Bundle(), tab = true) },
                onBack = { currentScreen?.onBackPressed() }
            ) { modifier ->
                NavHost(navController = nav, startDestination = initialRoute, modifier = modifier) {
                    factories.forEach { (name, factory) ->
                        composable(
                            route = name + "?request={request}",
                            arguments = listOf(navArgument("request") { type = NavType.StringType; defaultValue = "root" })
                        ) { backStackEntry ->
                            val screen = remember(backStackEntry.id) { controller(backStackEntry, factory) }
                            AndroidView(
                                modifier = Modifier.fillMaxSize(),
                                factory = {
                                    (screen.contentView.parent as? ViewGroup)?.removeView(screen.contentView)
                                    screen.contentView.setViewTreeLifecycleOwner(screen)
                                    screen.contentView.setViewTreeViewModelStoreOwner(screen)
                                    screen.contentView.setViewTreeSavedStateRegistryOwner(backStackEntry)
                                    screen.contentView
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun controller(entry: NavBackStackEntry, factory: () -> ScreenController): ScreenController {
        screens[entry.id]?.let { return it }
        val state = ViewModelProvider(entry)[ScreenEntryState::class.java]
        states[entry.id] = state
        val request = entry.arguments?.getString("request") ?: "root"
        val args = state.state.get<Bundle>("arguments") ?: payloads.get<Bundle>(request) ?: Bundle()
        state.state["arguments"] = args
        val screen = factory()
        screens[entry.id] = screen
        screen.attach(this, entry.id, args, state.state["controller"])
        state.state.get<Bundle>("controller")?.getSparseParcelableArray<android.os.Parcelable>("views")?.let {
            screen.contentView.restoreHierarchyState(it)
        }
        entry.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (screen.isDestroyed) return@LifecycleEventObserver
            when (event) {
                Lifecycle.Event.ON_START -> screen.onStart()
                Lifecycle.Event.ON_RESUME -> { currentScreen = screen; screen.onResume() }
                Lifecycle.Event.ON_PAUSE -> screen.onPause()
                Lifecycle.Event.ON_STOP -> screen.onStop()
                Lifecycle.Event.ON_DESTROY -> {
                    saveScreen(screen)
                    screen.destroy()
                    screens.remove(entry.id)
                    states.remove(entry.id)
                    payloads.remove<Bundle>(request)
                    if (currentScreen === screen) currentScreen = null
                }
                else -> Unit
            }
        })
        return screen
    }

    private fun saveScreen(screen: ScreenController) {
        if (!screen.isDestroyed) states[screen.entryId]?.state?.set("controller", Bundle().also { screen.onSaveInstanceState(it) })
    }

    fun navigate(route: String, arguments: Bundle = Bundle(), tab: Boolean = false, reset: Boolean = false) {
        val nav = navigation ?: return
        if (tab && nav.currentDestination?.route?.substringBefore('?') == route) return
        // Navigation snapshots saved tabs during navigate(), before entry destruction.
        screens.values.forEach { saveScreen(it) }
        val request = UUID.randomUUID().toString()
        payloads[request] = Bundle(arguments)
        nav.navigate(route + "?request=" + request) {
            launchSingleTop = tab
            if (reset) popUpTo(nav.graph.id) { inclusive = true }
            else if (tab) {
                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                restoreState = true
            }
        }
        // A restored tab keeps its original request; discard the unused new payload.
        if (nav.currentBackStackEntry?.arguments?.getString("request") != request) {
            payloads.remove<Bundle>(request)
        }
    }

    fun openScreenIntent(intent: Intent): Boolean {
        val className = intent.getStringExtra(ScreenIntents.EXTRA_SCREEN) ?: intent.component?.className ?: return false
        val route = classes[className] ?: return false
        val reset = route == "home"
        navigate(route, intent.extras ?: Bundle(), reset = reset)
        return true
    }

    fun closeScreen(screen: ScreenController) {
        window.decorView.post {
            val nav = navigation ?: return@post
            if (nav.currentBackStackEntry?.id == screen.entryId && !nav.popBackStack()) finish()
        }
    }
    fun deliverScreenResult(screen: ScreenController, resultCode: Int, data: Intent?) {
        // Internal screen results use the previous navigation entry, not Activity results.
        navigation?.previousBackStackEntry?.savedStateHandle?.set("screen_result", Bundle().apply {
            putInt("code", resultCode); putParcelable("data", data)
        })
    }
    fun findScreen(type: Class<*>): ScreenController? = screens.values.lastOrNull { it.javaClass == type && !it.isDestroyed }
    fun backToScreen(type: Class<*>) {
        val route = classes[type.name] ?: return
        navigation?.popBackStack(route + "?request={request}", false)
    }

    override fun onNewIntent(intent: Intent) { super.onNewIntent(intent); setIntent(intent); openScreenIntent(intent) }
    override fun startActivity(intent: Intent) {
        if (!openScreenIntent(intent)) super.startActivity(intent)
    }
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) return super.dispatchKeyEvent(event)
        return currentScreen?.dispatchKeyEvent(event) == true || super.dispatchKeyEvent(event)
    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        currentScreen?.onConfigurationChanged(newConfig)
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) { super.onWindowFocusChanged(hasFocus); currentScreen?.onWindowFocusChanged(hasFocus) }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("initial_route", initialRoute)
        screens.values.forEach { saveScreen(it) }
        super.onSaveInstanceState(outState)
    }
    override fun onDestroy() {
        screens.values.toList().forEach { it.destroy() }
        screens.clear()
        AppManager.getInstance().finishActivity(this)
        super.onDestroy()
    }
}
