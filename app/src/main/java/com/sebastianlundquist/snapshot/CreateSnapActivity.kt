package com.sebastianlundquist.snapshot

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.graphics.Bitmap

class CreateSnapActivity : AppCompatActivity() {

    var createSnapImageView: ImageView? = null
    var messageEditText: EditText? = null

    fun chooseImageClicked(view: View) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
        else {
            getPhoto()
        }
    }

    fun nextClicked(view: View) {

    }

    fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    fun cropToSquare(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val newWidth = if (height > width) width else height
        val newHeight = if (height > width) height - (height - width) else height
        var cropW = (width - height) / 2
        cropW = if (cropW < 0) 0 else cropW
        var cropH = (height - width) / 2
        cropH = if (cropH < 0) 0 else cropH

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        setTitle(R.string.create_snap_title)

        createSnapImageView = findViewById(R.id.createSnapImageView)
        messageEditText = findViewById(R.id.messageEditText)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImage = data!!.data
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            try {
                var bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                bitmap = cropToSquare(bitmap)
                createSnapImageView?.setImageBitmap(bitmap)
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }
}
