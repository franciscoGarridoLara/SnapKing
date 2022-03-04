package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.BaseDatos.IGetJugadorGanador
import com.example.snapking.BaseDatos.IGetJugadoresFromSala
import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.databinding.ActivityGanadorBinding
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.WrapperSala
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

class GanadorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGanadorBinding
    private lateinit var wraperSala: WrapperSala
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGanadorBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        cogerWrapperSala()
        setListeners()

        BaseDatos.getInstance()?.getGanadorFromSala(wraperSala.id, object : IGetJugadorGanador {
            override fun oncallBack(ganador: WrapperUsuarioPartida) {
                Log.d("GANADOR ACTIVITY",ganador.toString())
                binding.txtjugadorWiner.setText(ganador.Usuario.nickname)
                binding.txtEstrellas.setText(ganador.puntos.toString())
                Picasso.get()
                    .load(ganador.Usuario.avatar)
                    .into(binding.imgperfil)
                confeti()
            }

        })

    }

    private fun confeti() {

       var party= Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 800,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 1000, TimeUnit.MILLISECONDS).perSecond(1000),
            position = Position.Relative(0.5, 0.3)
        )
        binding.konfettiView.start(party)
    }

    private fun setListeners() {
        binding.btnLobby.setOnClickListener {
            startActivity(Intent(this, PrincipalActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {

        startActivity(Intent(this, PrincipalActivity::class.java))
        finish()

        super.onBackPressed() // optional depending on your needs
    }

    private fun cogerWrapperSala() {
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala = Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"), type)
    }
}