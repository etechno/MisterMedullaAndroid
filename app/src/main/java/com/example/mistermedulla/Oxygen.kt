package com.example.mistermedulla

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.graphics.createBitmap

class Oxygen (var initBubbles: Bitmap,var initBubblesBackground: Bitmap, var initBubblesFill: Bitmap, var initBreathCanister: Bitmap, var initBreathFill: Bitmap) {
    var bubbles: Bitmap
    var bubblesBackground: Bitmap
    var bubblesFill: Bitmap
    var bubblesFillReal: Bitmap
    var breathCanister: Bitmap
    var breathFill: Bitmap
    var breathFillReal: Bitmap
    var oxygen: Int = 100
    var air: Int = 100
    var bubblesY: Int = 0
    var oxygenY: Int = 0
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {

        bubbles = Bitmap.createScaledBitmap(
            initBubbles, 200, 800, false
        )
        bubblesBackground = Bitmap.createScaledBitmap(
            initBubblesBackground, 200, 800, false
        )
        bubblesFill = Bitmap.createScaledBitmap(
            initBubblesFill, 200, 800, false
        )
        bubblesFillReal = Bitmap.createBitmap(bubblesFill,0,0,200,800)
        breathCanister = Bitmap.createScaledBitmap(
            initBreathCanister, 200, 800, false
        )
        breathFill = Bitmap.createScaledBitmap(
            initBreathFill, 200, 800, false
        )
        breathFillReal = Bitmap.createBitmap(breathFill,0,0,200,800)
        w = breathCanister.width
        h = breathCanister.height
        x = 2050
        y = 300
    }
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(breathCanister,x.toFloat(), y.toFloat(),null)
        canvas.drawBitmap(breathFillReal, x.toFloat(), (y+oxygenY).toFloat(), null)
        canvas.drawBitmap(bubbles, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(bubblesBackground, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(bubblesFillReal, x.toFloat(), (y+bubblesY).toFloat(), null)
    }
    fun update(airMod: Int, beat: Boolean, lastBreath: Int) {
        air += airMod
        if (beat == true && lastBreath > 7){
            oxygen += air / 6
            air -= air / 8
        } else {
            oxygen -= 2
        }
        if (air > 100) {
            air = 100
        }
        else if (air < 1) {
            air = 0
        }
        if (oxygen > 100) {
            oxygen = 100
        }
        else if (oxygen < 1) {
            oxygen = 0
        }

        bubblesY = 8*(100-air)
        if (bubblesY >= 800){
            bubblesY = 799
        }
        var bubblesH: Int = 800 - bubblesY
        bubblesFillReal = Bitmap.createBitmap(bubblesFill,0,bubblesY,200,bubblesH)
        oxygenY = 8*(100-oxygen)
        if (oxygenY >= 800) {
            oxygenY = 799
        }
        var oxygenH: Int = 800 - oxygenY
        breathFillReal = Bitmap.createBitmap(breathFill,0,0,200,oxygenH)
//        x = newX - w / 2
//        y = newY - h / 2
        return
    }
    fun oxygenTooLow():Boolean {
        return oxygen < 15
    }
}