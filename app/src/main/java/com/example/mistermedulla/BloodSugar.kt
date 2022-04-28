package com.example.mistermedulla

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

class BloodSugar(var initLeaf: Bitmap,var initChocolate: Bitmap) {
    var chocolate: Bitmap
    var leaf: Bitmap
    var realChocolate: Bitmap
    var sugarLevel: Int = 325
    var x: Int = 0
    var y: Int = 0
    var sugarX: Int = 325

    init {
        leaf = Bitmap.createScaledBitmap(
            initLeaf, 550, 300, false
        )
        chocolate = Bitmap.createScaledBitmap(
            initChocolate, 650, 300, false
        )
        realChocolate = Bitmap.createBitmap(chocolate,sugarX,0,650 - sugarX,300)
        x = 500
        y = 125
    }
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(leaf, x.toFloat(), y.toFloat(), null)
        canvas.drawBitmap(realChocolate, (x - 50 +sugarX).toFloat(), y.toFloat(), null)
    }
    fun update(foodLevel: Int, insulin: Boolean) {
        sugarLevel += (foodLevel / 100) - 3
        if (insulin) {
            sugarLevel -= 100
        }
        if (sugarLevel > 550) {
            sugarLevel = 550
        }
        else if (sugarLevel <= 0) {
            sugarLevel = 1
        }
        sugarX = ((650 - sugarLevel)/ 11) * 13
        if (sugarX >= 650) {
            sugarX = 649
        }
        var SugarWidth: Int = 650 - sugarX
        realChocolate = Bitmap.createBitmap(chocolate,sugarX,0,SugarWidth,300)
    }
    fun getSugarLow(): Boolean {
        return sugarLevel < 100
    }
    fun getSugarHigh(): Boolean {
        return sugarLevel > 500
    }
}