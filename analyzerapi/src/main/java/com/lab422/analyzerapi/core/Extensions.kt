package com.lab422.analyzerapi.core

import java.io.IOException
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody

val Boolean.apiValue: String
    get() = if (this) { "1" } else { "0" }

@Throws(IOException::class)
fun copy(body: ResponseBody, limit: Long): ResponseBody {
    val source = body.source()
    if (source.request(limit)) throw IOException("body too long!")
    val bufferedCopy = source.buffer.clone()
    return bufferedCopy.asResponseBody(body.contentType(), body.contentLength())
}
