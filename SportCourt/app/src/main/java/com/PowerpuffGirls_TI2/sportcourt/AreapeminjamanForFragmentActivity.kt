package com.PowerpuffGirls_TI2.sportcourt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class AreapeminjamanForFragmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_areapeminjaman_for_fragment)
        val mFragmentManager = supportFragmentManager
        val mAPeminjamanFragment = AreapeminjamanFragment()
        val fragment = mFragmentManager.findFragmentByTag(AreapeminjamanFragment::class.java.simpleName)
        if (fragment !is AreapeminjamanFragment){
            Log.d("MyFlexibleFragment", "Fragment Name :" + AreapeminjamanFragment::class.java.simpleName)
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container,mAPeminjamanFragment,mFragmentManager::class.java.simpleName)
                .commit()
        }
    }
}