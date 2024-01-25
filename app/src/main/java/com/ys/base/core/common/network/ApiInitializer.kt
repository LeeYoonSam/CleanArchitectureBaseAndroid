package com.ys.base.core.common.network

import com.ys.base.core.common.network.mapper.ApiFailureMapper
import com.ys.base.core.common.network.operators.BaseOperator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * ApiInitializer is a rules and strategies initializer of the network response.
 */
//@ThreadLocal
object ApiInitializer {

    /**
     * determines the success code range of network responses.
     *
     * if a network request is successful and the response code is in the [successCodeRange],
     * its response will be a [ApiResponse.Success].
     *
     * if a network request is successful but out of the [successCodeRange] or failure,
     * the response will be a [ApiResponse.Failure.Error].
     * */
    @JvmStatic
    var successCodeRange: IntRange = 200..299

    /**
     * A list of global operators that is executed by [ApiResponse]s globally on each response.
     *
     * [ApiResponseOperator] allows you to handle success and error response instead of
     * the [ApiResponse.onSuccess], [ApiResponse.onError], [ApiResponse.onException] transformers.
     * [ApiResponseSuspendOperator] can be used for suspension scope.
     *
     * By setting [apiOperators], you don't need to set operator for every [ApiResponse] by using [ApiResponse.of] function.
     */
    @JvmStatic
    var apiOperators: MutableList<BaseOperator> = mutableListOf()

    /**
     * A list of global failure mappers that is executed by [ApiResponse]s globally on each response.
     *
     * [ApiResponseFailureMapper] allows you to map the failure responses
     * to transform the payload, or changes to custom types.
     * [ApiResponseFailureSuspendMapper] can be used for suspension scope.
     *
     * By setting [apiFailureMapper], it will automatically maps all [ApiResponse] by using [ApiResponse.of] function.
     */
    @JvmStatic
    var apiFailureMapper: MutableList<ApiFailureMapper> = mutableListOf()

    /**
     * A [CoroutineScope] for executing and operating the overall Retrofit network requests.
     */
    @JvmSynthetic
    var apiScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * A global [Timeout] for operating the [ApiResponseCallAdapterFactory].
     *
     * Returns a timeout that spans the entire call: resolving DNS, connecting, writing the request
     * body, server processing, and reading the response body. If the call requires redirects or
     * retries all must complete within one timeout period.
     */
//    @JvmStatic
//    var apiTimeout: Timeout? = null
}