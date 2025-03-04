package com.example.flux.model.exception

import com.example.flux.model.error.ErrorData

class HttpGeneralException(override var errorData: ErrorData?) : BaseHttpException()
