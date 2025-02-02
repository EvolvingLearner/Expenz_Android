package com.money.expenz.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Subscription")
data class Subscription(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Subscription_Id")
    var subscriptionId: Int = 0,
    @ColumnInfo(name = "Category")
    var category: String = "",
    @ColumnInfo(name = "Amount")
    var amount: Int = 0,
    @ColumnInfo(name = "Date")
    var date: String = "",
    @ColumnInfo(name = "Notes")
    var notes: String = "",
    @ColumnInfo(name = "userId")
    var userId: Int = 0,
)
