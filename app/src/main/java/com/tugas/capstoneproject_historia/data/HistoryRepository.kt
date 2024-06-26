package com.tugas.capstoneproject_historia.data

import androidx.lifecycle.LiveData
import com.tugas.capstoneproject_historia.data.entity.HistoryEntity
import com.tugas.capstoneproject_historia.data.room.HistoryDao
import com.tugas.capstoneproject_historia.utils.AppExecutors

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