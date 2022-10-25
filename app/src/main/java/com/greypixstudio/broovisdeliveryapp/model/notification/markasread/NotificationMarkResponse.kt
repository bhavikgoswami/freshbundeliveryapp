package com.greypixstudio.broovisdeliveryapp.model.notification.markasread

data class NotificationMarkResponse(
    val code: Int,
    val count: Count,
    val error: String,
    val message: String,
    val results: Results,
    val success: Boolean
)