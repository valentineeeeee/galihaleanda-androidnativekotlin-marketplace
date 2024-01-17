package com.example.prjctmobile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.example.prjctmobile.databinding.ActivityMainBinding
import com.example.prjctmobile.session.SessionManager
import com.google.android.material.navigation.NavigationView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.prjctmobile.table.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var productAdapter: ProductAdapter
    private val productList: MutableList<Product> = ArrayList()

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // Find the ImageView inside the HorizontalScrollView
//        val imageView1: ImageView = findViewById(R.id.imageView1)  // Replace with the actual ID
//        val imageView2: ImageView = findViewById(R.id.imageView2)  // Replace with the actual ID
//        val imageView3: ImageView = findViewById(R.id.imageView3)  // Replace with the actual ID
//
//        // Set the desired width and height for the ImageView
//
//        // Set layout parameters for each ImageView
//        val layoutParams = FrameLayout.LayoutParams(
//            resources.getDimensionPixelSize(R.dimen.desired_image_width),
//            resources.getDimensionPixelSize(R.dimen.desired_image_height)
//        )
//
//        imageView1.layoutParams = layoutParams
//        imageView2.layoutParams = layoutParams
//        imageView3.layoutParams = layoutParams


        sessionManager = SessionManager(this)

        val cartImageView: ImageView = binding.cartImageView

        // Handle click event on cartImageView
        cartImageView.setOnClickListener {
            // Create a new instance of the ProfileFragment
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val sessionManager = SessionManager(applicationContext)
        val image = sessionManager.getUserImageUrl()

        image?.let {
            Glide.with(applicationContext)
                .load(it)
                .placeholder(R.drawable.kita) // placeholder image while loading
                .error(R.drawable.kita) // error image if loading fails
                .into(binding.cartImageView)
        }

        val recyclerView: RecyclerView = binding.recyclerViewProducts
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 2)

        // Inisialisasi adapter
        productAdapter = ProductAdapter(productList, object : ProductAdapter.OnItemClickListener {
            override fun onItemClick(product: Product) {
                // Handle item click, for example, navigate to detail activity
                val intent = Intent(applicationContext, DetailBarangActivity::class.java)
                intent.putExtra("productId", product.productId)
                intent.putExtra("productName", product.productName)
                intent.putExtra("productPrice", product.productPrice)
                intent.putExtra("productDescription", product.productDescription)
                intent.putExtra("imageUrl", product.imageUrl)
                intent.putExtra("productCategory", product.productCategory)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        })
        recyclerView.adapter = productAdapter

        fetchDataFromFirebase()

        val sidebarMenuImageView: ImageView = binding.imageViewSidebarMenu
        sidebarMenuImageView.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }


        // Menangani perubahan item-menu
        binding.bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_home ->
                {
                    // Ganti dengan Intent untuk Activity yang Anda inginkan
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
//                    replaceFragment(HomeFragment())
                R.id.menu_add ->
                    //replaceFragment(AddFragment())
                {
                    // Ganti dengan Intent untuk Activity yang Anda inginkan
                    val intent = Intent(this, AddProductActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.menu_favorite ->
                    //replaceFragment(AddFragment())
                {
                    // Ganti dengan Intent untuk Activity yang Anda inginkan
                    val intent = Intent(this, FavoriteProductActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                R.id.menu_profile ->
                {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                }
//                    replaceFragment(ProfileFragment())

            }
            true
        }

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.abc_open_drawer_description,
            R.string.abc_close_drawer_description
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navView.setNavigationItemSelectedListener(this)

        val editTextSearch: EditText = binding.EditTextSearch

        editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(editTextSearch.text.toString())
                return@setOnEditorActionListener true
            }
            false
        }

    }

    private fun performSearch(query: String) {
        Log.d("SearchDebug", "Performing search with query: $query")
        productAdapter.filter(query)
    }

    private fun fetchDataFromFirebase() {
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                productList.clear()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { productList.add(it) }
                }

                // Refresh tampilan RecyclerView dengan adapter
                sortProductsByStatus(productList)
                productAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.buku -> {
                showProductsByCategory("Buku")
                Toast.makeText(
                    applicationContext, "Halaman Buku",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.baju -> {
                showProductsByCategory("Baju")
                Toast.makeText(
                    applicationContext, "Halaman Baju",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.celana -> {
                showProductsByCategory("Celana")
                Toast.makeText(
                    applicationContext, "Halaman Celana",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.aksesoris -> {
                showProductsByCategory("Aksesoris")
                Toast.makeText(
                    applicationContext, "Halaman Aksesoris",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.handphone -> {
                showProductsByCategory("Handphone")
                Toast.makeText(
                    applicationContext, "Halaman Handphone",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.laptop -> {
                showProductsByCategory("Laptop")
                Toast.makeText(
                    applicationContext, "Halaman Laptop",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.komputer -> {
                showProductsByCategory("Komputer")
                Toast.makeText(
                    applicationContext, "Halaman Komputer",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.buku -> {
                showProductsByCategory("Buku")
                Toast.makeText(
                    applicationContext, "Halaman Buku",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.gaming -> {
                showProductsByCategory("Gaming")
                Toast.makeText(
                    applicationContext, "Halaman Gaming",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.mainan -> {
                showProductsByCategory("Mainan")
                Toast.makeText(
                    applicationContext, "Halaman Mainan",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.dapur -> {
                showProductsByCategory("Dapur")
                Toast.makeText(
                    applicationContext, "Halaman Dapur",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.office -> {
                showProductsByCategory("Office")
                Toast.makeText(
                    applicationContext, "Halaman Office",
                    Toast.LENGTH_SHORT
                ).show()
            }
            R.id.properti -> {
                showProductsByCategory("Property ")
                Toast.makeText(
                    applicationContext, "Halaman Properti",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.drawerLayout.closeDrawers()
        return true
    }

    private fun showProductsByCategory(productCategory: String) {
        // Mengambil referensi database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

        // Mengambil data product berdasarkan productCategory
        databaseReference.orderByChild("productCategory").equalTo(productCategory).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products: MutableList<Product> = ArrayList()

                for (productSnapshot in snapshot.children) {
                    val product = productSnapshot.getValue(Product::class.java)
                    product?.let { products.add(it) }
                }

                // Menampilkan product dalam RecyclerView
                displayProducts(products)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(applicationContext, "Gagal mengambil data dari Firebase", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayProducts(products: List<Product>) {
        // Mengupdate data produk dalam adapter
        productAdapter.updateData(products)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun sortProductsByStatus(products: List<Product>) {
        val sortedProducts = products.sortedBy { it.productStatus }
        productAdapter.updateData(sortedProducts)
    }

}
