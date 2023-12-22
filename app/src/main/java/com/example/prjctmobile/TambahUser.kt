package com.example.prjctmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityRegisterBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class TambahUser : AppCompatActivity() {
    //private lateinit var detilList: MutableList<DetilUser>
    private lateinit var ref: DatabaseReference
    private lateinit var binding: ActivityRegisterBinding

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAMA = "extra_nama"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(EXTRA_ID)
        val nama = intent.getStringExtra(EXTRA_NAMA)

//        detilList = mutableListOf()
        ref = FirebaseDatabase.getInstance().getReference("")
    }
}