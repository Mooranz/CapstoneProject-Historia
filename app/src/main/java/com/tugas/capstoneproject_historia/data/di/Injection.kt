package com.tugas.capstoneproject_historia.data.di

import android.content.Context
import com.tugas.capstoneproject_historia.data.HistoryRepository
import com.tugas.capstoneproject_historia.data.room.HistoryDatabase
import com.tugas.capstoneproject_historia.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): HistoryRepository {
        val database = HistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        val appExecutors = AppExecutors()
        return HistoryRepository.getInstance(dao, appExecutors)
    }
}