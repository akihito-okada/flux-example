package com.example.flux.model.exception

import com.example.flux.model.error.ErrorData
import java.io.IOException

abstract class BaseHttpException : IOException() {

    abstract var errorData: ErrorData?
}
