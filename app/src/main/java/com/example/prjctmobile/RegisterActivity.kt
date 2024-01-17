package com.example.prjctmobile

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityRegisterBinding
import com.example.prjctmobile.table.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import java.util.UUID

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var ref: DatabaseReference
    lateinit var choose_img: Button
    lateinit var upload_image: Button
    lateinit var image_view: ImageView
    var fileUri: Uri? = null
    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)



        choose_img = findViewById(R.id.buttonUploadImage)
        upload_image = findViewById(R.id.buttonRegister)
        image_view = findViewById(R.id.imageViewUploaded)

        ref = FirebaseDatabase.getInstance().getReference("user")

        choose_img.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Choose image to upload"),0
            )
        }

        upload_image.setOnClickListener{
            if (fileUri != null) {
                val productId = ref.push().key!!
                uploadImage()
            } else {
                Toast.makeText(applicationContext, "Please select image",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewToLogin.setOnClickListener{
            navigateToLogin()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap : Bitmap = MediaStore.Images.Media.getBitmap(contentResolver,fileUri)
                image_view.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("Exception", "Error: " + e)
            }
        }
    }

    private fun uploadImage() {
        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading Image...")
            progressDialog.setMessage("Processing")
            progressDialog.show()

            val ref: StorageReference =
                FirebaseStorage.getInstance().getReference("user")
                    .child(UUID.randomUUID().toString())

            ref.putFile(fileUri!!).addOnSuccessListener { taskSnapshot ->
                // Gambar berhasil diunggah, dapatkan URL gambar
                ref.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString() // Simpan URL gambar ke variabel global
                    progressDialog.dismiss()

                    // Setelah upload gambar selesai, baru lakukan registrasi
                    performRegistration()

                    Toast.makeText(
                        applicationContext, "File Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun performRegistration() {
        val userId = ref.push().key
        val email = binding.editTextRegisterEmail.text.toString()
        val address = binding.editTextRegisterAddress.text.toString()
        val username = binding.editTextRegisterUsername.text.toString()
        val password = binding.editTextRegisterPassword.text.toString()
        val konfirmasiPass = binding.editTextRegisterKonfirmasiPassword.text.toString()

        val user = User(userId!!, email, address, username, password, imageUrl)

        if (email.isNotEmpty() && address.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()
            && konfirmasiPass.isNotEmpty()
        ) {
            if (password == konfirmasiPass) {
                ref.child(userId).setValue(user).addOnCompleteListener {
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

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

}
