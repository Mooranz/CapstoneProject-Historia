package com.tugas.capstoneproject_historia

import com.google.gson.annotations.SerializedName

data class WikiResponse(

	@field:SerializedName("batchcomplete")
	val batchcomplete: String,

	@field:SerializedName("query")
	val query: Query
)

data class Thumbnail(

	@field:SerializedName("width")
	val width: Int,

	@field:SerializedName("source")
	val source: String,

	@field:SerializedName("height")
	val height: Int
)

data class Query(

	@field:SerializedName("pages")
	val pages: Pages,

	@field:SerializedName("normalized")
	val normalized: List<NormalizedItem>
)

data class NormalizedItem(

	@field:SerializedName("from")
	val from: String,

	@field:SerializedName("to")
	val to: String
)

data class Pages(
	@field:SerializedName("61588237")
	val jsonMemberPage: JsonMemberPage
)

data class JsonMemberPage(

	@field:SerializedName("thumbnail")
	val thumbnail: Thumbnail,

	@field:SerializedName("touched")
	val touched: String,

	@field:SerializedName("ns")
	val ns: Int,

	@field:SerializedName("canonicalurl")
	val canonicalurl: String,

	@field:SerializedName("contentmodel")
	val contentmodel: String,

	@field:SerializedName("fullurl")
	val fullurl: String,

	@field:SerializedName("pagelanguagehtmlcode")
	val pagelanguagehtmlcode: String,

	@field:SerializedName("length")
	val length: Int,

	@field:SerializedName("pageid")
	val pageid: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("lastrevid")
	val lastrevid: Int,

	@field:SerializedName("extract")
	val extract: String,

	@field:SerializedName("pageimage")
	val pageimage: String,

	@field:SerializedName("pagelanguage")
	val pagelanguage: String,

	@field:SerializedName("pagelanguagedir")
	val pagelanguagedir: String,

	@field:SerializedName("editurl")
	val editurl: String
)
