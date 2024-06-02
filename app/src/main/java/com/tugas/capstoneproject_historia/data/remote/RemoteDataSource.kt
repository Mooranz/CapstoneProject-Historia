package com.tugas.capstoneproject_historia.data.remote

import android.content.Context
import com.tugas.capstoneproject_historia.R
import com.tugas.capstoneproject_historia.data.remote.response.LandmarkInfo

/* Remote Data Simulation */

class RemoteDataSource (private val context: Context) {

    fun getLandmarkInfo(): LandmarkInfo {
        return LandmarkInfo(
            title = context.getString(R.string.tugu_title),
            desc = context.getString(R.string.tugu_en_detail),
            year = context.getString(R.string.tugu_tahun_angka),
            locaction = context.getString(R.string.tugu_lokasi),
            img = context.getString(R.string.tugu_img)
        )
    }
}