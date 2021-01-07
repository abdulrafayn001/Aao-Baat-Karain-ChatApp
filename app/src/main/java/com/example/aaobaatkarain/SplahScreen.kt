package com.example.aaobaatkarain
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity






class SplahScreen : AppCompatActivity() {

    lateinit var TextSplash:TextView
    lateinit var Logo:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah_screen)

        TextSplash = findViewById(R.id.nameSplash)
        Logo = findViewById(R.id.logosplash)

        val animation = AnimationUtils.loadAnimation(this, R.anim.move)
        animation.duration = 2000
        TextSplash.startAnimation(animation)

        Handler().postDelayed({

            finish() }, 2000)

    }
}