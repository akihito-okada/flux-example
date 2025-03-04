package com.example.flux.repository.util.ext

import kotlinx.coroutines.Job

object JobExt {
    fun Job.cancelIfNeeded() {
        if (isCancelled.not()) {
            cancel()
        }
    }
}
