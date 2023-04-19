package com.appdtt.dtt

import android.Manifest.permission
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

class AudioCaptures : AppCompatActivity() {
    // Initializing all variables..
    private var startTV: TextView? = null
    private var stopTV: TextView? = null
    private var playTV: TextView? = null
    private var stopplayTV: TextView? = null
    private var statusTV: TextView? = null

    // creating a variable for media recorder object class.
    private lateinit var mRecorder: MediaRecorder

    // creating a variable for mediaplayer class
    private var mPlayer: MediaPlayer? = null

        // string variable is created for storing a file name
        private var mFileName: String? = null

        // constant for storing audio permission
         val REQUEST_AUDIO_PERMISSION_CODE = 1


    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audiorecrod)

        // initialize all variables with their layout items.
        statusTV = findViewById(R.id.idTVstatus)
        startTV = findViewById(R.id.btnRecord)
        stopTV = findViewById(R.id.btnStop)
        playTV = findViewById(R.id.btnPlay)
        stopplayTV = findViewById(R.id.btnStopPlay)
        stopTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        playTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        stopplayTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        startTV!!.setOnClickListener { // start recording method will
            // start the recording of audio.
            startRecording()
        }
        stopTV!!.setOnClickListener(View.OnClickListener { // pause Recording method will
            // pause the recording of audio.
            pauseRecording()
        })
        playTV!!.setOnClickListener(View.OnClickListener { // play audio method will play
            // the audio which we have recorded
            playAudio()
        })
        stopplayTV!!.setOnClickListener(View.OnClickListener { // pause play method will
            // pause the play of audio
            pausePlaying()
        })
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun startRecording() = if (CheckPermissions()) {
        stopTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        startTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        playTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        stopplayTV!!.setBackgroundColor(resources.getColor(R.color.gray))

        mFileName = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/AudioRecording.3gp"

        // below method is used to initialize
        // the media recorder class


        mRecorder = MediaRecorder(this@AudioCaptures)

        // below method is used to set the audio
        // source which we are using a mic.
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)

        // below method is used to set // the output format of the audio.
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)

        // below method is used to set the
        // audio encoder for our recorded audio.
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        // below method is used to set the
        // output file location for our recorded audio
        mRecorder.setOutputFile(mFileName)
        try {
            Log.e("TAG", "92 success")
            mRecorder.prepare()

        } catch (e: IOException) {
            Log.e("TAG", "95 failed")
        }
        mRecorder.start()
        statusTV!!.text = "Recording Started"
    } else {
        RequestPermissions()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // this method is called when user will
        // grant the permission for audio recording.
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.size > 0) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                    Toast.makeText(applicationContext, "Permission Granted", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    fun CheckPermissions(): Boolean {
        // this method is used to check permission
        val result =
            ContextCompat.checkSelfPermission(applicationContext, permission.WRITE_EXTERNAL_STORAGE)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(
            this@AudioCaptures,
            arrayOf(permission.RECORD_AUDIO, permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_AUDIO_PERMISSION_CODE
        )
    }

    fun playAudio() {
        stopTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        startTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        playTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        stopplayTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))

        // for playing our recorded audio
        // we are using media player class.
        mPlayer = MediaPlayer()
        try {
            // below method is used to set the
            // data source which will be our file name
            mPlayer!!.setDataSource(mFileName)

            // below method will prepare our media player
            mPlayer!!.prepare()

            // below method will start our media player.
            mPlayer!!.start()
            statusTV!!.text = "Recording Started Playing"
        } catch (e: IOException) {
            Log.e("TAG", "prepare() failed")
        }
    }

    fun pauseRecording() {
        stopTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        startTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        playTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        stopplayTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))

        // below method will stop
        // the audio recording.
        mRecorder!!.stop()

        // below method will release
        // the media recorder class.
        mRecorder!!.release()
       // mRecorder = null
        statusTV!!.text = "Recording Stopped"
    }

    fun pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        mPlayer!!.release()
        mPlayer = null
        stopTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        startTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        playTV!!.setBackgroundColor(resources.getColor(R.color.purple_200))
        stopplayTV!!.setBackgroundColor(resources.getColor(R.color.gray))
        statusTV!!.text = "Recording Play Stopped"
    }


}