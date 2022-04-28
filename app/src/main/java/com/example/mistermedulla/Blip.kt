package com.example.mistermedulla

import android.graphics.Bitmap
import android.graphics.Canvas

class Blip (initBlip: Bitmap) {
    var Image: Bitmap
    var x: Int = 0
    var y: Int = 0
    var lifeTime: Int = 0
    init {
        Image = Bitmap.createScaledBitmap(
            initBlip, 125, 360, false
        )
        x = 100
        y = 570
    }
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(Image, x.toFloat(), y.toFloat(), null)
    }
    fun update() {
        x += 125
        lifeTime += 1
    }
    public fun getLivingTicks(): Int {
        return lifeTime
    }
}