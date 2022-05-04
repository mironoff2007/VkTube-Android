package com.mobiledeveloper.vktube.ui.screens.subscriptions.models

sealed class SubscriptionsListEvent {
    object ScreenShown : SubscriptionsListEvent()
    object ClearAction : SubscriptionsListEvent()
    object Back : SubscriptionsListEvent()
    object ToggleAll : SubscriptionsListEvent()
    data class GroupClick(val item: SubscriptionCellModel) : SubscriptionsListEvent()
    data class Search(val searchBy: String) : SubscriptionsListEvent()
}