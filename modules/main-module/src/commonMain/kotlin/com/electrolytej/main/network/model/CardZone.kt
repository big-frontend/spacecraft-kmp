package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
enum class CardZone {
    @ProtoNumber(0)
    DECK,    // 卡组

    @ProtoNumber(1)
    HAND,    // 手牌

    @ProtoNumber(2)
    MZONE,   // 怪兽区

    @ProtoNumber(3)
    SZONE,   // 魔法陷阱区

    @ProtoNumber(4)
    GRAVE,   // 墓地

    @ProtoNumber(5)
    REMOVED, // 除外

    @ProtoNumber(6)
    EXTRA,   // 额外卡组

    @ProtoNumber(7)
    ONFIELD, // 场地

    @ProtoNumber(8)
    FZONE,   // TODO

    @ProtoNumber(9)
    PZONE,   // 灵摆区

    @ProtoNumber(10)
    TZONE   // 衍生物区
}