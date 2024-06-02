package com.tugas.capstoneproject_historia.history

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.entity.HistoryEntity
import com.dicoding.asclepius.view.history.HistoryAdapter
import com.dicoding.asclepius.view.history.HistoryViewModel
import com.dicoding.asclepius.view.history.ViewModelFactory
import com.tugas.capstoneproject_historia.DetailActivity
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

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = getString(R.string.app_name) + " - History"

        viewModel.getHistory().observe(this) { history ->
            historyAdapter.submitList(history)

            binding.rvHistory.apply {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = historyAdapter
            }

            historyAdapter.setOnItemClickCallback(object : HistoryAdapter.OnItemClickCallback {
                override fun onItemClicked(data: HistoryEntity) {
                    val intent = Intent(this@HistoryActivity, DetailActivity::class.java)
//                    intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, data.imageUri)
//                    intent.putExtra(ResultActivity.EXTRA_RESULT, data.result)
                    startActivity(intent)
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