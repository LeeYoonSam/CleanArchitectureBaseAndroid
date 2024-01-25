package com.ys.base.core.common.network.annotations

import android.annotation.SuppressLint

@SuppressLint("ExperimentalAnnotationRetention")
@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.CONSTRUCTOR,
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPEALIAS,
)
@RequiresOptIn(
    message = "This is internal API for the sandwich libraries. Do not depend on " +
        "this API in your own client code.",
    level = RequiresOptIn.Level.ERROR,
)
@DslMarker
annotation class InternalApi