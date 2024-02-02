package com.money.expenz.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "User")
data class User(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "Id")
    var id: Int = 0,
    @NonNull
    @ColumnInfo(name = "Email")
    var email: String = "",
    @NonNull
    @ColumnInfo(name = "UserName")
    var userName: String = "",
    @NonNull
    @ColumnInfo(name = "Password")
    var password: String = "",
    @NonNull
    @ColumnInfo(name = "Country")
    var country: String = "",
    @NonNull
    @ColumnInfo(name = "TotalIncome")
    var totalIncome: Long = 0,
    @NonNull
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
