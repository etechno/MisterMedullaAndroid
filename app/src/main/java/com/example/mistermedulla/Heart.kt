package com.example.mistermedulla

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

class Heart(var center: Bitmap,var line: Bitmap, var flare: Bitmap) {
    var heartCenter: Bitmap
    var heartLine: Bitmap
    var heartFlare: Bitmap
    var health: Int = 100
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
//    private var xVelocity = 20
//    private var yVelocity = 20
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {

        heartCenter = Bitmap.createScaledBitmap(
            center, 200, 200, false
        )
        heartLine = Bitmap.createScaledBitmap(
            line, 200, 200, false
        )
        heartFlare = Bitmap.createScaledBitmap(
            flare, 202, 202, false
        )
        w = heartCenter.width
        h = heartCenter.height
//        x = screenWidth/2
//        y = screenHeight/2
        x = 1350
        y = 400
    }
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(heartFlare, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(heartLine, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(heartCenter, (x + (100 - health)).toFloat(), (y + (100 - health)).toFloat(), null)
    }
    // returns true if player is dead
    fun update(healthMod: Int): Boolean {
        // val randomNum = ThreadLocalRandom.current().nextInt(1, 5)

//        if (x > screenWidth - image.width || x < image.width) {
//            xVelocity = xVelocity * -1
//        }
//        if (y > screenHeight - image.height || y < image.height) {
//            yVelocity = yVelocity * -1
//        }
        health += healthMod
        if (health > 100) {
            health = 100
        }
        else if (health < 0) {
            health = 0
        }
        heartCenter = Bitmap.createScaledBitmap(
            center, (health * 2) + 1, (health * 2) + 1, false
        )
        if (health == 0) {
            return true
        }
        else {
            return false
        }
//        x += (xVelocity)
//        y += (yVelocity)
//        x = newX - w / 2
//        y = newY - h / 2
    }
    fun updateTouch(touch_x: Int, touch_y: Int) {
        x = touch_x - w / 2
        y = touch_y - h / 2
    }
}