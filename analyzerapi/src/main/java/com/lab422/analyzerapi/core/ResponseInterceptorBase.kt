package com.lab422.analyzerapi.core

import com.lab422.common.Logger
import com.lab422.common.StringProvider
import com.lab422.common.api.ApiException
import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

abstract class ResponseInterceptorBase(
    private val stringProvider: StringProvider,
    private val logger: Logger
) : Interceptor {

    @Suppress("PrivatePropertyName")
    protected val tag: String = this.javaClass.name

    abstract fun processResponse(response: Response)

    abstract fun unknownException(ex: Throwable): Throwable

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)

            processResponse(response)

            return response
        } catch (ioException: IOException) {
            // StreamResetException, SocketTimeoutException
            // SSLException, UnknownHostException
            throw ApiException.networkException(ioException, stringProvider)
        } catch (exception: Throwable) {
            logger.e(tag, exception)
            throw unknownException(exception)
        }
    }
}
