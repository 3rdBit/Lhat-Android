package com.third.lhat

import android.os.Bundle
import android.util.Log
//import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.ViewBindingUtil

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity(), Startable {
    private lateinit var binding: VB
//    var firstPressedTime: Long = -1
//    var lastPressedTime: Long = -1
//    val INTERVAL = 2000
//    val makeToast = { Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.setDefaultUncaughtExceptionHandler(GlobalExceptionHandler())
        Objects.applicationContext = applicationContext
        ActivityCollector.addActivity(this)
        Log.d("BaseActivity", localClassName)
        binding = ViewBindingUtil.inflateWithGeneric(this, layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}