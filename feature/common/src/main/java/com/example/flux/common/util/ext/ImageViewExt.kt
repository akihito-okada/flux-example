package com.example.flux.common.util.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Base64
import android.widget.ImageView
import com.example.flux.common.util.ext.BitmapExt.getCircledBitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import timber.log.Timber
import kotlin.math.ceil

object ImageViewExt {
    fun ImageView.setImageRectangle(url: String, scaledWidth: Int) {
        if (!isValidContextForGlide(context) || url.isEmpty()) {
            clear()
            return
        }

        // scaledWidthをImageViewのlayoutParamsから取得
        val params = layoutParams
        // 初期幅を設定
        params.width = scaledWidth
        layoutParams = params

        Glide.with(this)
            .load(url)
            .override(scaledWidth) // 幅に合わせたスケーリング
            .into(
                object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        val width = resource.intrinsicWidth
                        val height = resource.intrinsicHeight
                        val aspectRatio = height.toFloat() / width.toFloat()

                        params.height = ceil(scaledWidth * aspectRatio.toDouble()).toInt()
                        layoutParams = params

                        scaleType = ImageView.ScaleType.FIT_CENTER
                        setImageDrawable(resource)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        // 処理なし
                    }
                },
            )
    }

    fun ImageView.setImageSquare(url: String, scaledPix: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (url.isEmpty()) {
            clear()
            return
        }
        Glide
            .with(this)
            .load(url)
            .override(scaledPix)
            .into(this)
    }

    fun ImageView.setImageRes(imageRes: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (imageRes == 0) {
            clear()
            return
        }
        Glide
            .with(this)
            .load(imageRes)
            .into(this)
    }

    fun ImageView.setCircleImageUri(uri: Uri, scaledPix: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (uri == Uri.EMPTY) {
            clear()
            return
        }
        Glide.with(this)
            .load(uri)
            .override(scaledPix)
            .circleCrop()
            .into(this)
    }

    fun ImageView.setRectangleImageUri(uri: Uri, scaledWidth: Int, scaledHeight: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (uri == Uri.EMPTY) {
            clear()
            return
        }
        Glide.with(this)
            .load(uri)
            .override(scaledWidth, scaledHeight)
            .into(this)
    }

    fun ImageView.clear() {
        if (!isValidContextForGlide(context)) {
            return
        }
        Glide
            .with(this)
            .clear(this)
    }

    // For RoundedImageView
    fun ImageView.setImageRectangleWithScaleForRoundedImageView(imageUrl: String, scaledWidth: Int, scaledHeight: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (imageUrl.isEmpty()) {
            clear()
            return
        }
        Glide
            .with(this)
            .asBitmap()
            .load(imageUrl)
            .override(scaledWidth, scaledHeight)
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        setImageBitmap(resource)
                    }
                },
            )
    }

    fun ImageView.setImageRectangle(url: String, thumbnailUrl: String, scaledWidth: Int, scaledHeight: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (url.isEmpty()) {
            clear()
            return
        }
        Glide.with(this)
            .load(url)
            .thumbnail(
                Glide
                    .with(this)
                    .load(thumbnailUrl)
                    .centerCrop()
                    .override(scaledWidth, scaledHeight),
            )
            .centerCrop()
            .override(scaledWidth, scaledHeight)
            .into(this)
    }

    fun ImageView.setImageRectangleWithOriginalAspectRatio(url: String, scaledWidth: Int) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (url.isEmpty()) {
            clear()
            return
        }
        Glide.with(this)
            .load(url)
            .override(scaledWidth, Target.SIZE_ORIGINAL)
            .into(this)
    }

    fun ImageView.setImageRectangleWithOriginalAspectRatio(url: String) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (url.isEmpty()) {
            clear()
            return
        }
        Glide.with(this)
            .load(url)
            .into(this)
    }

    fun ImageView.loadCircularBase64Image(base64Image: String) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (base64Image.isEmpty()) {
            clear()
            return
        }
        val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
        val bimap = BitmapFactory
            .decodeByteArray(decodedString, 0, decodedString.size)
            .getCircledBitmap()
        Glide.with(this)
            .asBitmap()
            .centerCrop()
            .load(bimap)
            .into(this)
    }

    fun ImageView.loadImageUrl(url: String, onResourceReady: (resource: Bitmap) -> Unit) {
        if (!isValidContextForGlide(context)) {
            return
        }
        if (url.isEmpty()) {
            clear()
            return
        }
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(
                object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?,
                    ) {
                        onResourceReady.invoke(resource)
                    }
                },
            )
    }

    private fun isValidContextForGlide(context: Context?): Boolean {
        val activity = getActivity(context) ?: return false
        return !(activity.isDestroyed || activity.isFinishing)
    }

    private fun getActivity(context: Context?): Activity? {
        var tempContext = context ?: return null
        while (tempContext is ContextWrapper) {
            if (tempContext is Activity) {
                return tempContext
            }
            tempContext = tempContext.baseContext
        }
        return null
    }

    fun <T> ImageView.loadCircularImage(
        model: T,
        scaledPix: Int,
        onCompleted: (() -> Unit)? = null,
    ) {
        if (!isValidContextForGlide(context)) {
            onCompleted?.invoke()
            return
        }
        if (model is String && model.isBlank()) {
            onCompleted?.invoke()
            clear()
            return
        }
        Glide.with(this)
            .asBitmap()
            .load(model)
            .override(scaledPix)
            .dontTransform()
            .dontAnimate()
            .circleCrop()
            .listener(
                object : RequestListener<Bitmap> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        onCompleted?.invoke()
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        onCompleted?.invoke()
                        return false
                    }
                },
            )
            .into(this)
    }

    fun <T, M> ImageView.loadCircularImage(
        model: T,
        thumbnailModel: M,
        scaledPix: Int,
        thumbnailScaledPix: Int,
        onCompleted: (() -> Unit)? = null,
    ) {
        if (!isValidContextForGlide(context)) {
            onCompleted?.invoke()
            Timber.d("isInvalidContextForGlide")
            return
        }
        if (model is String && model.isBlank()) {
            clear()
            onCompleted?.invoke()
            Timber.d("isBlank")
            return
        }
        Glide.with(this)
            .asBitmap()
            .load(model)
            .thumbnail(
                Glide
                    .with(this)
                    .asBitmap()
                    .load(thumbnailModel)
                    .override(thumbnailScaledPix)
                    .dontTransform()
                    .dontAnimate()
                    .circleCrop()
                    .addListener(
                        object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                                onCompleted?.invoke()
                                Timber.d("onLoadFailed thumbnail")
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                                onCompleted?.invoke()
                                Timber.d("onResourceReady thumbnail")
                                return false
                            }
                        },
                    ),
            )
            .override(scaledPix)
            .dontTransform()
            .dontAnimate()
            .circleCrop()
            .listener(
                object : RequestListener<Bitmap> {
                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>, isFirstResource: Boolean): Boolean {
                        onCompleted?.invoke()
                        Timber.d("onLoadFailed")
                        return false
                    }

                    override fun onResourceReady(resource: Bitmap, model: Any, target: Target<Bitmap>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                        onCompleted?.invoke()
                        Timber.d("onResourceReady")
                        return false
                    }
                },
            )
            .into(this)
    }
}
