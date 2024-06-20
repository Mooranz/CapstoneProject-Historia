package com.tugas.capstoneproject_historia.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tugas.capstoneproject_historia.MainActivity
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.app_final_name) + " - Detail"

        setupData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupData() {
        val detail = intent.getParcelableExtra<HistoryEntity>(EXTRA_DETAIL) as HistoryEntity
        if (detail != null) {
            if (!detail.imageUri.isNullOrEmpty()) {
                Glide.with(applicationContext)
                    .load(detail.imageUri)
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                    .into(binding.imgDetail)
            }
            binding.tvTitle.text = detail.title
            binding.tvDetail.text = detail.explanation
            binding.tvLocation.visibility = View.GONE
            binding.tvYearMade.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}