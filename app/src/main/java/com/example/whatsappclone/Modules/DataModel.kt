package com.example.whatsappclone.Modules


enum class DataModelType {
    START_AUDIO_CALL, START_VIDEO_CALL, Offer, Answer, Endcall
}

data class DataModel(
    val sender: String? = null,
    val target: String? = null,
    val type: DataModelType? = null,
    val data: String? = null,
    val timeStamp: Long? = System.currentTimeMillis()
)

fun DataModel.isvalid(): Boolean {
    return System.currentTimeMillis() - this.timeStamp!! < 60000
}
