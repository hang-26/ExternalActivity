package com.example.externalactivity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission.Write
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.externalactivity.databinding.ActivityMainBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.math.E

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val STORAGE_PERMISSION_CODE = 100
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode == Activity.RESULT_OK){
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkIsPermission()
        setEvent()
    }

     fun setEvent() {
         /**
          * xét sự kiện truy cập và ghi file
          */
         binding.btSave.setOnClickListener {
             val fileName = "external_file"
             var contentExternal = binding.etContent.text.toString()
             //lấy đường dẫn tói thư mục lưu trữ
             val externalDir = ContextCompat.getExternalFilesDirs(this, null)
             val file = File (getExternalFilesDir(null), fileName)
             // Nếu file chưa tồn tại, tạo mới file
             if (!file.exists()) {
                 file.createNewFile()
                 FileOutputStream(file).use {
                     it.write(contentExternal.toByteArray())
                 }
             }
         }


         /**
          * xét sự kiên đọc file
          */
         binding.btRead.setOnClickListener {
             val fileName = "external_file"
             val file = File (getExternalFilesDir(null), fileName)
             var contentRead = FileInputStream(file).bufferedReader().useLines { lines ->
                 lines.fold("") { some, text ->
                     "$some\n$text"
                 }
             }
             binding.etContentRead.setText(contentRead)
         }
     }

    /**
    * Hàm kiểm tra quyền
     */
    fun checkIsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Log.d("external", "SDK Version: ${Build.VERSION.SDK_INT}")
            Log.d("external", "Code Version: ${Build.VERSION_CODES.R}")
            if (Environment.isExternalStorageManager()) {
                Log.d("external", "checkIsPermission 1: ${Environment.isExternalStorageManager()}")
                setEvent()

            } else {
                Log.d("external", "checkIsPermission 2: ${Environment.isExternalStorageManager()}")
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startForResult.launch(intent)
                //intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                //startActivityForResult(intent, STORAGE_PERMISSION_CODE)
            }
        } else {
            val read = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val write = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (read  != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    STORAGE_PERMISSION_CODE
                )
            }
        }
    }
}