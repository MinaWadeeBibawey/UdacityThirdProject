package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.notification.NotificationUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private var URL: String = ""
    private var selectedName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        urls_radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.image_url -> {
                    URL = URL_IMAGE
                    selectedName = resources.getString(R.string.text_image_radio_button)
                }
                R.id.load_app_url -> {
                    URL = URL_STARTER_CODE
                    selectedName = resources.getString(R.string.text_load_app_radio_button)
                }
                R.id.retrofit_url -> {
                    URL = URL_SQUARE
                    selectedName = resources.getString(R.string.text_Retrofit_radio_button)
                }
            }
        }

        custom_button.setOnClickListener {
            if (URL.isNotEmpty()) {
                download(URL)
                custom_button.loadingButtonState(0)
            } else {
                Toast.makeText(this, "Please Select The File To Download", Toast.LENGTH_LONG)
                    .show()
            }
        }

        initApp()
    }

    private fun initApp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationUtils.createNotificationChannel(
                this,
                NotificationUtils.getNewsChannel(this)
            )
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            NotificationUtils.sendNotification(
                context = this@MainActivity,
                titleResId = R.string.notification_title,
                messageResId = R.string.notification_description,
                notificationId = id?.toInt(),
                downloadFileName = selectedName,
                downloadStatus = "Success"
            )

            custom_button.loadingButtonState(1)
        }
    }

    private fun download(URL: String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val URL_IMAGE =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val URL_STARTER_CODE =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/refs/heads/master.zip"
        private const val URL_SQUARE =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
    }

}
