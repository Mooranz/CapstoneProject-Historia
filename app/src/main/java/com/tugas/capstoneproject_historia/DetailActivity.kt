package com.tugas.capstoneproject_historia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tugas.capstoneproject_historia.data.remote.RemoteDataSource
import com.tugas.capstoneproject_historia.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        setupData(this)
        binding.tvLink.text = getString(R.string.tugu_link)

        if (!clicked) {
            binding.tvLink.setTextColor(getColor(com.bumptech.glide.R.color.material_deep_teal_200))
        } else binding.tvLink.setTextColor(getColor(com.bumptech.glide.R.color.abc_tint_default))

        binding.tvLink.setOnClickListener {
            intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(binding.tvLink.text.toString())
            startActivity(intent)

            clicked = true
            binding.tvLink.setTextColor(getColor(com.bumptech.glide.R.color.abc_tint_default))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupData(context: Context) {
        val repo = RemoteDataSource(this)
        val landmarkInfo = repo.getLandmarkInfo().apply {
            binding.apply {
                tvTitle.text = title
                tvYearMade.text = "Dibangun Tahun: $year"
                tvLocation.text = locaction
                tvDetails.text = Html.fromHtml(desc, Html.FROM_HTML_MODE_COMPACT)

                Glide.with(context)
                    .load(img)
                    .into(imgDetail)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}