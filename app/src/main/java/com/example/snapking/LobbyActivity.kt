package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.snapking.databinding.ActivityLobbyBinding

class LobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivityLobbyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recicle



    }
}