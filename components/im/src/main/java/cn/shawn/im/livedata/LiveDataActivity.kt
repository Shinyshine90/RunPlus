package cn.shawn.im.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.*
import cn.shawn.im.R

class LiveDataActivity : AppCompatActivity() {
    
    val TAG = "LiveDataActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)
        ViewModelProvider(this).get(ViewModel::class.java)
                .liveData.observe(this, observer)

        lifecycle.addObserver(object : LifecycleObserver {
            
            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun onPause() {
                Log.i(TAG, "onPause: ")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun onResume() {
                Log.i(TAG, "onResume: ")
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                Log.i(TAG, "onDestroy: ")
            }
        })
    }

    val observer = Observer<String> {
        Log.i("LiveDataActivity", "receive : $it")
    }
}