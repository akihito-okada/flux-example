package com.example.flux.common.delegate

import com.example.flux.common.R
import com.example.flux.common.flux.Dispatcher
import com.example.flux.common.flux.util.DispatcherHandler
import com.example.flux.model.error.ErrorData
import com.example.flux.model.exception.Http50xException
import com.example.flux.model.exception.HttpGeneralException
import com.example.flux.model.exception.HttpUnauthorizedException
import com.example.flux.model.util.ResourceProvider
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineExceptionHandler
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface ErrorHandlerDelegate {
    val dispatcher: Dispatcher
    val ignoreExceptionHandler: CoroutineExceptionHandler
    fun onError(e: Throwable)
    fun onError(e: Throwable, showSnackbarMessage: (String) -> Unit)
}

class ErrorHandlerDelegateImpl(
    override val dispatcher: Dispatcher,
    private val resourceProvider: ResourceProvider,
    private val showMessageDelegate: ShowMessageDelegate,
) : ErrorHandlerDelegate, DispatcherHandler, ShowMessageDelegate by showMessageDelegate {

    override val ignoreExceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
    }

    override fun onError(e: Throwable) {
        onError(e) { message ->
            showSnackbarMessage(message)
        }
    }

    override fun onError(e: Throwable, showSnackbarMessage: (String) -> Unit) {
        if (e.cause is CancellationException) {
            Timber.d(e)
            return
        }
        when (e) {
            is CancellationException -> {
                Timber.d(e)
            }

            is UnknownHostException,
            is SocketTimeoutException,
            is ConnectException,
                -> {
                Timber.e(e)
                val message = R.string.network_error
                showSnackbarMessage.invoke(resourceProvider.getString(message))
            }

            is Http50xException -> {
                Timber.e(e)
                showDialogMessage(R.string.maintenance_title, R.string.maintenance_description)
            }

            is HttpUnauthorizedException -> {
                Timber.e(e)
                showMessage(e.errorData, showSnackbarMessage)
            }

            is HttpGeneralException -> {
                Timber.e(e)
                showMessage(e.errorData, showSnackbarMessage)
            }

            else -> {
                Timber.e(e)
                showSnackbarMessage.invoke(resourceProvider.getString(R.string.general_error))
            }
        }
    }

    private fun showMessage(errorData: ErrorData?, showSnackbarMessage: (String) -> Unit) {
        val title = errorData?.title
        val message = errorData?.message
        when {
            title.isNullOrEmpty() && message.isNullOrEmpty() -> {
                showSnackbarMessage.invoke(resourceProvider.getString(R.string.general_error))
            }

            title.isNullOrEmpty() && !message.isNullOrEmpty() -> {
                showSnackbarMessage.invoke(message)
            }

            !title.isNullOrEmpty() && !message.isNullOrEmpty() -> {
                showDialogMessage(title, message)
            }
        }
    }
}
