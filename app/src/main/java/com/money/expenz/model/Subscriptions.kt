package com.money.expenz.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Subscription(
   @StringRes val stringResourceId : Int,
   @DrawableRes val imageResourceId : Int
)
