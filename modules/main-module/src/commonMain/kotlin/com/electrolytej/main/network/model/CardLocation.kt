package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
data class CardLocation(
    @ProtoNumber(1) val controller: Int = 0,       // 控制者编号
    @ProtoNumber(2) val zone: CardZone = CardZone.DECK,          // 区域编号
    @ProtoNumber(3) val sequence: Int = 0,         // 在某个区域的序号
    @ProtoNumber(4) val position: CardPosition = CardPosition.FACEUP_ATTACK,  // 表示形式
    @ProtoNumber(5) val isOverlay: Boolean = false,        // 是否在超量区
    @ProtoNumber(6) val overlaySequence: Int = 0 // 超量素材序号
)
