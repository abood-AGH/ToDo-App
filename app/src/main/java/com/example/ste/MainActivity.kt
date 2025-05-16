package com.example.ste

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(
            {
                val intent = Intent(
                    this@MainActivity,
                    AfterLoginActivity::class.java
                )
                startActivity(intent)
                finish()
            },
            2000
        )

    }
}
