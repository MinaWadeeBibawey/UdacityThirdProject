package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    private var notificationTitle: String? = null
    private var notificationMessage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(toolbar)

        loadExtras()
        initClickListeners()
        displayNotificationData()
    }

    private fun loadExtras() {
        val extras = intent.extras
        extras?.let {
            notificationTitle = it.getString(EXTRA_TITLE)
            notificationMessage = it.getString(EXTRA_MESSAGE)
        }
    }

    private fun initClickListeners() {
        binding.apply {
            back_btn.setOnClickListener {
                finish()
            }
        }
    }

    private fun displayNotificationData() {
        binding.apply {
            file_name_text.text = notificationTitle
            status_text.text = notificationMessage
        }
    }

    companion object {
        private const val EXTRA_TITLE = "notification_title"
        private const val EXTRA_MESSAGE = "notification_message"

        fun withExtras(downloadFileName: String, downloadStatus: String): Bundle {
            return Bundle().apply {
                putString(EXTRA_TITLE, downloadFileName)
                putString(EXTRA_MESSAGE, downloadStatus)
            }
        }
    }

}
