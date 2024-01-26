package com.ys.base.core.common.network

import com.ys.base.core.common.network.mapper.ApiResponseFailureMapper
import com.ys.base.core.common.network.mapper.ApiResponseFailureSuspendMapper
import com.ys.base.core.common.network.operators.ApiResponseOperator
import com.ys.base.core.common.network.operators.ApiResponseSuspendOperator
import kotlinx.coroutines.launch

sealed interface ApiResponse<out T> {
    data class Success<T>(val data: T): ApiResponse<T>

    /**
     *  OkHttp 요청 콜의 API 실패 응답 클래스
     *  subtypes: [ApiResponse.Failure.Error] and [ApiResponse.Failure.Exception].
     */
    sealed interface Failure<T>: ApiResponse<T> {

        /**
         * API 응답 에러 상황
         * 예시: internal server error.
         *
         * @property payload 에러 상세 정보를 포함하는 payload
         */
        open class Error(val payload: Any?): Failure<Nothing> {
            override fun equals(other: Any?): Boolean = other is Error &&
                payload == other.payload

            override fun hashCode(): Int {
                var result = 17
                result = 31 * result + payload.hashCode()
                return result
            }

            override fun toString(): String = payload.toString()
        }

        /**
         * API 요청 예외 상황
         * 클라이언트 측에서 요청이나 응답 진행중 예상치 못한 에러가 발생
         * 예시: 네트워크 연결 에러, 타임아웃 등
         *
         * @param throwable 예외 throwable
         *
         * @property message 예외로부터 생성된 메시지
         */
        open class Exception(val throwable: Throwable): Failure<Nothing> {
            val message: String? = throwable.message

            override fun equals(other: Any?): Boolean = other is Exception &&
                throwable == other.throwable

            override fun hashCode(): Int {
                var result = 17
                result = 31 * result + throwable.hashCode()
                return result
            }

            override fun toString(): String = message.orEmpty()
        }
    }

    companion object {
        /**
         * [Failure] factory 함수.[Throwable] 인자를 받습니다.
         *
         * @param ex A throwable.
         *
         * @return [ApiResponse.Failure.Exception] 기반의 throwable.
         */
        fun exception(ex: Throwable): Failure.Exception =
            Failure.Exception(ex).apply { operate().maps() }

        /**
         * ApiResponse Factory.
         *
         * 전달 받은 [f] 로부터 [ApiResponse] 생성.
         *
         * [f] 가 예외가 발생 하지 않으면 [ApiResponse.Success]를 만듭니다.
         * [f] 가 예외가 발생 하면 [ApiResponse.Failure.Exception]를 만듭니다.
         */
        inline fun <reified T> of(crossinline f: () -> T): ApiResponse<T> {
            return try {
                val result = f()
                Success(
                    data = result,
                )
            } catch (e: Exception) {
                exception(e)
            }.operate().maps()
        }

        /**
         * ApiResponse Factory.
         *
         * 전달 받은 [f] 로부터 [ApiResponse] 생성.
         *
         * [f] 가 예외가 발생 하지 않으면 [ApiResponse.Success]를 만듭니다.
         * [f] 가 예외가 발생 하면 [ApiResponse.Failure.Exception]를 만듭니다.
         */
        suspend inline fun <reified T> suspendOf(
            crossinline f: suspend () -> T,
        ): ApiResponse<T> {
            val result = f()
            return of { result }
        }

        /**
         * Operates if there is a global [BaseOperator]
         * which operates on [ApiResponse]s globally on each response and returns the target [ApiResponse].
         *
         * @return [ApiResponse] A target [ApiResponse].
         */
        @Suppress("UNCHECKED_CAST")
        fun <T> ApiResponse<T>.operate(): ApiResponse<T> = apply {
            val globalOperators = ApiInitializer.apiOperators
            globalOperators.forEach { globalOperator ->
                if (globalOperator is ApiResponseOperator<*>) {
                    operator(globalOperator as ApiResponseOperator<T>)
                } else if (globalOperator is ApiResponseSuspendOperator<*>) {
                    val scope = ApiInitializer.apiScope
                    scope.launch {
                        suspendOperator(globalOperator as ApiResponseSuspendOperator<T>)
                    }
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun <T> ApiResponse<T>.maps(): ApiResponse<T> {
            val mappers = ApiInitializer.apiFailureMapper
            var response: ApiResponse<T> = this
            mappers.forEach { mapper ->
                if (response is Failure) {
                    if (mapper is ApiResponseFailureMapper) {
                        response = mapper.map(response as Failure<T>) as ApiResponse<T>
                    } else if (mapper is ApiResponseFailureSuspendMapper) {
                        val scope = ApiInitializer.apiScope
                        scope.launch {
                            response = mapper.map(response as Failure<T>) as ApiResponse<T>
                        }
                    }
                }
            }
            return response
        }
    }
}