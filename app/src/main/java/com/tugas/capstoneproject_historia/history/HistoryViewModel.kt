package com.dicoding.asclepius.view.history

import androidx.lifecycle.ViewModel
import com.tugas.capstoneproject_historia.data.HistoryRepository
import com.dicoding.asclepius.data.entity.HistoryEntity

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun getHistory() = historyRepository.getHistory()

    fun insertHistory(historyEntity: HistoryEntity) = historyRepository.insertHistoryIntoDatabase(historyEntity)

    companion object {
        private const val TAG = "ResultViewModel"
    }

}