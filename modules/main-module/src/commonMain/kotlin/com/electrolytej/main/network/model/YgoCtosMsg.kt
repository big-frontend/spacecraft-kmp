package com.electrolytej.main.network.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber


@Serializable
sealed class YgoCtosMsg {
    @Serializable
    data class CtosPlayerInfo(val name: String = "") : YgoCtosMsg()

    @Serializable
    data class CtosJoinGame(
        @ProtoNumber(1) val version: Int = 0,
        @ProtoNumber(2) val gameid: Int = 0,
        @ProtoNumber(3) val passwd: String = ""
    ) : YgoCtosMsg()

    @Serializable
    data class CtosUpdateDeck(
        @ProtoNumber(1) val main: List<Int> = emptyList(),
        @ProtoNumber(2) val extra: List<Int> = emptyList(),
        @ProtoNumber(3) val side: List<Int> = emptyList()
    ) : YgoCtosMsg()

    @Serializable
    object CtosHsReady : YgoCtosMsg()

    @Serializable
    object CtosHsStart : YgoCtosMsg()

    @Serializable
    object CtosHsNotReady : YgoCtosMsg()

    @Serializable
    data class CtosHandResult(
        @ProtoNumber(1) val hand: HandType = HandType.UNKNOWN
    ) : YgoCtosMsg()

    @Serializable
    data class CtosTpResult(
        @ProtoNumber(1) val tp: TpType = TpType.UNKNOWN
    ) : YgoCtosMsg() {
        @Serializable
        enum class TpType {
            @ProtoNumber(0)
            UNKNOWN,

            @ProtoNumber(1)
            FIRST,  // 先攻

            @ProtoNumber(2)
            SECOND // 后攻
        }
    }

    @Serializable
    object CtosTimeConfirm : YgoCtosMsg()

    @Serializable
    data class CtosGameMsgResponse(
        @ProtoNumber(1) val selectIdleCmd: SelectIdleCmdResponse? = null,
        @ProtoNumber(2) val selectPlace: SelectPlaceResponse? = null,
        @ProtoNumber(3) val selectMulti: SelectMultiResponse? = null,
        @ProtoNumber(4) val selectSingle: SelectSingleResponse? = null,
        @ProtoNumber(5) val selectEffectYn: SelectEffectYnResponse? = null,
        @ProtoNumber(6) val selectPosition: SelectPositionResponse? = null,
        @ProtoNumber(7) val selectOption: SelectOptionResponse? = null,
        @ProtoNumber(8) val selectBattleCmd: SelectBattleCmdResponse? = null,
        @ProtoNumber(9) val selectCounterResponse: SelectCounterResponse? = null,
        @ProtoNumber(10) val sortCard: SortCardResponse? = null
    ) : YgoCtosMsg() {
        @Serializable
        data class SelectIdleCmdResponse(@ProtoNumber(1) val code: Int = 0)

        @Serializable
        data class SelectPlaceResponse(
            @ProtoNumber(1) val player: Int = 0,
            @ProtoNumber(2) val zone: CardZone = CardZone.DECK,
            @ProtoNumber(3) val sequence: Int = 0
        )

        @Serializable
        data class SelectMultiResponse(@ProtoNumber(1) val selectedPtrs: List<Int> = emptyList())

        @Serializable
        data class SelectSingleResponse(@ProtoNumber(1) val selectedPtr: Int = 0)

        @Serializable
        data class SelectEffectYnResponse(@ProtoNumber(1) val selected: Boolean = false)

        @Serializable
        data class SelectPositionResponse(@ProtoNumber(1) val position: CardPosition = CardPosition.FACEUP_ATTACK)

        @Serializable
        data class SelectOptionResponse(@ProtoNumber(1) val code: Int = 0)

        @Serializable
        data class SelectBattleCmdResponse(@ProtoNumber(1) val selectedCmd: Int = 0)

        @Serializable
        data class SelectCounterResponse(@ProtoNumber(1) val selectedCount: List<Int> = emptyList())

        @Serializable
        data class SortCardResponse(@ProtoNumber(1) val sortedIndex: List<Int> = emptyList())
    }

    @Serializable
    data class CtosChat(@ProtoNumber(1) val message: String = "") : YgoCtosMsg()

    @Serializable
    object CtosSurrender : YgoCtosMsg()

    @Serializable
    object CtosHsToObserver : YgoCtosMsg()

    @Serializable
    object CtosHsToDuelList : YgoCtosMsg()
}
