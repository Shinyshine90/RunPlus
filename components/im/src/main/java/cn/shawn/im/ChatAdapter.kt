package cn.shawn.im

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.shawn.im.entity.MsgEntity
import cn.shawn.im.entity.TxtMsgBody
import com.bumptech.glide.Glide

class ChatAdapter : RecyclerView.Adapter<MsgHolder>() {

    private val data = mutableListOf<MsgEntity>()

    fun loadData(data: List<MsgEntity>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = data[position].direction.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgHolder {
        return when (viewType) {
            MsgEntity.Direction.RECEIVE.ordinal ->
                ReceiveMsgHolder(parent.inflate(R.layout.item_msg_receive, false))
            MsgEntity.Direction.SEND.ordinal ->
                SendMsgHolder(parent.inflate(R.layout.item_msg_send, false))
            else -> BothMsgHolder(parent.inflate(R.layout.item_msg_both, false))
        }
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MsgHolder, position: Int) {
        holder.onBindData(data[position])
    }

}


abstract class MsgHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val container = itemView.findViewById<ViewGroup>(R.id.fl_content)

    fun onBindData(msgEntity: MsgEntity) {
        container.removeAllViews()
        msgEntity.createContent(container).run {
            container.addView(this.itemView)
            this.bindData(msgEntity)
        }
    }

}

class ReceiveMsgHolder(itemView: View) : MsgHolder(itemView) {

}

class SendMsgHolder(itemView: View) : MsgHolder(itemView) {

}

class BothMsgHolder(itemView: View) : MsgHolder(itemView) {

}

abstract class BizMsgHolder(val itemView: View) {
    abstract fun bindData(msgEntity: MsgEntity)
}

class TxtMsgHolder(itemView: View) : BizMsgHolder(itemView) {

    private val tvText = itemView.findViewById<TextView>(R.id.textView)

    override fun bindData(msgEntity: MsgEntity) {
        (msgEntity.msgBody as? TxtMsgBody)?.let {
            tvText.text = it.content
        }
    }
}

class ImgMsgHolder(itemView: View) : BizMsgHolder(itemView) {

    private val imageView = itemView.findViewById<ImageView>(R.id.imageView)

    override fun bindData(msgEntity: MsgEntity) {
        Glide.with(imageView).asGif()
                .load("http://img.adoutu.com/article/1606320537007.gif")
                .into(imageView)
    }
}

fun ViewGroup.inflate(layoutId: Int, attach: Boolean): View =
        LayoutInflater.from(this.context).inflate(layoutId, this, attach)

fun MsgEntity.createContent(parent: ViewGroup): BizMsgHolder {
    return when (this.msgType) {
        MsgEntity.MsgType.TXT -> TxtMsgHolder(parent.inflate(R.layout.item_msg_txt, false))
        MsgEntity.MsgType.IMG -> ImgMsgHolder(parent.inflate(R.layout.item_msg_img, false))
    }
}

