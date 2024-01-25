package com.ys.base.core.common.network

/**
 * A callback interface for observing [ApiResponse] value updating.
 */
fun interface ResponseObserver<T> {

    /** observes a new [ApiResponse] value. */
    fun observe(response: ApiResponse<T>)
}