package com.example.spaceshooter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.os.postDelayed
import java.util.*
import kotlin.collections.ArrayList

class GameView(context: Context) : View(
    context
) {
    var background: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.background)
    var ground: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ground)
    var player: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.player)
    var rectBackground: Rect
    var rectGround: Rect
    val UPDATE_MILLIS: Long = 30
    var runnable: Runnable
    var textPaint = Paint()
    var healthPaint = Paint()
    var TEXT_SIZE = 120f
    var points = 0
    var life = 3
    var random: Random
    var playerX: Float
    var playerY: Float
    var oldX = 0f
    var oldPlayerX = 0f
    var mobs: ArrayList<Mob>
    var explosions: ArrayList<Explosion>
    var missiles: ArrayList<Missile>

//    var p = Paint()
    var scrw = 0
    var scrh = 0
//    var missileCount = 99
//    var missileNum = intArrayOf(100)
//    var mX = intArrayOf(100)
//    var mY = intArrayOf(100)
//    var mD = intArrayOf(100)
//    var MD = 3

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.scrw = w
        this.scrh = h
    }

    init {
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x //체력바
        dHeight = size.y //체력바
        rectBackground = Rect(0, 0, dWidth, dHeight)
        rectGround = Rect(0, dHeight - ground.height, dWidth, dHeight)
        runnable = Runnable { invalidate() }
        textPaint.color = Color.rgb(255, 165, 0)
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT
//        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.font));
        healthPaint.color = Color.GREEN
        random = Random()
        playerX = (dWidth / 2 - player.width / 2).toFloat()
        playerY = (dHeight - ground.height - player.height).toFloat()
        mobs = ArrayList()
        explosions = ArrayList()
        missiles = ArrayList()
        for (i in 0..2) {
            val mob = Mob(context)
            mobs.add(mob)
        }
        for(i in 0..9) {
            val missile = Missile(context)
            missiles.add(missile)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(background, null, rectBackground, null)
        canvas.drawBitmap(ground, null, rectGround, null)
//        player = Bitmap.createScaledBitmap(player, 100, 100, true)
        canvas.drawBitmap(player, playerX, playerY+100, null)


//        var missile = arrayOfNulls<Bitmap>(100)
//        for(i in missile.indices) {
//            missile[i] = BitmapFactory.decodeResource(resources, R.drawable.missile)
//            missile[i] = missile[i]?.let {
//                Bitmap.createScaledBitmap(it, scrw/16, scrw, true)
//            }
//            if(missileNum[i] == 1) {
//                missile[i]?.let {
//                    canvas.drawBitmap(it, mX[i].toFloat(), mY[i].toFloat(), null)
//                }
//                if(mD[i] == 3) {
//                    mY[i] -= scrh/32
//                }
//            }
//            if(mY[i] > scrh - scrw/16 || mY[i] < 0) {
//                missileNum[i] = 0
//            }
//        }

        for (i in mobs.indices) { // ground 접촉시 처리
            mobs[i].getMob(mobs[i].mobFrame)?.let {
                canvas.drawBitmap(
                    it,
                    mobs[i].mobX.toFloat(),
                    mobs[i].mobY.toFloat(),
                    null
                )
            }
            mobs[i].mobFrame++
            if (mobs[i].mobFrame > 2) {
                mobs[i].mobFrame = 0
            }
            mobs[i].mobY += mobs[i].mobVelocity
            if (mobs[i].mobY + mobs[i].mobHeight >= dHeight - ground.height + 150) {
                points += 10
//                val explosion = Explosion(context)
//                explosion.explosionX = mobs[i].mobX
//                explosion.explosionY = mobs[i].mobY
//                explosions.add(explosion)
                mobs[i].resetPosition()
            }
        }

        for(i in missiles.indices) {
            missiles[i].getMissile(missiles[i].missileFrame)?.let {
                canvas.drawBitmap(
                    it,
                    missiles[i].missileX.toFloat(),
                    missiles[i].missileY.toFloat(),
                    null
                )
            }
            missiles[i].missileFrame++
            if(missiles[i].missileFrame > 9) {
                missiles[i].missileFrame = 0
            }
            missiles[i].missileY -= missiles[i].missileVelocity
            if (missiles[i].missileY + missiles[i].missileHeight <= 0) {
//                val explosion = Explosion(context)
//                explosion.explosionX = missiles[i].missileX
//                explosion.explosionY = missiles[i].missileY
//                explosions.add(explosion)
                missiles[i].resetMPosition()
            }
        }

        for (i in mobs.indices - 1) { //player 접촉시 처리
            if (mobs[i].mobX + mobs[i].mobWidth >= playerX
                && mobs[i].mobX <= playerX + player.width
                && mobs[i].mobY + mobs[i].mobWidth >= playerY
                && mobs[i].mobY + mobs[i].mobWidth <= playerY + player.height) {
//                life--
                val explosion = Explosion(context)
                explosion.explosionX = mobs[i].mobX
                explosion.explosionY = mobs[i].mobY
                explosions.add(explosion)
                mobs[i].resetPosition()
                if (life == 0) {
                    val intent = Intent(context, GameOver::class.java)
                    intent.putExtra("points", points)
                    context.startActivity(intent)
                    (context as Activity).finish()
                }
            }
        }

        for (i in explosions.indices - 1) { //폭발 처리
            explosions[i].getExplosion(explosions[i].explosionFrame)?.let {
                canvas.drawBitmap(
                    it,
                    explosions[i].explosionX.toFloat(),
                    explosions[i].explosionY.toFloat(),
                    null
                )
            }
            explosions[i].explosionFrame++
            if (explosions[i].explosionFrame > 3) {
                explosions.removeAt(i)
            }
        }

        if (life == 2) {
            healthPaint.color = Color.YELLOW
        } else if (life == 1) {
            healthPaint.color = Color.RED
        }

        canvas.drawRect(
            (dWidth - 200).toFloat(),
            30f,
            (dWidth - 200 + 60 * life).toFloat(),
            80f,
            healthPaint
        )
        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
        handler.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        if (touchY >= playerY) {
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldPlayerX = playerX
            }
            if (action == MotionEvent.ACTION_MOVE) {
                val shift = oldX - touchX
                val newPlayerX = oldPlayerX - shift
                playerX = if (newPlayerX <= 0) {
                    0f
                } else if (newPlayerX >= dWidth - player.width) {
                    (dWidth - player.width).toFloat()
                } else newPlayerX
                for(i in 0..9) {
                    missiles[i].missileX = playerX.toInt() + 180
                }
            }
        }
        return true
    }

    companion object {
        @JvmField
        var dWidth: Int = 0
        var dHeight: Int = 0
    }
}