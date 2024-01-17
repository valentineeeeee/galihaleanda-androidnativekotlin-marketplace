package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityLoginBinding
import com.example.prjctmobile.session.SessionManager
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var ref: DatabaseReference
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance().getReference("user")
        sessionManager = SessionManager(this)

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
            navigateToRegister()
        }
    }

    private fun loginUser(email: String, password: String) {
        val query = ref.orderByChild("email").equalTo(email)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val storedPassword = userSnapshot.child("password").value.toString()
                        if (password == storedPassword) {
                            // Password sesuai, login berhasil
                            val userId = userSnapshot.key
                            val username = userSnapshot.child("username").value.toString()
                            val imageUrl = userSnapshot.child("imageUrl").value.toString()
                            sessionManager.saveUserSession(userId!!, email, username, imageUrl)
                            navigateToMain()
                        } else {
                            showToast("Password salah")
                        }
                    }
                } else {
                    showToast("Pengguna dengan email $email tidak ditemukan")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Implementation for onCancelled method
            }
        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
