package com.github.tvbox.osc.ui.compose

import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.tvbox.osc.R
import com.owen.tvrecyclerview.widget.TvRecyclerView

@Composable
fun SearchScreen(
    backBtn: ImageButton? = null,
    searchEdit: EditText? = null,
    clearBtn: TextView? = null,
    searchBtn: TextView? = null,
    checkboxBtn: TextView? = null,
    suggestionsView: View? = null,
    historyWordGrid: TvRecyclerView? = null,
    wordSwitchBtn: TextView? = null,
    wordGrid: TvRecyclerView? = null,
    resultsGrid: TvRecyclerView? = null,
    keyBoardRoot: View? = null,
    remoteRoot: View? = null,
    onSetupSearchEdit: ((EditText) -> Unit)? = null,
    onSetupResultsGrid: ((TvRecyclerView) -> Unit)? = null
) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFAEC6FF),
            background = Color(0xFF10131A),
            surface = Color(0xFF171C26),
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF10131A))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                if (backBtn != null) {
                    AndroidView(
                        modifier = Modifier.size(48.dp),
                        factory = {
                            (backBtn.parent as? android.view.ViewGroup)?.removeView(backBtn)
                            backBtn
                        }
                    )
                }
                if (searchEdit != null) {
                    AndroidView(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        factory = {
                            (searchEdit.parent as? android.view.ViewGroup)?.removeView(searchEdit)
                            searchEdit
                        }
                    )
                }
                if (clearBtn != null) {
                    AndroidView(
                        modifier = Modifier.size(48.dp),
                        factory = {
                            (clearBtn.parent as? android.view.ViewGroup)?.removeView(clearBtn)
                            clearBtn
                        }
                    )
                }
                if (searchBtn != null) {
                    AndroidView(
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight(),
                        factory = {
                            (searchBtn.parent as? android.view.ViewGroup)?.removeView(searchBtn)
                            searchBtn
                        }
                    )
                }
                if (checkboxBtn != null) {
                    AndroidView(
                        modifier = Modifier
                            .wrapContentWidth()
                            .fillMaxHeight(),
                        factory = {
                            (checkboxBtn.parent as? android.view.ViewGroup)?.removeView(checkboxBtn)
                            checkboxBtn
                        }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (suggestionsView != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (suggestionsView.parent as? android.view.ViewGroup)?.removeView(suggestionsView)
                            suggestionsView
                        }
                    )
                }
                if (resultsGrid != null) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = {
                            (resultsGrid.parent as? android.view.ViewGroup)?.removeView(resultsGrid)
                            resultsGrid
                        }
                    )
                } else {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            TvRecyclerView(ctx).apply {
                                id = R.id.mGridView
                                clipChildren = true
                                clipToPadding = false
                                onSetupResultsGrid?.invoke(this)
                            }
                        }
                    )
                }
            }

            if (keyBoardRoot != null) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = {
                        (keyBoardRoot.parent as? android.view.ViewGroup)?.removeView(keyBoardRoot)
                        keyBoardRoot
                    }
                )
            }
            if (remoteRoot != null) {
                AndroidView(
                    modifier = Modifier.fillMaxWidth(),
                    factory = {
                        (remoteRoot.parent as? android.view.ViewGroup)?.removeView(remoteRoot)
                        remoteRoot
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    SearchScreen()
}
