package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
sealed class StocGameMessageContent {
    @Serializable
    data class MsgStart(
        @ProtoNumber(1) val playerType: PlayerType = PlayerType.UNKNOWN,
        @ProtoNumber(2) val life1: Int = 0,
        @ProtoNumber(3) val life2: Int = 0,
        @ProtoNumber(4) val deckSize1: Int = 0,
        @ProtoNumber(5) val deckSize2: Int = 0,
        @ProtoNumber(6) val extraSize1: Int = 0,
        @ProtoNumber(7) val extraSize2: Int = 0
    ) : StocGameMessageContent() {
        @Serializable
        enum class PlayerType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            FirstStrike,  // 先攻

            @ProtoNumber(2)
            SecondStrike, // 后攻

            @ProtoNumber(3)
            Observer      // 观战者
        }
    }

    @Serializable
    data class MsgDraw(
        @ProtoNumber(1) val player: Int = 0,
        @ProtoNumber(2) val count: Int = 0,
        @ProtoNumber(3) val cards: List<Int> = emptyList()
    ) : StocGameMessageContent()

    @Serializable
    data class MsgNewTurn(@ProtoNumber(1) val player: Int = 0) : StocGameMessageContent()

    @Serializable
    data class MsgNewPhase(@ProtoNumber(1) val phaseType: PhaseType = PhaseType.UNKNOWN) :
        StocGameMessageContent() {
        @Serializable
        enum class PhaseType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            DRAW,         // 抽卡阶段

            @ProtoNumber(2)
            STANDBY,

            @ProtoNumber(3)
            MAIN1,        // 第一主要阶段

            @ProtoNumber(4)
            BATTLE_START, // 战斗开始

            @ProtoNumber(5)
            BATTLE_STEP,

            @ProtoNumber(6)
            DAMAGE,       // 伤害阶段

            @ProtoNumber(7)
            DAMAGE_GAL,   // 伤害计算阶段

            @ProtoNumber(8)
            BATTLE,

            @ProtoNumber(9)
            MAIN2,        // 第二主要阶段

            @ProtoNumber(10)
            END          // 回合结束
        }
    }

    @Serializable
    data class MsgHint(
        @ProtoNumber(1) val hintType: HintType = HintType.UNKNOWN,
        @ProtoNumber(2) val player: Int = 0,
        @ProtoNumber(3) val hintData: Int = 0
    ) : StocGameMessageContent() {
        @Serializable
        enum class HintType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            HINT_EVENT,

            @ProtoNumber(2)
            HINT_MESSAGE,    // TODO

            @ProtoNumber(3)
            HINT_SELECTMSG,  // TODO

            @ProtoNumber(4)
            HINT_OPSELECTED, // TODO

            @ProtoNumber(5)
            HINT_EFFECT,     // 效果

            @ProtoNumber(6)
            HINT_RACE,       // 种族

            @ProtoNumber(7)
            HINT_ATTRIB,     // 属性

            @ProtoNumber(8)
            HINT_CODE,       // TODO

            @ProtoNumber(9)
            HINT_NUMBER,     // 数字

            @ProtoNumber(10)
            HINT_CARD,      // TODO

            @ProtoNumber(11)
            HINT_ZONE       // 区域
        }
    }

    // 其他消息类型按照同样的模式实现...
    // 由于篇幅限制，这里省略了部分嵌套消息的实现，实际使用时需要完整实现所有类型
}