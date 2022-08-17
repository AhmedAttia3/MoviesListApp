package com.smart.movieslist.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.smart.movieslist.R
import java.io.IOException
import java.net.SocketTimeoutException

fun Context.showLongToast(message: String) {
    Toast.makeText(
        this,
        message,
        Toast.LENGTH_LONG
    ).show()
}

fun Throwable.getErrorType(): Constants.ErrorType {
    return when (this) {
        is SocketTimeoutException -> Constants.ErrorType.TIMEOUT
        is IOException -> Constants.ErrorType.NETWORK
        else -> Constants.ErrorType.UNKNOWN
    }
}

fun Constants.ErrorType.getMessage(context: Context): String = when (this) {
    Constants.ErrorType.NETWORK -> context.resources.getString(R.string.noInternet)
    Constants.ErrorType.TIMEOUT -> context.resources.getString(R.string.timeout)
    Constants.ErrorType.UNKNOWN -> context.resources.getString(R.string.unknown)
}

@SuppressLint("CheckResult")
@BindingAdapter(
    "srcUrl",
    "placeholder",
    requireAll = true // make the attributes required
)
fun ImageView.bindSrcUrl(
    url: String?,
    placeholder: Drawable?,
) = Glide.with(this).load(url).centerCrop()
    .let { request ->
        if (placeholder != null) {
            request.placeholder(placeholder)
        }

        request.into(this)
    }
