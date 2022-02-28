package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.BaseDatos.ISalasLectura
import com.example.snapking.Firebase.User
import com.example.snapking.databinding.ActivityPrincipalBinding
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.Sala
import com.example.snapking.modelo.WrapperSala
import com.google.gson.Gson
import com.squareup.picasso.Picasso


class PrincipalActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrincipalBinding

    private lateinit var btnAjustes:ImageButton
    private lateinit var btnPerfil:ImageButton
    private lateinit var user:User
    private lateinit var db:BaseDatos
    var haysalas=false
    var WrapperSalaGlobal:WrapperSala?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityPrincipalBinding.inflate(layoutInflater)

        setContentView(binding.root)

        db = BaseDatos.getInstance()!!
        user = User.getInstancia()!!

        iniciarInterfaz()
        setListeners()

    }



    private fun iniciarInterfaz() {
        Picasso.get()
            .load(User.getInstancia()!!.getAvatar())
            .into(binding.btnPerfil)
    }

    private fun setListeners() {

        //-------------BORRAR----------------
        binding.tematica.setOnClickListener(){
            startActivity(Intent(this,TematicaActivity::class.java))
            finish()
        }
        binding.btnParty.setOnClickListener{




        }


        binding.btnAjustes.setOnClickListener()
        {
            User.getInstancia()!!.signOut()
            startActivity(Intent(this,MainActivity::class.java))
            //finish()
        }
        if(User.getInstancia()!= null){
            User.getInstancia()!!.printName()
            User.getInstancia()!!.printToken()
        }else{
            Log.d(User.TAG,"no hay usuario ")
        }

        binding.btnBattle.setOnClickListener {

            machmaking()


        }

        binding.btnPerfil.setOnClickListener()
        {
            startActivity(Intent(this,AmigosActivity::class.java))
            //finish()

        }
    }

    private fun crearSala() {
        var iduser=User.getInstancia()?.printToken().toString()
        var jugador=Jugador(iduser,false,0)
        var listaJugadores=ArrayList<Jugador>()
        listaJugadores.add(jugador)

        var sala=Sala("sala publica",8,iduser,true,null,5,listaJugadores)
        var id=BaseDatos.getInstance()?.escribirSala(sala)
    }

    private fun machmaking() {
        var wraperSala:WrapperSala?
        wraperSala=null
        var id=User.getInstancia()?.printToken().toString()

        BaseDatos.getInstance()?.leerSala(object: ISalasLectura{
            override fun OncallBack(lista: List<WrapperSala>) {
                haysalas=true
                Log.d("-----------principal","entro principal")
                    var i=0
                    var bucle=true
                    var wraperFinal:WrapperSala?
                    wraperFinal=null
                    while(bucle&&i<lista.size) {
                        var wraperSalaIn=lista[i]
                        if (wraperSalaIn.sala.capacidad > wraperSalaIn.sala.jugadores.size) {
                            Log.d("-----------pr","entro en el if")

                            var jugador=Jugador(User.getInstancia()?.printToken().toString(),false,0)
                            Log.d("-----------pr","entro en el escribiendo")
                            BaseDatos.getInstance()?.meterJugadorSala(wraperSalaIn.id,jugador)
                            bucle=false
                            WrapperSalaGlobal=wraperSalaIn
                        }
                        i++
                    }
                Log.d("---------bb", bucle.toString())
                    if(bucle){
                        var jugador=Jugador(id,false,0)
                        var listaJugadores=ArrayList<Jugador>()
                        listaJugadores.add(jugador)

                        var sala=Sala("sala publica",8,id,true,null,5,listaJugadores)
                        var idsala=BaseDatos.getInstance()?.escribirSala(sala)
                        WrapperSalaGlobal=WrapperSala(idsala.toString(),sala)

                    }


                var intent=Intent(applicationContext,LobbyActivity::class.java)
                Log.d("----------",WrapperSalaGlobal.toString())
                intent.putExtra("wrapersala",Gson().toJson(WrapperSalaGlobal) )
                startActivity(intent)

            }


        })



    }




}