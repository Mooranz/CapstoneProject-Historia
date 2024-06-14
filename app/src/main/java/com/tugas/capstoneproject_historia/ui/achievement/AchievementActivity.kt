package com.tugas.capstoneproject_historia.ui.achievement

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.databinding.ActivityAchievementBinding


class AchievementActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAchievementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAchievementBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val nightModeFlags: Int =
            this.getResources().getConfiguration().uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(this, R.drawable.maps_with_pins)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(this, R.drawable.maps_with_pins)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.menu_maps ->{
                startActivity(Intent(this, MapsActivity::class.java))
            }

            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}