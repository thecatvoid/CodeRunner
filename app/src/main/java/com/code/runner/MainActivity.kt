package com.code.runner

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.code.runner.Editor
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, Editor.newInstance())
                .commit()
        }
    }
}