package com.tugas.capstoneproject_historia.data

import androidx.lifecycle.LiveData
import com.dicoding.asclepius.data.entity.HistoryEntity
import com.dicoding.asclepius.data.room.HistoryDao
import com.dicoding.asclepius.utils.AppExecutors

class HistoryRepository private constructor(
    private val historyDao: HistoryDao,
    private val appExecutors: AppExecutors
) {

    fun getHistory(): LiveData<List<HistoryEntity>> = historyDao.getHistory()

    fun insertHistoryIntoDatabase(historyEntity: HistoryEntity) {
        appExecutors.diskIO.execute {
            historyDao.insertHistory(historyEntity)
        }
    }

    companion object {
        @Volatile
        private var instance: HistoryRepository? = null
        fun  getInstance(
            historyDao: HistoryDao,
            appExecutors: AppExecutors
        ): HistoryRepository = instance ?: synchronized(this) {
            instance ?: HistoryRepository(historyDao, appExecutors)
        }.also { instance = it }
    }
}