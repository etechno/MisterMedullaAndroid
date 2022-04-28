package com.example.mistermedulla

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas

class EKG (initFrame: Bitmap, initLine: Bitmap) {
    var Frame: Bitmap
    var Line: Bitmap
    var x: Int = 0
    var y: Int = 0

//    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
//    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    init {
        Frame = Bitmap.createScaledBitmap(
            initFrame, 1000, 400, false
        )
        Line = Bitmap.createScaledBitmap(
            initLine, 1000, 400, false
        )
        x = 100
        y = 550
    }
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(Line, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(Frame, x.toFloat(), y.toFloat(), null)
    }
    fun drawFrame(canvas: Canvas) {
        canvas.drawBitmap(Frame, x.toFloat(), y.toFloat(), null)
    }
    fun drawLine(canvas: Canvas) {
        canvas.drawBitmap(Line, x.toFloat(), y.toFloat(), null)
    }
}