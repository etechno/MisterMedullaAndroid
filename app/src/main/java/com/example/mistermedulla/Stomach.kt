package com.example.mistermedulla

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas


class Stomach (var initBone: Bitmap, var initFood1: Bitmap, var initFood2: Bitmap, var initFood3: Bitmap, var initFood4: Bitmap, var initFood5: Bitmap) {
    var bone: Bitmap
    var food1: Bitmap
    var food2: Bitmap
    var food3: Bitmap
    var food4: Bitmap
    var food5: Bitmap
    var foodFill: Int = 1000
    var x: Int = 100
    var y: Int = 100

    init {

        bone = Bitmap.createScaledBitmap(
            initBone, 300, 300, false
        )
        food1 = Bitmap.createScaledBitmap(
            initFood1, 300, 300, false
        )
        food2 = Bitmap.createScaledBitmap(
            initFood2, 300, 300, false
        )
        food3 = Bitmap.createScaledBitmap(
            initFood3, 300, 300, false
        )
        food4 = Bitmap.createScaledBitmap(
            initFood4, 300, 300, false
        )
        food5 = Bitmap.createScaledBitmap(
            initFood5, 300, 300, false
        )
        x = 100
        y = 125
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(bone, x.toFloat(), y.toFloat(), null)
        if (foodFill >= 825) {
            canvas.drawBitmap(food5, x.toFloat(), y.toFloat(), null)
        }
        else if (foodFill >= 650) {
            canvas.drawBitmap(food4, x.toFloat(), y.toFloat(), null)
        }
        else if (foodFill >= 475) {
            canvas.drawBitmap(food3, x.toFloat(), y.toFloat(), null)
        }
        else if (foodFill >= 300) {
            canvas.drawBitmap(food2, x.toFloat(), y.toFloat(), null)
        }
        else if (foodFill >= 125) {
            canvas.drawBitmap(food1, x.toFloat(), y.toFloat(), null)
        }

    }
    fun update(ate: Boolean) {
        if (ate) {
            foodFill += 250
        }
        foodFill -= 15
        if (foodFill >= 1000) {
            foodFill = 1000
        }
        else if (foodFill <= 0){
            foodFill = 0
        }
    }
    fun getFoodLevel(): Int {
        return foodFill
    }
    fun setFoodLevel(newLevel: Int){
        foodFill = newLevel
    }

}