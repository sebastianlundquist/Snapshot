package com.sebastianlundquist.snapshot

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL

class ViewSnapActivity : AppCompatActivity() {

    var messageTextView : TextView? = null
    var snapImageView : ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)

        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.showSnapImageView)

        messageTextView?.text = intent.getStringExtra("message")

        var task = ImageDownloader()
        val myImage : Bitmap
        try {
            myImage = task.execute(intent.getStringExtra("imageURL")).get()
            snapImageView?.setImageBitmap(myImage)
        }
        catch (e : Exception) {
            e.printStackTrace()
        }
    }

    class ImageDownloader : AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg p0: String): Bitmap? {
            try {
                val url = URL(p0[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                return BitmapFactory.decodeStream(connection.inputStream)
            }
            catch (e : Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
