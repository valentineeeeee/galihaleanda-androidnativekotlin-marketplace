package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityRegisterBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var ref: DatabaseReference
//    private lateinit var ref: DatabaseReference
//    private lateinit var userList: MutableList

//    private lateinit var registerEmailEditText: EditText
//    private lateinit var registerUsernameEditText: EditText
//    private lateinit var registerPasswordEditText: EditText
//    private lateinit var registerButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ref = FirebaseDatabase.getInstance().getReference("user")

        binding.buttonRegister.setOnClickListener {
            val id = ref.push().key
            val email = binding.editTextRegisterEmail.text.toString()
            val address = binding.editTextRegisterAddress.text.toString()
            val username = binding.editTextRegisterUsername.text.toString()
            val password = binding.editTextRegisterPassword.text.toString()
            val konfirmasiPass = binding.editTextRegisterKonfirmasiPassword.text.toString()

            val user = User(id!!, email, address, username, password)

            if (email.isNotEmpty() && address.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
                && konfirmasiPass.isNotEmpty()
            ) {
                if (password == konfirmasiPass) {
                    ref.child(id).setValue(user).addOnCompleteListener {
                        Toast.makeText(
                            applicationContext,
                            "Registrasi berhasil",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "Password tidak cocok!", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

//        ref = FirebaseDatabase.getInstance().getReference("user")

//        binding.buttonRegister.setOnClickListener(this)
//        registerEmailEditText = findViewById(R.id.editTextRegisterEmail)
//        registerUsernameEditText = findViewById(R.id.editTextRegisterUsername)
//        registerPasswordEditText = findViewById(R.id.editTextRegisterPassword)
//        //registerButton = findViewById(R.id.buttonLogin)
//
//        registerButton.setOnClickListener {
//            val email = registerEmailEditText.text.toString()
//            val username = registerUsernameEditText.text.toString()
//            val password = registerPasswordEditText.text.toString()
//
//            // Implementasi logika pendaftaran
//            if (isValidRegistration(email, username, password)) {
//                // Pendaftaran berhasil, implementasikan tindakan yang sesuai
//                showToast("Pendaftaran berhasil")
//                // Tentu, Anda mungkin ingin menyimpan data pengguna ke database atau tempat penyimpanan lainnya.
//            } else {
//                showToast("Pendaftaran gagal. Pastikan email, username, dan password valid.")
//            }
//        }
    }

//    fun onRegisterClick(v: View?) {
//        simpanData()
//    }
//
//    private fun simpanData() {
//        val email = binding.editTextRegisterEmail.toString().trim()
//        val alamat = binding.editTextRegisterAddress.toString().trim()
//        val username = binding.editTextRegisterUsername.toString().trim()
//        val password = binding.editTextRegisterPassword.toString().trim()
//
//        if (email.isEmpty() && alamat.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(
//                email
//            ).matches() && password.isEmpty()
//        ) {
//            Toast.makeText(this, "Isi data anda secara lengkap untuk mendaftar", Toast.LENGTH_SHORT)
//                .show()
//            return
//        }
//        val userId = ref.push().key
//        val user = User(userId!!, email, alamat, username, password)
//
//        ref.child(userId).setValue(user).addOnCompleteListener {
//            Toast.makeText(applicationContext, "Akun berhasil dibuat!", Toast.LENGTH_SHORT)
//                .show()
//
//            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    fun panggilActivityLogin(view: View?) {
//        val i = Intent(applicationContext, LoginActivity::class.java)
//        startActivity(i)
//    }
//
//    private fun isValidRegistration(email: String, username: String, password: String): Boolean {
//        // Implementasikan validasi pendaftaran sesuai kebutuhan aplikasi Anda.
//        // Misalnya, pastikan bahwa email belum terdaftar sebelumnya dan password memiliki keamanan yang memadai.
//        return email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
//                username.isNotEmpty() && password.isNotEmpty()
//    }
//
//    private fun showToast(message: String) {
//        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
//    }

//    fun onRegisterClick(view: android.view.View) {
//        // Panggil fungsi untuk menangani klik pada tombol Register
//        registerButton.performClick()
//    }

}
