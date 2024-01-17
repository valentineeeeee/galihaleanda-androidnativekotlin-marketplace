package com.example.prjctmobile

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.prjctmobile.databinding.ActivityAddProductBinding
import com.example.prjctmobile.session.SessionManager
import com.example.prjctmobile.table.Product
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddProductActivity : AppCompatActivity() {

    lateinit var choose_img: Button
    lateinit var upload_image: Button
    lateinit var image_view: ImageView
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var ref: DatabaseReference
    private lateinit var userId: String
    private var selectedImageUri: Uri? = null // Uri untuk menyimpan gambar yang dipilih
    private lateinit var spinnerCategory: Spinner
    var fileUri: Uri? = null
    private var imageUrl: String? = null
    private lateinit var dynamicTextView: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        val view = setContentView(binding.root)

        choose_img = findViewById(R.id.chooseImage)
        upload_image = findViewById(R.id.buttonAddProduct)
        image_view = findViewById(R.id.imageViewAddProduct)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance().reference
        spinnerCategory = findViewById(R.id.spinnerCategory)

        ref = FirebaseDatabase.getInstance().getReference("products")

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

//        upload_image.setOnClickListener{
//            addProduct()
//        }

        // Fetch data for the spinner
        fetchDataForSpinner()

        // Handle item selected in the spinner
        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                val selectedCategory = parentView.getItemAtPosition(position).toString()
                // Do something with the selected category
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing here
            }
        }

        // Find the TextView by its ID
        dynamicTextView = findViewById(R.id.textViewDynamic)

        // Set the default text
        dynamicTextView.text = "Jual Product"

        val backButton: ImageView = findViewById(R.id.img)
        backButton.setOnClickListener {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            onBackPressed()
        }



        // Inisialisasi referensi produk di Firebase Realtime Database
        ref = FirebaseDatabase.getInstance().getReference("products")

        return view

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
                FirebaseStorage.getInstance().getReference("products")
                    .child(UUID.randomUUID().toString())

            ref.putFile(fileUri!!).addOnSuccessListener { taskSnapshot ->
                // Gambar berhasil diunggah, dapatkan URL gambar
                ref.downloadUrl.addOnSuccessListener { uri ->
                    imageUrl = uri.toString() // Simpan URL gambar ke variabel global
                    addProduct()

                    progressDialog.dismiss()
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

    private fun fetchDataForSpinner() {
        val categoryRef = databaseReference.child("category")

        categoryRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories: MutableList<String> = ArrayList()

                for (categorySnapshot in snapshot.children) {
                    val categoryName = categorySnapshot.child("categoryName").value.toString()
                    categories.add(categoryName)
                }

                val adapter =
                    ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item, categories)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }


    private fun addProduct() {
        val sessionManager = SessionManager(applicationContext)
        val userId = sessionManager.getUserId()
        val productId = ref.push().key
        val productName = binding.editTextProductName.text.toString().trim()
        val productDescription = binding.editTextProductDescription.text.toString().trim()
        val productPrice = binding.editTextProductPrice.text.toString().trim()
        val productCategory = binding.spinnerCategory.selectedItem.toString()
        val productImage = "products/$productId"

        val product = Product(
            productId!!,
            productName,
            productDescription,
            productPrice,
            productImage,
            "available", // Misalnya, set status menjadi "available" saat pertama kali ditambahkan
            productCategory,
            userId,
            "",
            imageUrl
        )

        if (productName.isNotEmpty() && productDescription.isNotEmpty() && productPrice.isNotEmpty()) {
            ref.child(productId).setValue(product).addOnCompleteListener {
                Toast.makeText(
                    applicationContext, "Product Successfully Added", Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        } else {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

}
