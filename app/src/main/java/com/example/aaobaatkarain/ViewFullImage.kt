package com.example.aaobaatkarain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.squareup.picasso.Picasso

class ViewFullImage : AppCompatActivity() {

    private var ImageViewer:ImageView? = null
    private var imageURL:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_full_image)

        imageURL = intent.getStringExtra("url").toString()
        ImageViewer = findViewById(R.id.image_viewer)

        Picasso.get().load(imageURL).into(ImageViewer)

    }
}