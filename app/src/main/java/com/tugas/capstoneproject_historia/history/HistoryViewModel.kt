package com.tugas.capstoneproject_historia.history

import androidx.lifecycle.ViewModel
import com.tugas.capstoneproject_historia.data.HistoryRepository
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    fun getHistory() = historyRepository.getHistory()

    fun insertHistory(historyEntity: HistoryEntity) = historyRepository.insertHistoryIntoDatabase(historyEntity)

    companion object {
        private const val TAG = "ResultViewModel"
    }

}