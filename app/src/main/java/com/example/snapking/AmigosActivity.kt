package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView

class AmigosActivity : AppCompatActivity() {

    private lateinit var etBusqueda:EditText
    private lateinit var rvAmigos:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos)

        inicializarInterfaz()
    }

    private fun inicializarInterfaz() {
        rvAmigos = findViewById(R.id.rvAmigos)
        etBusqueda = findViewById(R.id.etBusqueda)
    }
}