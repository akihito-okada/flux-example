package com.example.flux.model.exception

import com.example.flux.model.error.ErrorData

class Http50xException(override var errorData: ErrorData?) : BaseHttpException()
