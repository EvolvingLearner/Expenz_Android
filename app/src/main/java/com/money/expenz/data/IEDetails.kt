package com.money.expenz.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.reflect.full.memberProperties

@Entity(tableName = "IEDetails")
data class IEDetails(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "IE_Id")
    var ieId: Int = 0,
    @ColumnInfo(name = "IncomeExpense")
    var ie: String = "",
    @ColumnInfo(name = "Category")
    var category: String = "",
    @ColumnInfo(name = "Amount")
    var amount: Int = 0,
    @ColumnInfo(name = "Date")
    var date: String = "",
    @ColumnInfo(name = "Notes")
    var notes: String = "",
    @ColumnInfo(name = "userId")
    var userId: Int = 0
)

data class IEDetailsDTO(
    val ie: String = "",
    val category: String = "",
    val amount: Int = 0,
    val date: String = "",
    val notes: String = ""
)

fun IEDetails.toIEDetailsDTO(): IEDetailsDTO {
    return IEDetailsDTO(ie, category, amount, date, notes)
}

inline fun <reified T : Any> T.asMap(): Map<String, Any?> {
    val props = T::class.memberProperties.associateBy { it.name }
    return props.keys.associateWith { props[it]?.get(this) }
}
