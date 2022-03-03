package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.snapking.modelo.WrapperSala
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class GanadorActivity : AppCompatActivity() {
    private lateinit var  wraperSala:WrapperSala
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ganador)
        cogerWrapperSala()




    }


    private fun cogerWrapperSala() {
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala = Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"), type)
    }
}