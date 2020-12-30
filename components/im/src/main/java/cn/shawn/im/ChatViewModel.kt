package cn.shawn.im

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.shawn.im.entity.MsgEntity
import cn.shawn.im.entity.TxtMsgBody
import cn.shawn.im.entity.createImgMsg
import cn.shawn.im.entity.createTxtMsg

class ChatViewModel : ViewModel() {

    val liveData: MutableLiveData<MutableList<MsgEntity>> = MutableLiveData(mutableListOf())

    fun sendMessage(msg: String) {
        liveData.postValue(liveData.value?.apply {
            add(createTxtMsg(MsgEntity.Direction.SEND, msg))
        })
    }

    private fun receiveMessage(msg: MsgEntity) {
        liveData.postValue(liveData.value?.apply {
            add(msg)
        })
    }

    fun triggerReceiveMsg() {
        val msgReceive = "收到一条Txt消息"
        Thread {
            while (true) {
                when ((Math.random() * 2).toInt()) {
                    0 -> receiveMessage(createTxtMsg(MsgEntity.Direction.RECEIVE, msgReceive))
                    else -> receiveMessage(createImgMsg(MsgEntity.Direction.RECEIVE))
                }
                Thread.sleep(3000L)
            }
        }.start()
    }

}