package cn.shawn.im.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread

class ViewModel : ViewModel() {

    init {
       thread {
           Thread.sleep(1000L)
           liveData.postValue("1")
       }.start()
    }

    val liveData = MutableLiveData<String>()


}