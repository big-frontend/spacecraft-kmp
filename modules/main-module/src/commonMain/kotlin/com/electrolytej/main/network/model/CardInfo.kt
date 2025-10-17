package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class CardInfo(
    @ProtoNumber(1) val code: Int = 0,
    @ProtoNumber(2) val controller: Int = 0,
    @ProtoNumber(3) val location: CardZone = CardZone.DECK,
    @ProtoNumber(4) val sequence: Int = 0
)
