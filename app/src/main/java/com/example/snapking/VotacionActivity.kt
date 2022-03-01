package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.example.snapking.Adapters.AmigoAdapterOnline
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.BaseDatos.IGetJugadoresFromSala
import com.example.snapking.BaseDatos.IGetUsersFromSala
import com.example.snapking.Firebase.User
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.databinding.ActivityVotacionBinding
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.Usuario
import com.example.snapking.modelo.WrapperSala
import com.example.snapking.modelo.WrapperUsuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
private lateinit var binding:ViewBinding
class VotacionActivity : AppCompatActivity() {
    private lateinit var wraperSala:WrapperSala
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVotacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cogerWrapperSala()





    }
    private fun cargarUsuarios()
    {
        /*Log.d("---------- USER ----------","Usuario AmigosActivity" + User.getInstancia()!!.user.toString())
        var adapter =
            BaseDatos.getInstance()!!.getListWrapperUsuariosFromListIds(User.getInstancia()!!.user.amigos)
                ?.let { it1 -> AmigoAdapterOnline(it1) }

        rvAmigos.adapter = adapter*/
        var usuarios=ArrayList<Usuario>()


        }




    private fun cogerWrapperSala(){
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala= Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
    }



}
