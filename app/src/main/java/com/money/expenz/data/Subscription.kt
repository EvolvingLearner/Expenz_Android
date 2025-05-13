package com.money.expenz.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.full.memberProperties

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

data class SubscriptionDTO(
    val category: String = "",
    val amount: Int = 0,
    val date: String = "",
    val notes: String = "",
)

fun Subscription.toSubDetailsDTO(): SubscriptionDTO {
    return SubscriptionDTO(category, amount, date, notes)
}

inline fun <reified T : Any> T.asSubMap(): Map<String, Any?> {
    val props = T::class.memberProperties.associateBy { it.name }
    return props.keys.associateWith { props[it]?.get(this) }
}