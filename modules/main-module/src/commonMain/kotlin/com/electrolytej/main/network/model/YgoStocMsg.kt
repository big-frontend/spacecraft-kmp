package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
sealed class YgoStocMsg {
    @Serializable
    data class StocJoinGame(
        @ProtoNumber(1) val lflist: Int = 0,
        @ProtoNumber(2) val rule: Int = 0,
        @ProtoNumber(3) val mode: Int = 0,
        @ProtoNumber(4) val duelRule: Int = 0,
        @ProtoNumber(5) val noCheckDeck: Boolean = false,
        @ProtoNumber(6) val noShuffleDeck: Boolean = false,
        @ProtoNumber(7) val startLp: Int = 0,
        @ProtoNumber(8) val startHand: Int = 0,
        @ProtoNumber(9) val drawCount: Int = 0,
        @ProtoNumber(10) val timeLimit: Int = 0
    ) : YgoStocMsg()

    @Serializable
    data class StocChat(
        @ProtoNumber(1) val player: Int = 0,
        @ProtoNumber(2) val msg: String = ""
    ) : YgoStocMsg()

    @Serializable
    data class StocHsPlayerEnter(
        @ProtoNumber(1) val name: String = "",
        @ProtoNumber(2) val pos: Int = 0
    ) : YgoStocMsg()

    @Serializable
    data class StocTypeChange(
        @ProtoNumber(1) val selfType: SelfType = SelfType.UNKNOWN,
        @ProtoNumber(2) val isHost: Boolean = false
    ) : YgoStocMsg() {
        @Serializable
        enum class SelfType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            PLAYER1,

            @ProtoNumber(2)
            PLAYER2,

            @ProtoNumber(3)
            PLAYER3,

            @ProtoNumber(4)
            PLAYER4,

            @ProtoNumber(5)
            PLAYER5,

            @ProtoNumber(6)
            PLAYER6,

            @ProtoNumber(100)
            OBSERVER
        }
    }

    @Serializable
    data class StocHsPlayerChange(
        @ProtoNumber(1) val state: State = State.UNKNOWN,
        @ProtoNumber(2) val pos: Int = 0,
        @ProtoNumber(3) val movedPos: Int = 0
    ) : YgoStocMsg() {
        @Serializable
        enum class State {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            MOVE,        // 位置移动

            @ProtoNumber(2)
            READY,       // 准备完毕

            @ProtoNumber(3)
            NO_READY,    // 取消准备

            @ProtoNumber(4)
            LEAVE,       // 离开房间

            @ProtoNumber(5)
            TO_OBSERVER  // 到观战席
        }
    }

    @Serializable
    data class StocHsWatchChange(@ProtoNumber(1) val count: Int = 0) : YgoStocMsg()

    @Serializable
    object StocSelectHand : YgoStocMsg()

    @Serializable
    data class StocHandResult(
        @ProtoNumber(1) val meResult: HandType = HandType.UNKNOWN,
        @ProtoNumber(2) val opResult: HandType = HandType.UNKNOWN
    ) : YgoStocMsg()

    @Serializable
    object StocSelectTp : YgoStocMsg()

    @Serializable
    data class StocDeckCount(
        @ProtoNumber(1) val meMain: Int = 0,
        @ProtoNumber(2) val meExtra: Int = 0,
        @ProtoNumber(3) val meSide: Int = 0,
        @ProtoNumber(4) val opMain: Int = 0,
        @ProtoNumber(5) val opExtra: Int = 0,
        @ProtoNumber(6) val opSide: Int = 0
    ) : YgoStocMsg()

    @Serializable
    object StocDuelStart : YgoStocMsg()

    @Serializable
    data class StocGameMessage(@ProtoNumber(1) val gameMsg: StocGameMessageContent? = null) :
        YgoStocMsg()

    @Serializable
    data class StocTimeLimit(
        @ProtoNumber(1) val player: Int = 0,
        @ProtoNumber(2) val leftTime: Int = 0
    ) : YgoStocMsg()

    @Serializable
    data class StocErrorMsg(
        @ProtoNumber(1) val errorType: ErrorType = ErrorType.UNKNOWN,
        @ProtoNumber(2) val errorCode: Int = 0
    ) : YgoStocMsg() {
        @Serializable
        enum class ErrorType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            JOINERROR,    // 加入房间错误

            @ProtoNumber(2)
            DECKERROR,    // 卡组错误

            @ProtoNumber(3)
            SIDEERROR,    // 副卡组错误

            @ProtoNumber(4)
            VERSIONERROR  // 版本错误
        }
    }

    @Serializable
    object StocChangeSide : YgoStocMsg()

    @Serializable
    object StocWaitingSide : YgoStocMsg()

    @Serializable
    object StocDuelEnd : YgoStocMsg()
}
