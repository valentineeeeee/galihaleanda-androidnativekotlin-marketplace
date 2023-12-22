package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance().getReference("user")

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextLoginEmail.text.toString()
            val password = binding.editTextLoginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
        binding.TextViewToRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        // Membuat query untuk mencari pengguna dengan email yang sesuai
        val query = ref.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Pengguna dengan email tersebut ditemukan
                    for (userSnapshot in dataSnapshot.children) {
                        val storedPassword = userSnapshot.child("password").value.toString()

                        // Memeriksa apakah password sesuai
                        if (password == storedPassword) {
                            // Password sesuai, login berhasil
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Password tidak sesuai, handle kesalahan login
                            Toast.makeText(this@LoginActivity, "Password salah", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                } else {
                    // Pengguna dengan email tersebut tidak ditemukan, handle kesalahan login
                    Toast.makeText(
                        this@LoginActivity,
                        "Pengguna dengan email $email tidak ditemukan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Implementation for onCancelled method
            }
        })
    }

    fun panggilActivityRegister(view: View?) {
        val i = Intent(applicationContext, RegisterActivity::class.java)
        startActivity(i)
    }

//    fun onRegisterClick(view: android.view.View) {
//        // Panggil fungsi untuk menangani klik pada tombol Register
//        navigateToRegister()
//    }

    private fun navigateToRegister() {
        // Intent untuk membuka halaman pendaftaran
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun isValidLogin(username: String, password: String): Boolean {
        // Implementasikan validasi login sesuai kebutuhan aplikasi Anda
        return username == "pengguna" && password == "kataSandi"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
