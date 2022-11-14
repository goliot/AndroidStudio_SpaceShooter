package com.example.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*
import android.view.WindowManager

class Missile(context: Context) {
    var missile = arrayOfNulls<Bitmap>(10)
    var missileFrame = 0
    var missileX = 0
    var missileY = 0
    var missileVelocity = 0
    var random: Random


    init {
        for(i in 0..9) {
            missile[i] = BitmapFactory.decodeResource(context.resources, R.drawable.missile)
            missile[i] = missile[i]?.let {
                Bitmap.createScaledBitmap(it, 50, 50, true)
            }
        }
        random = Random()
        resetMPosition()
    }

    fun getMissile(missileFrame: Int): Bitmap? {
        return missile[missileFrame]
    }

    val missileWidth: Int
        get() = missile[0]!!.width
    val missileHeight: Int
        get() = missile[0]!!.height

    fun resetMPosition() {
//        missileX = 500
//        missileY = (-200 + random.nextInt(600) * -1) * -1 + 1300
        missileY = 2000 - random.nextInt(100)
        missileVelocity = 50
    }
}