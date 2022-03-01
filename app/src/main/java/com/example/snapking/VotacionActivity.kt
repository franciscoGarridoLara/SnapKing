package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.snapking.Adapters.AmigoAdapterOnline
import com.example.snapking.Adapters.UsuarioAdapterVotacion
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.BaseDatos.IGetJugadoresFromSala
import com.example.snapking.BaseDatos.IGetUsersFromSala
import com.example.snapking.BaseDatos.IusuariosPuntos
import com.example.snapking.Firebase.User
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.databinding.ActivityVotacionBinding
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperSala
import com.example.snapking.modelo.WrapperUsuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class VotacionActivity : AppCompatActivity() {
    private  var binding:ViewBinding?=null

    private lateinit var wraperSala:WrapperSala
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVotacionBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        cogerWrapperSala()
        cargarUsuarios()






    }
    private fun cargarUsuarios()
    {

        BaseDatos.getInstance()?.getWrapperusuariosPuntosFromSala(wraperSala.id,object:IusuariosPuntos{
            override fun OncallBack(lista: List<WrapperUsuarioPartida>) {


                val rvContacts = findViewById<View>(R.id.recicleUsu)  as RecyclerView
                // Initialize contacts

                val adapter = UsuarioAdapterVotacion(lista)
                // Attach the adapter to the recyclerview to populate items
                rvContacts.adapter = adapter



            }

        })


        }




    private fun cogerWrapperSala(){
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala= Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
    }



}
