package com.depromeet.housekeeper.util

import android.graphics.drawable.PictureDrawable
import androidx.annotation.Nullable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG


class SvgDrawableTranscoder : ResourceTranscoder<SVG?, PictureDrawable?> {
    @Nullable
    override fun transcode(
        toTranscode: Resource<SVG?>,
        options: Options
    ): Resource<PictureDrawable?>? {
            val svg: SVG = toTranscode.get()
            val picture = svg.renderToPicture()
            val drawable = PictureDrawable(picture)
            return SimpleResource(drawable)
    }

}