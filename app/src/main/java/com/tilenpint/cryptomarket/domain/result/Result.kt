package com.tilenpint.cryptomarket.domain.result

sealed class Result<out T> {
    abstract val data: T?

    class Success<out T>(override val data: T) : Result<T>()
    class Progress<out T>(
        override val data: T? = null,
        val loadingStyle: LoadingStyle = LoadingStyle.NORMAL
    ) : Result<T>()

    class Error<out T>(val exception: Throwable, override val data: T? = null) : Result<T>()
}

fun <A, B> Result<A>.mapData(transform: (A) -> B): Result<B> {
    return when (this) {
        is Result.Error -> Result.Error(exception, data?.let(transform))
        is Result.Progress -> Result.Progress(data?.let(transform), loadingStyle)
        is Result.Success -> Result.Success(transform(data))
    }
}

enum class LoadingStyle {
    NORMAL,
    SILENT
}