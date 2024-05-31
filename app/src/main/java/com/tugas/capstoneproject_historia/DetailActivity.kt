package com.tugas.capstoneproject_historia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tugas.capstoneproject_historia.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    var clicked = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // karena tidak terdapat di wikipiedia api
        binding.tvLocation.visibility = View.GONE
        binding.tvYearMade.visibility = View.GONE

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true);

        DetailViewModel().getData()

        DetailViewModel().data.observe(this) {
            val imgLink = it.query.pages.jsonMemberPage.thumbnail.source
            var detailText = it.query.pages.jsonMemberPage.extract
            detailText = DetailViewModel().removeReferencesAndExternalLinks(detailText)

            binding.tvTitle.text = it.query.normalized.first().to.toString()
            binding.tvDetails.text = Html.fromHtml(detailText, Html.FROM_HTML_MODE_COMPACT)
            binding.tvLink.text = it.query.pages.jsonMemberPage.fullurl

            Glide.with(this)
                .load(imgLink)
                .into(binding.imgDetail)
        }

        DetailViewModel().isLoading.observe(this) {
            showLoading(it)
        }

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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