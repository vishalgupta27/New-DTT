package com.appdtt.dtt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.ViewSwitcher

class ImagesSwitchers : AppCompatActivity() {

    private lateinit var imageSwitcher: ImageSwitcher
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button

    private val images = arrayOf(
        R.drawable.im1,
        R.drawable.im2,
        R.drawable.im3
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.imageslider)

        imageSwitcher = findViewById(R.id.images_switcher)
        nextButton = findViewById(R.id.Next)
        prevButton = findViewById(R.id.Previous)

        // Set the factory for creating ImageView objects for the ImageSwitcher
        imageSwitcher.setFactory {
            val imageView = ImageView(this)
            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            imageView
        }

        // Set animations for switching images
        val inAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        val outAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_out)
        imageSwitcher.inAnimation = inAnimation
        imageSwitcher.outAnimation = outAnimation

        // Set initial image
        imageSwitcher.setImageResource(images[currentIndex])

        // Set click listeners for next and previous buttons
        nextButton.setOnClickListener {
            currentIndex++
            if (currentIndex >= images.size) {
                currentIndex = 0
            }
            imageSwitcher.setImageResource(images[currentIndex])
        }

        prevButton.setOnClickListener {
            currentIndex--
            if (currentIndex < 0) {
                currentIndex = images.size - 1
            }
            imageSwitcher.setImageResource(images[currentIndex])
        }
    }
}
