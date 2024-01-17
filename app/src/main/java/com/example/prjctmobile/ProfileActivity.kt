package com.example.prjctmobile

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.prjctmobile.databinding.ActivityProfileBinding
import com.example.prjctmobile.session.SessionManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var dynamicTextView: TextView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize viewPager and tabLayout before using them
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)

        // Set up the ViewPager2 with an adapter
        val adapter = ProfilePagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Set tab names here, e.g., "Profile" and "Review"
            when (position) {
                0 -> tab.text = "Dijual"
                1 -> tab.text = "Dibeli"
            }
        }.attach()

        val sessionManager = SessionManager(applicationContext)

        // Mendapatkan informasi pengguna dari SessionManager
        val userId = sessionManager.getUserId()
        val username = sessionManager.getUserName()
        val email = sessionManager.getUserEmail()
        val image = sessionManager.getUserImageUrl()

        // Menetapkan informasi pengguna pada TextView
        binding.textView3.text = username
        binding.textView4.text = email

        image?.let {
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.kita) // placeholder image while loading
                .error(R.drawable.kita) // error image if loading fails
                .into(binding.imageViewProfile)
        }

        // Find the TextView by its ID
        dynamicTextView = findViewById(R.id.textViewDynamic)

        // Set the default text
        dynamicTextView.text = "Profile"

        val backButton: ImageView = findViewById(R.id.img)
        backButton.setOnClickListener {
            onBackPressed()
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

        }
    }
}
