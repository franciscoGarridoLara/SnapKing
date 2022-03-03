package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.BaseDatos.IGetJugadorGanador
import com.example.snapking.BaseDatos.IGetJugadoresFromSala
import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.modelo.Jugador
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

        BaseDatos.getInstance()?.getGanadorFromSala(wraperSala.id,object:IGetJugadorGanador{
            override fun oncallBack(ganador: WrapperUsuarioPartida) {

            }

        })




    }

    override fun onBackPressed() {

        startActivity(Intent(this,PrincipalActivity::class.java))
        finish()

        //super.onBackPressed() // optional depending on your needs
    }


    private fun cogerWrapperSala() {
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala = Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"), type)
    }
}