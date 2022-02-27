package com.example.snapking

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.snapking.databinding.ActivityLobbyBinding
import com.example.snapking.modelo.WrapperSala
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recicle
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        var wraperSala=Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
        Log.d("-------------------activity lobby",wraperSala.toString())


    }
}