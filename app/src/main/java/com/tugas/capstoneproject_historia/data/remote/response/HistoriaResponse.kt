package com.tugas.capstoneproject_historia.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoriaResponse(

	@field:SerializedName("data")
	val data: Data?,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: String
)

@Parcelize
data class Data(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("confidenceScore")
	val confidenceScore: Int,

	@field:SerializedName("suggestion")
	val suggestion: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("explanation")
	val explanation: String,
/*
	@field:SerializedName("imageUrl")
	val imageUrl: String?*/
) : Parcelable
