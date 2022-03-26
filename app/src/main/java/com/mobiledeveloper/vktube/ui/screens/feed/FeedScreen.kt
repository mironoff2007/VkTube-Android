package com.mobiledeveloper.vktube.ui.screens.feed

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mobiledeveloper.vktube.navigation.NavigationTree
import com.mobiledeveloper.vktube.ui.common.cell.VideoCell
import com.mobiledeveloper.vktube.ui.common.cell.VideoCellModel
import com.mobiledeveloper.vktube.ui.common.cell.VideoGrayCell
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedAction
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedEvent
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedState

@Composable
fun FeedScreen(
    navController: NavController,
    feedViewModel: FeedViewModel
) {
    val viewState by feedViewModel.viewStates().collectAsState()
    val viewAction by feedViewModel.viewActions().collectAsState(initial = null)

    FeedView(
        viewState = viewState,
        onVideoClick = {
            feedViewModel.obtainEvent(FeedEvent.VideoClicked(it))
        },
        onScrollEnd = {feedViewModel.obtainEvent(FeedEvent.ScrollEnd)}
    )

    LaunchedEffect(key1 = viewAction, block = {
        when (viewAction) {
            is FeedAction.OpenVideoDetail -> {
                navController.navigate("${NavigationTree.Root.Detail.name}/${(viewAction as FeedAction.OpenVideoDetail).videoId}")
                feedViewModel.obtainEvent(FeedEvent.ClearAction)
            }
        }
    })

    LaunchedEffect(key1 = Unit, block = {
        feedViewModel.obtainEvent(FeedEvent.ScreenShown)
    })
}

@Composable
private fun FeedView(viewState: FeedState, onVideoClick: (VideoCellModel) -> Unit, onScrollEnd: () -> Unit) {

    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn (state = lazyListState) {

        if (lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == lazyListState.layoutInfo.totalItemsCount - 1) {
            onScrollEnd.invoke()
        }

        if (viewState.items.isEmpty()) {
            repeat(10) {
                item {
                    VideoGrayCell()
                }
            }
        } else {
            viewState.items.forEach {
                item {
                    VideoCell(it) {
                        onVideoClick.invoke(it)
                    }
                }
            }
        }
    }
}