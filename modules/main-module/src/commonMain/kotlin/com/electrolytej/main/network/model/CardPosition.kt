package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
enum class CardPosition {
    @ProtoNumber(0)
    FACEUP_ATTACK,

    @ProtoNumber(1)
    FACEDOWN_ATTACK,

    @ProtoNumber(2)
    FACEUP_DEFENSE,

    @ProtoNumber(3)
    FACEDOWN_DEFENSE,

    @ProtoNumber(4)
    FACEUP,

    @ProtoNumber(5)
    FACEDOWN,

    @ProtoNumber(6)
    ATTACK,

    @ProtoNumber(7)
    DEFENSE
}
