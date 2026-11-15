package com.fbox.mobile

import android.widget.TextView
import androidx.activity.ComponentDialog
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.hasScrollToNodeAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.performScrollToNode
import androidx.core.widget.NestedScrollView
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.espresso.Espresso
import androidx.test.espresso.base.DefaultFailureHandler
import androidx.test.platform.app.InstrumentationRegistry
import androidx.viewpager.widget.ViewPager
import com.github.tvbox.osc.R
import com.github.tvbox.osc.bean.SourceBean
import com.github.tvbox.osc.ui.activity.SearchActivity
import com.github.tvbox.osc.ui.dialog.SearchCheckboxDialog
import com.github.tvbox.osc.util.HawkConfig
import com.github.tvbox.osc.util.SearchHelper
import com.orhanobut.hawk.Hawk
import com.github.tvbox.osc.ui.activity.PushActivity
import com.github.tvbox.osc.ui.adapter.ApiHistoryDialogAdapter
import com.github.tvbox.osc.ui.compose.ComposeLayouts
import com.github.tvbox.osc.ui.dialog.AboutDialog
import com.github.tvbox.osc.ui.dialog.BaseDialog
import com.github.tvbox.osc.ui.dialog.ApiHistoryDialog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.ArrayList
import java.util.concurrent.atomic.AtomicReference

@RunWith(AndroidJUnit4::class)
class ComposeInteropBehaviorTest {
    @get:Rule(order = 0)
    val preferences = HawkPreferencesCheckpoint()

    @get:Rule(order = 1)
    val compose = createEmptyComposeRule()

    @Before
    fun disableFailurePixelCopy() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Espresso.setFailureHandler(DefaultFailureHandler(context, false))
    }

    @After
    fun restoreFailureHandler() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        Espresso.setFailureHandler(DefaultFailureHandler(context))
    }

    @Test
    fun viewBindingsExistBeforeFirstComposition() {
        ActivityScenario.launch(PushActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(ComponentDialog::class.java.isAssignableFrom(BaseDialog::class.java))
                val home = ComposeLayouts.createActivityLayout(activity, R.layout.activity_home)!!
                assertNotNull(home.findViewById<ViewPager>(R.id.mViewPager))
                val settings = ComposeLayouts.createFragmentLayout(activity, R.layout.fragment_model)!!
                assertNotNull(settings.findViewById<TextView>(R.id.tvApi))
                assertNotNull(settings.findViewById<TextView>(R.id.showFastSearchText))
                val search = ComposeLayouts.createActivityLayout(activity, R.layout.activity_search)!!
                assertNotNull(search.findViewById<NestedScrollView>(R.id.mobileSearchSuggestions))
            }
        }
    }

    @Test
    fun composeAboutSheetHasBoundedHeightInsideNativeDialog() {
        ActivityScenario.launch(PushActivity::class.java).use { scenario ->
            val shown = AtomicReference<AboutDialog>()
            scenario.onActivity { activity ->
                shown.set(AboutDialog(activity).apply { show() })
            }
            try {
                compose.onNodeWithText("FBox Android 手机版", substring = true).assertIsDisplayed()
            } finally {
                scenario.onActivity { shown.get().dismiss() }
            }
        }
    }

    @Test
    fun composeSearchScopePersistsSelectionAndRefreshesReusedDialog() {
        val previousApiPresent = Hawk.contains(HawkConfig.API_URL)
        val previousApi = Hawk.get(HawkConfig.API_URL, "")
        val previousPreferences = Hawk.get<HashMap<String, HashMap<String, String>>?>(
            HawkConfig.SOURCES_FOR_SEARCH, null
        )
        val previousRuntime = SearchHelper.getSourcesForSearch()
        Hawk.put(HawkConfig.API_URL, "https://compose-checkbox.invalid/config")
        try {
            ActivityScenario.launch(PushActivity::class.java).use { scenario ->
                val first = SourceBean().apply { key = "scope-one"; name = "测试源一" }
                val second = SourceBean().apply { key = "scope-two"; name = "测试源二" }
                val third = SourceBean().apply { key = "scope-three"; name = "测试源三" }
                val shown = AtomicReference<SearchCheckboxDialog>()
                scenario.onActivity { activity ->
                    shown.set(SearchCheckboxDialog(
                        activity, mutableListOf(first, second),
                        hashMapOf(first.key to "1", second.key to "1")
                    ).apply { show() })
                }
                try {
                    compose.onNodeWithText(first.name).performClick()
                    scenario.onActivity { shown.get().dismiss() }
                    assertEquals(setOf(second.key), SearchHelper.getSourcesForSearch()?.keys)
                    scenario.onActivity {
                        shown.get().mSourceList = mutableListOf(second, third)
                        shown.get().mCheckSourcees = hashMapOf(third.key to "1")
                        shown.get().show()
                    }
                    compose.onNodeWithText(third.name).assertIsDisplayed()
                    compose.onNodeWithText(first.name).assertDoesNotExist()
                    scenario.onActivity { shown.get().dismiss() }
                    assertEquals(setOf(third.key), SearchHelper.getSourcesForSearch()?.keys)
                } finally {
                    scenario.onActivity { if (shown.get().isShowing) shown.get().dismiss() }
                }
            }
        } finally {
            if (previousApiPresent) Hawk.put(HawkConfig.API_URL, previousApi)
            else Hawk.delete(HawkConfig.API_URL)
            if (previousPreferences == null) Hawk.delete(HawkConfig.SOURCES_FOR_SEARCH)
            else Hawk.put(HawkConfig.SOURCES_FOR_SEARCH, previousPreferences)
            SearchActivity.setCheckedSourcesForSearch(previousRuntime)
        }
    }

    @Test
    fun composeHistoryAcceptsLateDataAndPreservesSelectionAfterDeletingEarlierRow() {
        ActivityScenario.launch(PushActivity::class.java).use { scenario ->
            val shown = AtomicReference<ApiHistoryDialog>()
            val selected = AtomicReference<String>()
            val deleted = AtomicReference<List<String>>()
            val entries = (0 until 30).map { "https://source-$it.example/config.json" }
            scenario.onActivity { activity ->
                shown.set(ApiHistoryDialog(activity).apply { show() })
            }
            try {
                compose.waitForIdle()
                scenario.onActivity {
                    shown.get().setAdapter(object : ApiHistoryDialogAdapter.SelectDialogInterface {
                        override fun click(value: String) {
                            selected.set(value)
                        }

                        override fun del(value: String, data: ArrayList<String>) {
                            deleted.set(ArrayList(data))
                        }
                    }, entries, 1)
                }
                compose.onNodeWithText(entries[1]).assertIsSelected()
                compose.onNodeWithContentDescription("删除配置 ${entries[0]}").performClick()
                assertEquals(entries.drop(1), deleted.get())
                compose.onNodeWithText(entries[1]).assertIsSelected()
                compose.onNode(hasScrollToNodeAction()).performScrollToNode(hasText(entries.last()))
                compose.onNodeWithText(entries.last()).assertIsDisplayed().performClick()
                assertEquals(entries.last(), selected.get())
            } finally {
                scenario.onActivity { shown.get().dismiss() }
            }
        }
    }
}
