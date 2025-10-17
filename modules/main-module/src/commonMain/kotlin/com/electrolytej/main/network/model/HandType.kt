package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
enum class HandType {
    @ProtoNumber(0)
    UNKNOWN,

    @ProtoNumber(1)
    SCISSORS,

    @ProtoNumber(2)
    ROCK,

    @ProtoNumber(3)
    PAPER
}
