package com.tugas.capstoneproject_historia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
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

        val imgLink = getString(R.string.tugu_img)
        val detailText = getString(R.string.tugu_en_detail)

        binding.tvTitle.text = getString(R.string.tugu_title)
        binding.tvDetails.text = Html.fromHtml(detailText, Html.FROM_HTML_MODE_COMPACT)
        binding.tvLink.text = getString(R.string.tugu_link)

        Glide.with(this)
            .load(imgLink)
            .into(binding.imgDetail)

        if (clicked == false) {
            binding.tvLink.setTextColor(getColor(com.bumptech.glide.R.color.material_blue_grey_800))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}