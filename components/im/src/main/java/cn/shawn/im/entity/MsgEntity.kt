package cn.shawn.im.entity

data class MsgEntity(val direction: Direction, val msgType: MsgType, val msgBody: MsgBody?) {

    enum class Direction {
        RECEIVE, SEND, BOTH
    }

    enum class MsgType {
        TXT, IMG
    }
}

abstract class MsgBody

class TxtMsgBody(val content: String) : MsgBody()

class ImgMsgBody() : MsgBody()

fun createTxtMsg(direction: MsgEntity.Direction, txt: String) =
        MsgEntity(direction, MsgEntity.MsgType.TXT, TxtMsgBody(txt))

fun createImgMsg(direction: MsgEntity.Direction) =
        MsgEntity(direction, MsgEntity.MsgType.IMG, ImgMsgBody())






