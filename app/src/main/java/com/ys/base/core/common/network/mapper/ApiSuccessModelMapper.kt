package com.ys.base.core.common.network.mapper

import com.ys.base.core.common.network.ApiResponse

fun interface ApiSuccessModelMapper<T, V> {

    /**
     * 매퍼를 사용하여 [ApiResponse.Success]를 [V]에 매핑합니다.
     *
     * @param apiSuccessResponse 네트워크 요청의 [ApiResponse.Success] 응답입니다.
     * 반환 사용자 정의 [V] 성공 응답 모델.
     */
    fun map(apiSuccessResponse: ApiResponse.Success<T>): V
}