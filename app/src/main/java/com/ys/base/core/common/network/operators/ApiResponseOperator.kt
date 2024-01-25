package com.ys.base.core.common.network.operators

import com.ys.base.core.common.network.ApiResponse

/**
 * ApiResponseOperator는 [ApiResponse]에 대해 작동하고 [ApiResponse]를 반환합니다.
 * 이를 통해 [onSuccess], [onError],[onException] 트랜스포머 대신 성공 및 오류 응답을 처리할 수 있습니다.
 * 이 연산자는 싱글톤 인스턴스로 전역적으로 적용하거나 각 [ApiResponse]에 하나씩 적용할 수 있습니다.
 */
abstract class ApiResponseOperator<T> : BaseOperator {

    /**
     * 요청이 성공하면 성공적인 응답을 처리하기 위해 [ApiResponse.Success]를 작동합니다.
     * @param apiResponse 성공적인 응답.
     */
    abstract fun onSuccess(apiResponse: ApiResponse.Success<T>)

    /**
     * 요청이 실패한 경우 오류 응답을 처리하기 위해 [ApiResponse.Failure.Error]를 작동합니다.
     * @param apiResponse 실패한 응답.
     */
    abstract fun onError(apiResponse: ApiResponse.Failure.Error)

    /**
     * 요청에 예외가 발생하면 예외 응답을 처리하기 위해 [ApiResponse.Failure.Exception]을 작동합니다.
     * @param apiResponse 예외 응답입니다.
     */
    abstract fun onException(apiResponse: ApiResponse.Failure.Exception)
}