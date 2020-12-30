package cn.shawn.im

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cn.shawn.im.entity.MsgEntity
import cn.shawn.im.livedata.LiveDataActivity
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity(), Observer<MutableList<MsgEntity>> {

    private lateinit var viewModel:ChatViewModel

    private val adapter = ChatAdapter()

    private val layoutManager = LinearLayoutManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        startActivity(Intent(this, LiveDataActivity::class.java))
        initView()

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)
        viewModel.liveData.observe(this, this)
        viewModel.triggerReceiveMsg()
    }

    private fun initView() {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.itemAnimator = null
        recyclerView.addOnLayoutChangeListener {
            _, _, _, _, _, _, _, _, _ ->
            smoothScrollTo()
        }

        btnSend.setOnClickListener {
            viewModel.sendMessage(editText.text.toString())
        }
    }

    override fun onChanged(t: MutableList<MsgEntity>) {
        adapter.loadData(t)
        smoothScrollTo()
    }

    private fun smoothScrollTo() {
        if (adapter.itemCount <= 0 || recyclerView.canScrollVertically(1)) return
        layoutManager.scrollToPosition(adapter.itemCount - 1)
    }
}