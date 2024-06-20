package com.tugas.capstoneproject_historia.ui.history

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityOptionsCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.ui.detail.DetailActivity
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(application)
        val viewModel: HistoryViewModel by viewModels { factory }

        val historyAdapter = HistoryAdapter()

        val nightModeFlags: Int =
            this.getResources().getConfiguration().uiMode and
                    Configuration.UI_MODE_NIGHT_MASK
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(this, R.drawable.baseline_arrow_forward_ios_24)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, Color.WHITE)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                val unwrappedDrawable =
                    AppCompatResources.getDrawable(this, R.drawable.baseline_arrow_forward_ios_24)
                val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
                DrawableCompat.setTint(wrappedDrawable, Color.BLACK)
            }
        }

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.app_final_name) + " - History"

        viewModel.getHistory().observe(this) { history ->
            historyAdapter.submitList(history)
            history.forEach(::println)

            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
                adapter = historyAdapter
            }

            historyAdapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: HistoryEntity) {
                    val intent = Intent(this@HistoryActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DETAIL, data)
                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this@HistoryActivity).toBundle())
                }
            })
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