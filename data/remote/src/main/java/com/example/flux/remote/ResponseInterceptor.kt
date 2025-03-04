package com.example.flux.remote

import com.example.flux.model.error.ErrorData
import com.example.flux.model.error.FieldError
import com.example.flux.model.exception.Http50xException
import com.example.flux.model.exception.HttpGeneralException
import com.example.flux.model.exception.HttpUnauthorizedException
import com.example.flux.remote.response.ErrorResponse
import com.example.flux.remote.response.FieldErrorResponse
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import javax.net.ssl.SSLHandshakeException

private const val GENERAL_ERROR_STATUS_CODE_MINIMUM = 400

class ResponseInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val response = chain.proceed(request)
            val statusCode = response.code
            if (statusCode == HttpURLConnection.HTTP_OK ||
                statusCode == HttpURLConnection.HTTP_CREATED
            ) {
                return response
            }
            val errorResponse = getErrorResponse(response.body)
            errorResponse?.statusCode = statusCode
            when {
                statusCode == HttpURLConnection.HTTP_INTERNAL_ERROR ||
                    statusCode == HttpURLConnection.HTTP_NOT_IMPLEMENTED ||
                    statusCode == HttpURLConnection.HTTP_BAD_GATEWAY ||
                    statusCode == HttpURLConnection.HTTP_UNAVAILABLE ||
                    statusCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT
                    -> throw Http50xException(errorResponse?.convert())

                statusCode == HttpURLConnection.HTTP_UNAUTHORIZED -> throw HttpUnauthorizedException(errorResponse?.convert())
                statusCode >= GENERAL_ERROR_STATUS_CODE_MINIMUM -> throw HttpGeneralException(errorResponse?.convert())
                else -> return response
            }
        } catch (exception: SocketTimeoutException) {
            throw SocketTimeoutException("timeout")
        } catch (exception: SSLHandshakeException) {
            throw exception
        }
    }

    private fun getErrorResponse(errorBody: ResponseBody?): ErrorResponse? {
        return errorBody?.let {
            try {
                Gson().fromJson(errorBody.string(), ErrorResponse::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun ErrorResponse.convert(): ErrorData {
        return ErrorData(
            errors = errors,
            fieldErrors = fieldErrors?.convertFieldError() ?: arrayListOf(),
            statusCode = statusCode,
            shouldAppear = shouldAppear,
            title = title,
            message = message,
        )
    }

    private fun List<FieldErrorResponse>.convertFieldError(): List<FieldError> {
        return map { FieldError(it.key, it.message) }
    }
}
