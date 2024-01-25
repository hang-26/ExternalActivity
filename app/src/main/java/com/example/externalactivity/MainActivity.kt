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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.externalactivity.databinding.ActivityMainBinding
import kotlin.math.E

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val STORAGE_PERMISSION_CODE = 100
    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
        if (it.resultCode == Activity.RESULT_OK){
//            val e: String? = it.data?.getStringExtra("result")
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
         binding.btSave.setOnClickListener {
             val fileName = binding.etFolderName.text.toString()
             //lấy đường dẫn tói thư mục lưu trữ
             val externalDir = ContextCompat.getExternalFilesDirs(this, null)

         }
    }

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
//                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
//                startActivityForResult(intent, STORAGE_PERMISSION_CODE)
                startForResult.launch(intent)
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