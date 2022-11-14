package com.example.spaceshooter

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {
    lateinit var tvPoints: TextView
    lateinit var tvHighest: TextView
    lateinit var sharedPreferences: SharedPreferences
    lateinit var ivNewHighest: ImageView
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        tvPoints = findViewById(R.id.tvPoints)
        tvHighest = findViewById(R.id.tvHighest)
        ivNewHighest = findViewById(R.id.ivNewHighest)
        val points = intent.extras!!.getInt("points")
        tvPoints.text = "" + points
        sharedPreferences = getSharedPreferences("my_pref", 0)
        val highest = sharedPreferences.getInt("highest", 0)
        if (points > highest) {
            ivNewHighest.visibility = View.VISIBLE
            val editor = sharedPreferences.edit()
            editor.putInt("highest", highest)
            editor.commit()
        }
        tvHighest.text = "" + highest
    }

    fun restart(view: View?) {
        val intent = Intent(this@GameOver, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(view: View?) {
        finish()
    }
}