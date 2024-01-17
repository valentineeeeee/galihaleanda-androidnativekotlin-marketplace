package com.example.prjctmobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class TwoActivity : AppCompatActivity() {

    var btnNext: Button? = null
    var btnSkip: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)

        btnNext?.setOnClickListener {
            startActivity(Intent(this@TwoActivity, ThreeActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btnSkip?.setOnClickListener {
            startActivity(Intent(this@TwoActivity, LoginActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

    }
}