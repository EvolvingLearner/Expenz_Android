package com.money.expenz.data

import com.money.expenz.R
import com.money.expenz.data.Subscription

class DataSource {

    fun loadSubscriptions() : List<Subscription>{
     return listOf(
         Subscription(R.string.netflix, R.drawable.ic_category_media),
         Subscription(R.string.wifi, R.drawable.ic_category_wifi),
         Subscription(R.string.amazon, R.drawable.ic_category_media),
         Subscription(R.string.vpn, R.drawable.ic_category_vpn),
         Subscription(R.string.electricity, R.drawable.ic_category_electricity),
         Subscription(R.string.mobile, R.drawable.ic_category_mobile),
       )
    }
}