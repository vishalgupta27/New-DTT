package com.appdtt.dtt

import android.view.animation.AnimationUtils
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageSwitcher
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import java.io.IOException

class AnimationWithOthers : AppCompatActivity() {

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var mediaPlayer: MediaPlayer
    private var isRecording = false
    private var isPlaying = false



    companion object {
        private const val REQUEST_RECORD_AUDIO_PERMISSION = 200
    }

    @SuppressLint("MissingInflatedId", "CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.appdtt.dtt.R.layout.activity_animation_with_others)


        // Copy button click listener Clipboard
        val etText = findViewById<EditText>(com.appdtt.dtt.R.id.etText)
        val btnCopy = findViewById<Button>(com.appdtt.dtt.R.id.btnCopy)
        val btnPaste = findViewById<Button>(com.appdtt.dtt.R.id.btnPaste)

        // Set the custom font to the text view
        etText.typeface = ResourcesCompat.getFont(this, com.appdtt.dtt.R.font.poppins_bold)


        // Copy button click listener
        btnCopy.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val textToCopy = etText.text.toString() // Text to be copied to clipboard
            val clip = ClipData.newPlainText("label", textToCopy)
            clipboardManager.setPrimaryClip(clip)
            Toast.makeText(this, "Message Copy $textToCopy", Toast.LENGTH_SHORT).show()
        }

        // Paste button click listener
        btnPaste.setOnClickListener {
            val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (clipboardManager.hasPrimaryClip() && clipboardManager.primaryClip?.itemCount!! > 0) {
                val clip = clipboardManager.primaryClip?.getItemAt(0)
                val pastedText = clip?.text?.toString() // Pasted text from clipboard
                etText.setText(pastedText)
                Toast.makeText(this, "Message pasted $pastedText", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Clipboard is empty", Toast.LENGTH_SHORT).show()
            }
        }// Copy button click listener End Code


// For Tween Animations  Start
        //val animImage = findViewById<ImageView>(R.id.images)
        val animImage = findViewById<ImageView>(com.appdtt.dtt.R.id.images)
        val blink = findViewById<Button>(com.appdtt.dtt.R.id.Blink)
        val fade = findViewById<Button>(com.appdtt.dtt.R.id.Fade)
        val move = findViewById<Button>(com.appdtt.dtt.R.id.Move)
        val slide = findViewById<Button>(com.appdtt.dtt.R.id.Slide)
        val rotate = findViewById<Button>(com.appdtt.dtt.R.id.Rotate)
        val zoom = findViewById<Button>(com.appdtt.dtt.R.id.Zoom)
        val stop = findViewById<Button>(com.appdtt.dtt.R.id.btn_stop)

        blink.setOnClickListener {
            val blinkanim = AnimationUtils.loadAnimation(this,
                com.appdtt.dtt.R.anim.blink_animation
            )
            animImage.startAnimation(blinkanim)
        }
        fade.setOnClickListener {
            val fadeanim = AnimationUtils.loadAnimation(this, com.appdtt.dtt.R.anim.fade_animation)
            animImage.startAnimation(fadeanim)
        }
        move.setOnClickListener {
            val moveanim = AnimationUtils.loadAnimation(this, com.appdtt.dtt.R.anim.move_animation)
            animImage.startAnimation(moveanim)
        }
        rotate.setOnClickListener {
            val roatateanim = AnimationUtils.loadAnimation(this,
                com.appdtt.dtt.R.anim.rotate_animation
            )
            animImage.startAnimation(roatateanim)
        }
        slide.setOnClickListener {
            val slideanim = AnimationUtils.loadAnimation(this,
                com.appdtt.dtt.R.anim.slide_animation
            )
            animImage.startAnimation(slideanim)
        }
        zoom.setOnClickListener {
            val zoomanim = AnimationUtils.loadAnimation(this, com.appdtt.dtt.R.anim.zoom_animation)
            animImage.startAnimation(zoomanim)
        }
        stop.setOnClickListener {
            animImage.clearAnimation()
        }// For Tween Animations  ENd



        // Set up UI
        val recordButton = findViewById<Button>(com.appdtt.dtt.R.id.Record)
        recordButton.setOnClickListener {
            if (isRecording) {
                stopRecording()
                recordButton.text = "Record"
            } else {
                startRecording()
                recordButton.text = "Stop"
            }
        }

        val stopButton = findViewById<Button>(com.appdtt.dtt.R.id.Stop)
        stopButton.setOnClickListener {
            stopRecording()
            val recordButton = findViewById<Button>(com.appdtt.dtt.R.id.Record)
            recordButton.text = "Record"
        }

        val playButton = findViewById<Button>(com.appdtt.dtt.R.id.Play)
        playButton.setOnClickListener {
            if (isPlaying) {
                stopPlaying()
                playButton.text = "Play"
            } else {
                startPlaying()
                playButton.text = "Stop"
            }
        }
    }
    private fun hasAudioRecordingPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,"") == PackageManager.PERMISSION_GRANTED
    }

    private fun startRecording() {
        // Create MediaRecorder
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/recording.3gp"
        mediaRecorder.setOutputFile(fileName)

        try {
            mediaRecorder.prepare()
            mediaRecorder.start()
            isRecording = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        if (isRecording) {
            mediaRecorder.stop()
            mediaRecorder.release()
            isRecording = false
        }
    }

    private fun startPlaying() {
        mediaPlayer = MediaPlayer()
        val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/recording.3gp"
        mediaPlayer.setDataSource(fileName)
        mediaPlayer.prepare()
        mediaPlayer.start()
        isPlaying = true

        mediaPlayer.setOnCompletionListener {
            stopPlaying()
        }
    }

    private fun stopPlaying() {
        if (isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.release()
            isPlaying = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == com.appdtt.dtt.AnimationWithOthers.Companion.REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, do nothing
            } else {
                // Permission denied, show an error or handle it gracefully
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isRecording) {
            stopRecording()
        }
        if (isPlaying) {
            stopPlaying()
        }
    }
}
