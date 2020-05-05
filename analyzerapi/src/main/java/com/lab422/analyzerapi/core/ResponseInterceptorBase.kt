package com.lab422.analyzerapi.core

import com.lab422.common.Logger
import com.lab422.common.StringProvider
import com.lab422.common.api.ApiException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

abstract class ResponseInterceptorBase(
    private val StringProvider: StringProvider,
    private val logger: Logger
) : Interceptor {

    @Suppress("PrivatePropertyName")
    protected val TAG = this.javaClass.name

    abstract fun processResponse(response: Response)

    abstract fun unknownException(ex: Throwable): Throwable

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)

            processResponse(response)

            return response

        } catch (e: ApiException) {
            throw e
        } catch (e: IOException) {
            // StreamResetException, SocketTimeoutException
            // SSLException, UnknownHostException
            throw ApiException.networkException(e, StringProvider)
        } catch (e: Throwable) {
            logger.e(TAG, e)
            throw unknownException(e)
        }
    }
}