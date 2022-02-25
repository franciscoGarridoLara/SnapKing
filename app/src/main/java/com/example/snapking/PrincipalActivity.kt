package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.Firebase.User
import com.example.snapking.databinding.ActivityPrincipalBinding
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.Sala
import com.example.snapking.modelo.WrapperSala

class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var btnAjustes:ImageButton
    private lateinit var btnPerfil:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        //incializarBotones()


        binding.btnAjustes.setOnClickListener()
        {
            User.getInstancia()!!.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        if(User.getInstancia()!= null){
            User.getInstancia()!!.printName()
            User.getInstancia()!!.printToken()
        }else{
            Log.d(User.TAG,"no hay usuario ")
        }

        binding.btnBattle.setOnClickListener {
            var wraperSalaGlobal:WrapperSala?
            wraperSalaGlobal= machmaking()

            if(wraperSalaGlobal!=null){
                Toast.makeText(this, "wrapper sala "+wraperSalaGlobal.id.toString(), Toast.LENGTH_SHORT).show()
            }

        }
        binding.btnPerfil.setOnClickListener()
        {
            startActivity(Intent(this,AmigosActivity::class.java))
            //finish()
        }

        //BaseDatos.getInstance()!!.escribir()
        BaseDatos.getInstance()!!.escribirUsuario(User.getInstancia()!!.wrapper)

    }
    private fun machmaking(): WrapperSala? {
        var id= User.getInstancia()?.printToken() as String
        var jugador=Jugador(id,false,0)
        var wraperSalaFinal:WrapperSala?
        wraperSalaFinal=null
        var salas=BaseDatos.getInstance()!!.leerSala()
        Toast.makeText(this, "salas obtenidas", Toast.LENGTH_SHORT).show()
        var bucle=true
        var i=0
        while (bucle&&i<salas.size){
            var wrappersala=salas[i]
            if (wrappersala.sala.capacidad<wrappersala.sala.jugadores.size){


                bucle=false

                BaseDatos.getInstance()?.meterJugadorSala(wrappersala.id,jugador)
                wraperSalaFinal=wrappersala
            }
            i++

        }
        Toast.makeText(this, "buckke vale "+ bucle.toString(), Toast.LENGTH_SHORT).show()
        if(bucle){
            var jugadores=ArrayList<Jugador>()
            jugadores.add(jugador)

            var sala=Sala(
                "sala publica",
                8,
                User.getInstancia()?.printToken() as String,
                true,null,5,jugadores
            )
            var clave=BaseDatos.getInstance()?.escribirSala(sala)
            if (clave!=null){
                wraperSalaFinal=WrapperSala(clave,sala)
            }



        }
        return wraperSalaFinal



    }



    /*private fun incializarBotones() {
        btnAjustes = findViewById(R.id.btnAjustes)
        btnPerfil = findViewById(R.id.btnPerfil)
    }*/


}