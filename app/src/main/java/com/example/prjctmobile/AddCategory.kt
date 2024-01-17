package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.LoginActivity
import com.example.prjctmobile.databinding.ActivityAddCategoryBinding
import com.example.prjctmobile.table.Category
import com.example.prjctmobile.table.Product
import com.example.prjctmobile.table.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddCategory : AppCompatActivity() {

    // Inisialisasi Firebase
    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var ref: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance().getReference("category")

//        binding.buttonAddCategory.setOnClickListener {
//            val categoryId = ref.push().key
//            val categoryName = binding.editTextCategoryName.text.toString()
//
//            val category = Category(categoryId!!, categoryName,)
//                    ref.child(categoryId).setValue(category).addOnCompleteListener {
//                        Toast.makeText(
//                            applicationContext,
//                            "Registrasi berhasil",
//                            Toast.LENGTH_SHORT
//                        )
//                            .show()
//                        val intent = Intent(this, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//        }
    }
}