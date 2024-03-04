package com.money.expenz.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int = 1,
    @ColumnInfo(name = "Email")
    var email: String = "",
    @ColumnInfo(name = "UserName")
    var userName: String = "",
    @ColumnInfo(name = "Password")
    var password: String = "",
    @ColumnInfo(name = "Country")
    var country: String = "",
    @ColumnInfo(name = "TotalIncome")
    var totalIncome: Long = 0,
    @ColumnInfo(name = "TotalExpense")
    var totalExpense: Long = 0
)

/*@ColumnInfo(name = "Subscription")
var subscription: Nothing? = null)*/

/*constructor() : this()

constructor( userName: String, password: String , email : String){
    this.userName = userName
    this.password = password
    this.email = email
}*/
