package com.greypixstudio.broovisdeliveryapp.interfaces

interface ApplyVacationOperations {
    fun applyVacationStatus(subscriptionOrderId: Int , startDate: String , endDate: String)
}