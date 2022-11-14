package com.example.spaceshooter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.util.*

class Mob(context: Context) {
    var mob = arrayOfNulls<Bitmap>(3)
    var mobFrame = 0
    var mobX = 0
    var mobY = 0
    var mobVelocity = 0
    var random: Random

    init {
        mob[0] = BitmapFactory.decodeResource(context.resources, R.drawable.mob0)
        mob[1] = BitmapFactory.decodeResource(context.resources, R.drawable.mob1)
        mob[2] = BitmapFactory.decodeResource(context.resources, R.drawable.mob2)
        mob[0] = mob[0]?.let {
            Bitmap.createScaledBitmap(it, 300, 300, true)
        }
        mob[1] = mob[1]?.let {
            Bitmap.createScaledBitmap(it, 300, 300, true)
        }
        mob[2] = mob[2]?.let {
            Bitmap.createScaledBitmap(it, 300, 300, true)
        }
        random = Random()
        resetPosition()
    }

    fun getMob(mobFrame: Int): Bitmap? {
        return mob[mobFrame]
    }

    val mobWidth: Int
        get() = mob[0]!!.width
    val mobHeight: Int
        get() = mob[0]!!.height

    fun resetPosition() {
        mobX = random.nextInt(GameView.dWidth - mobWidth)
        mobY = -200 + random.nextInt(600) * -1
        mobVelocity = 35 + random.nextInt(16)
    }
}