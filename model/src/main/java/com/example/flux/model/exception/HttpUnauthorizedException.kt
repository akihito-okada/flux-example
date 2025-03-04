package com.example.flux.model.exception

import com.example.flux.model.error.ErrorData

class HttpUnauthorizedException(override var errorData: ErrorData?) : BaseHttpException()
