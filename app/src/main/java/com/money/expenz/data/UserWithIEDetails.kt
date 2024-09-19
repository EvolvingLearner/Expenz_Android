package com.money.expenz.data

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithIEDetails(
    @Embedded val user: User,
    @Relation(
        parentColumn = "Id",
        entityColumn = "userId"
    )
    val ieDetails: List<IEDetails>
)
