package com.ys.base.core.common.network.mapper

import com.ys.base.core.common.network.ApiResponse

/**
 * A mapper interface for mapping [ApiResponse.Failure] response to the same [ApiResponse.Failure] type.
 */
interface ApiResponseFailureSuspendMapper : ApiFailureMapper {

    /**
     * Maps an [ApiResponse.Failure].
     *
     * @param apiResponse The [ApiResponse.Failure] error response from the network request.
     * @return The same type of the [ApiResponse.Failure].
     */
    suspend fun map(apiResponse: ApiResponse.Failure<*>): ApiResponse.Failure<*>
}