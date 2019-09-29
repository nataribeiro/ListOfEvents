package br.com.natanaelribeiro.events.customviews

import com.squareup.picasso.Transformation
import android.graphics.*


class TopRoundedTransformation(
    private val radius: Float,
    private val margin: Float,
    private val topCorners: Boolean,
    private val bottomCorners: Boolean
) : Transformation {


    override fun key(): String {

        return "rounded_$radius$margin"
    }

    override fun transform(source: Bitmap?): Bitmap {

        source?.let {

            val paint = Paint()
            paint.isAntiAlias = true
            paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

            val output =
                Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            if (topCorners && bottomCorners) {
                // Uses native method to draw symmetric rounded corners
                canvas.drawRoundRect(
                    RectF(
                        margin, margin, source.width - margin,
                        source.height - margin
                    ), radius, radius, paint
                )
            } else {
                // Uses custom path to generate rounded corner individually
                canvas.drawPath(
                    RoundedRect(
                        margin, margin, source.width - margin,
                        source.height - margin, radius, radius, topCorners, topCorners,
                        bottomCorners, bottomCorners
                    ), paint
                )
            }


            if (source !== output) {
                source.recycle()
            }

            return output

        }

        return source!!
    }

    fun RoundedRect(
        leftX: Float, topY: Float, rightX: Float, bottomY: Float, rx: Float,
        ry: Float, topLeft: Boolean, topRight: Boolean, bottomRight: Boolean, bottomLeft: Boolean
    ): Path {
        var rx = rx
        var ry = ry
        val path = Path()
        if (rx < 0) rx = 0f
        if (ry < 0) ry = 0f
        val width = rightX - leftX
        val height = bottomY - topY
        if (rx > width / 2) rx = width / 2
        if (ry > height / 2) ry = height / 2
        val widthMinusCorners = width - 2 * rx
        val heightMinusCorners = height - 2 * ry

        path.moveTo(rightX, topY + ry)
        if (topRight)
            path.rQuadTo(0f, -ry, -rx, -ry)//top-right corner
        else {
            path.rLineTo(0f, -ry)
            path.rLineTo(-rx, 0f)
        }
        path.rLineTo(-widthMinusCorners, 0f)
        if (topLeft)
            path.rQuadTo(-rx, 0f, -rx, ry) //top-left corner
        else {
            path.rLineTo(-rx, 0f)
            path.rLineTo(0f, ry)
        }
        path.rLineTo(0f, heightMinusCorners)

        if (bottomLeft)
            path.rQuadTo(0f, ry, rx, ry)//bottom-left corner
        else {
            path.rLineTo(0f, ry)
            path.rLineTo(rx, 0f)
        }

        path.rLineTo(widthMinusCorners, 0f)
        if (bottomRight)
            path.rQuadTo(rx, 0f, rx, -ry) //bottom-right corner
        else {
            path.rLineTo(rx, 0f)
            path.rLineTo(0f, -ry)
        }

        path.rLineTo(0f, -heightMinusCorners)

        path.close()//Given close, last lineto can be removed.

        return path
    }
}