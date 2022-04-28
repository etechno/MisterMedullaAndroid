package com.example.mistermedulla

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread

    private var heartOutline: Heart? = null
    private var ekgBox: EKG? = null
    private var breathMeter: Oxygen? = null
    private var foodLevel: Stomach? = null
    private var bloodSugar: BloodSugar? = null
    private val blipList = mutableListOf<Blip>()
    private var titleImage: Bitmap

    private var touched = false
    private var touched_x = 0
    private var touched_y = 0

    private var heartBeated = false
    private var breathed = false
    private var ate = false
    private var insulinRelease = false
    private var gameState = "menu"
    private var performGameOver = false
    private var timeSinceLastBeat = 0
    private var timeSinceLastBreath = 0
    private var timeSinceScreenChange = 0
    private var score = 0
    private val tutorialText = listOf(
        "Welcome to Mister Medulla, tap to continue the tutorial.",
        "You will be controlling a virtual body.",
        "In particular, it's autonomic nervous system.",
        "The most important system you will control is the heart.",
        "The heart gauge here is a display of your health.",
        // 5
        "If the heart gets empty, you lose.",
        "To keep the heart from getting empty, it must beat.",
        "To display heartbeats, we have a handy EKG meter.",
        "Whenever you tap the heart or EKG, the heart will beat.",
        "Each heartbeat appears as a blip on the EKG.",
        // 10
        "Beat too fast, and you will lose health.",
        "Beat too slow, and you will lose health.",
        "Beating at the right time will increase your health.",
        "Almost there . . .",
        "Time your beats so that the previous beat reaches here.",
        // 15
        "Much later and your health will start to drain",
        "The next system will be your lungs.",
        "Your lungs are represented by the bar on the right.",
        "The bubbles show the air currently in your lungs,",
        "While the red is the oxygen in your blood.",
        // 20
        "Oxygen in your blood decreases over time.",
        "Air is turned into oxygen whenever you beat your heart.",
        "Tapping the meter on the right causes you to breathe.",
        "When you breathe, air in your lungs increases.",
        "However, for a short while after breathing,",
        // 25
        "Beating your heart won't change air into oxygen.",
        "If your oxygen level goes too low,",
        "You will start losing health.",
        "The next system you will need to manage is the stomach.",
        "The turkey leg in the top left is your stomach.",
        // 30
        "This is a full stomach.",
        "This is an empty stomach.",
        "Tapping the stomach increases how much food is inside.",
        "Food naturally drains from the stomach.",
        "Having a full or empty stomach won't kill you,",
        // 35
        "But it does impact our next system, blood sugar.",
        "The leaf and chocolate bar represent your blood sugar.",
        "The goal for this system is to balance it.",
        "Half leaf and half chocolate represent a balanced system.",
        "Blood sugar changes over time, depending on stomach's level.",
        // 40
        "Tapping on the blood sugar meter releases insulin.",
        "Releasing insulin causes a sharp drop in blood sugar",
        "Having too high blood sugar damages the body.",
        "While having too low blood sugar stops the heart from beating.",
        "Thats it for this tutorial, good luck staying alive!"
        )
    private var tutorialIndex = 0

    init {

        // add callback
        holder.addCallback(this)

        // instantiate the game thread
        thread = GameThread(holder, this)


        titleImage = Bitmap.createScaledBitmap(
            BitmapFactory.decodeResource(resources, R.drawable.title), 800, 350, false
        )
    }


    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }
    fun resetSystems(){
        heartOutline = Heart(BitmapFactory.decodeResource(resources, R.drawable.heartred),BitmapFactory.decodeResource(resources, R.drawable.heartline),BitmapFactory.decodeResource(resources, R.drawable.heartflare))
        ekgBox = EKG(BitmapFactory.decodeResource(resources, R.drawable.ekgoverlay),BitmapFactory.decodeResource(resources, R.drawable.ekgline))
        breathMeter = Oxygen(BitmapFactory.decodeResource(resources, R.drawable.breathbubbles),BitmapFactory.decodeResource(resources, R.drawable.breathbubblesbackground),BitmapFactory.decodeResource(resources, R.drawable.breathbubblesfill),BitmapFactory.decodeResource(resources, R.drawable.breathcanister),BitmapFactory.decodeResource(resources, R.drawable.breathoxygen))
        foodLevel = Stomach(BitmapFactory.decodeResource(resources, R.drawable.food0),BitmapFactory.decodeResource(resources, R.drawable.food1),BitmapFactory.decodeResource(resources, R.drawable.food2),BitmapFactory.decodeResource(resources, R.drawable.food3),BitmapFactory.decodeResource(resources, R.drawable.food4),BitmapFactory.decodeResource(resources, R.drawable.food5))
        bloodSugar = BloodSugar(BitmapFactory.decodeResource(resources, R.drawable.leaf),BitmapFactory.decodeResource(resources, R.drawable.chocolate))
    }
    override fun surfaceCreated(holder: SurfaceHolder) {
        resetSystems()
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, p1: Int, p2: Int, p3: Int) {

    }
    fun resetGame(){
        heartBeated = false
        breathed = false
        ate = false
        insulinRelease = false
        gameState = "menu"
        performGameOver = false
        timeSinceLastBeat = 0
        timeSinceLastBreath = 0
        blipList.clear()
        tutorialIndex = 0
        timeSinceScreenChange = 0
        score = 0
        resetSystems()
    }
    fun update() {
        if (gameState == "playing") {
            var healthMod = 0
            if (breathed == true) {
                breathMeter!!.update(30, heartBeated, timeSinceLastBreath)
                breathed = false
            } else {
                breathMeter!!.update(0, heartBeated, timeSinceLastBreath)
            }
            if (breathMeter!!.oxygenTooLow()) {
                healthMod -= 2
            }
            foodLevel!!.update(ate)
            bloodSugar!!.update(foodLevel!!.getFoodLevel(), insulinRelease)
            if (bloodSugar!!.getSugarHigh()){
                healthMod -= 2
            }
            heartBeated = false
            ate = false
            insulinRelease = false
            if (timeSinceLastBeat > 7) {
                healthMod -= 3
            }
            performGameOver = heartOutline!!.update(healthMod)
            if (performGameOver) {
                gameState = "gameover"
                timeSinceScreenChange = 0
            }
            score += 1
            timeSinceLastBeat += 1
            timeSinceLastBreath += 1
            for (blip in blipList) {
                blip.update()
            }
            for (i in blipList.indices.reversed()) {
                if (blipList[i].getLivingTicks() > 7) {
                    blipList.removeAt(i)
                }
            }
        }
        else {
            timeSinceScreenChange += 1
        }
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        if (gameState == "playing") {
            ekgBox!!.drawLine(canvas)
            for (i in blipList.indices) {
                blipList[i].draw(canvas)
            }
            ekgBox!!.drawFrame(canvas)
            heartOutline!!.draw(canvas)
            breathMeter!!.draw(canvas)
            foodLevel!!.draw(canvas)
            bloodSugar!!.draw(canvas)
        }
        else if (gameState == "gameover"){
            val paint = Paint()
            paint.setColor(Color.WHITE)
            paint.setStyle(Paint.Style.FILL)
            canvas.drawPaint(paint)

            paint.setColor(Color.BLACK)
            paint.setTextSize(100.toFloat())
            canvas.drawText("Game Over", 900.toFloat(), 450.toFloat(), paint)
            paint.setTextSize(75.toFloat())
            canvas.drawText("You scored " + score.toString() + " points", 800.toFloat(), 575.toFloat(), paint)
            canvas.drawText("Tap to return to the menu", 750.toFloat(), 700.toFloat(), paint)
        }
        else if (gameState == "menu") {
            val paint = Paint()
            paint.setColor(Color.WHITE)
            paint.setStyle(Paint.Style.FILL)
            canvas.drawPaint(paint)

            canvas.drawBitmap(titleImage, 700.toFloat(), 50.toFloat(), null)

            paint.setColor(Color.BLACK)
            paint.setTextSize(100.toFloat())
            canvas.drawText("Start Tutorial", 300.toFloat(), 850.toFloat(), paint)
            canvas.drawText("Start Game", 1400.toFloat(), 850.toFloat(), paint)
        }
        else if (gameState == "tutorial"){
            val paint = Paint()
            paint.setColor(Color.WHITE)
            paint.setTextSize(75.toFloat())
            canvas.drawText(tutorialText[tutorialIndex], 75.toFloat(), 100.toFloat(), paint)
            if (tutorialIndex >= 3) {
                heartOutline!!.draw(canvas)
            }
            if (tutorialIndex >= 7) {
                ekgBox!!.drawLine(canvas)
            }
            if (tutorialIndex >= 9 && tutorialIndex <= 16) {
                blipList[0].draw(canvas)
            }
            if (tutorialIndex >= 7) {
                ekgBox!!.drawFrame(canvas)
            }
            if (tutorialIndex >= 16) {
                breathMeter!!.draw(canvas)
            }
            if (tutorialIndex >= 28) {
                foodLevel!!.draw(canvas)
            }
            if (tutorialIndex >= 35) {
                bloodSugar!!.draw(canvas)
            }


        }
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {

        touched_x = event.x.toInt()
        touched_y = event.y.toInt()

        val action = event.action

        if (action == MotionEvent.ACTION_DOWN) {
            if (gameState == "playing") {
                // heart
                if (touched == false && heartBeated == false && touched_x < 1800 && touched_y > 400 && !(bloodSugar!!.getSugarHigh())) {
                    blipList.add(Blip(BitmapFactory.decodeResource(resources, R.drawable.ekgbeat)))
                    heartBeated = true
                    if (timeSinceLastBeat < 4) {
                        performGameOver = heartOutline!!.update(-3)
                    } else if (timeSinceLastBeat <= 7) {
                        heartOutline!!.update(1)
                    }
                    timeSinceLastBeat = 0
                }
                // breath
                else if (touched == false && breathed == false && touched_x > 1800) {
                    breathed = true
                    timeSinceLastBreath = 0
                }
                // blood sugar
                else if (touched == false && touched_y > 100 && touched_y < 400 && touched_x > 500 && touched_x < 1150) {
                    insulinRelease = true
                }
                // food
                else if (touched == false && touched_y > 100 && touched_y < 350 && touched_x > 100 && touched_x < 400) {
                    ate = true
                }
                touched = true
            }
            else if (gameState ==  "gameover"){
                if (timeSinceScreenChange > 4) {
                    resetGame()
                }
            }
            else if (gameState == "tutorial") {
                tutorialIndex += 1

                if (tutorialIndex == 5) {
                    heartOutline?.update(-1000)
                }
                if (tutorialIndex == 7) {
                    heartOutline?.update(1000)
                }
                if (tutorialIndex == 9) {
                    breathMeter!!.update(0, true, 25)
                    breathMeter!!.update(0, true, 25)
                    blipList.add(Blip(BitmapFactory.decodeResource(resources, R.drawable.ekgbeat)))
                }
                if (tutorialIndex > 9 && tutorialIndex <= 16) {
                    blipList[0].update()
                }
                if (tutorialIndex == 17) {
                    blipList.clear()
                }
                if (tutorialIndex >= 20 && tutorialIndex <= 27) {
                    breathMeter!!.update(0, false, 25)
                }
                if (tutorialIndex == 23) {
                    breathMeter!!.update(60, false, 25)
                }
                if (tutorialIndex == 31) {
                   foodLevel!!.setFoodLevel(1)
                }
                if (tutorialIndex == 32) {
                    foodLevel!!.setFoodLevel(750)
                }
                if (tutorialIndex == 40) {
                    bloodSugar!!.update(150,true)
                }
                if (tutorialIndex >= tutorialText.size) {
                    resetGame()
                }
            }
        }
        else if (action == MotionEvent.ACTION_UP) {
            touched = false
            if (gameState == "menu"){
                if (touched_y > 750 && touched_y < 900){
                    if (touched_x < 950 && touched_x > 250) {
                        gameState = "tutorial"
                    }
                    else if (touched_x > 1375 && touched_x < 1925){
                        gameState = "playing"
                    }
                }

            }
        }
        return true
    }
}