package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.snapking.Adapters.AmigoAdapterOnline
import com.example.snapking.Adapters.UsuarioAdapterVotacion
import com.example.snapking.BaseDatos.*
import com.example.snapking.Firebase.User
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.databinding.ActivityVotacionBinding
import com.example.snapking.modelo.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class VotacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVotacionBinding
    private lateinit var wraperSala: WrapperSala
    private lateinit var postListenerJugadores: ValueEventListener
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var fragment : VotacionFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotacionBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        cogerWrapperSala()
        cargarUsuarios()


        setListeners()
        iniciarVotacion()


    }
    //Esto se puede retocar a su gusto.
    private fun iniciarVotacion() {
        BaseDatos.getInstance()!!.getFotosFromRonda(wraperSala!!.id, object : IGetFotos{
            override fun OnCallBack(fotos: ArrayList<Foto>) {
                //Eliminamos la foto del propio usuario por que no se tiene que votar a el mismo.
                fotos.removeIf { it -> it.id.equals(User.getInstancia()!!.printToken().toString()) }

                for(foto in fotos){
                    switchFragment(User.getInstancia()!!.getName(),foto.url)
                    votar()
                }
            }

        })

    }



    private fun votar(){
        binding.btnVotar.visibility = View.VISIBLE

    }

    private fun switchFragment(nickname : String, url : String){

        fragment = VotacionFragment(nickname, url)
        fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameVotacion, fragment)
        //fragmentTransaction.addToBackStack(null)
        try {
            fragmentTransaction.commit()
        } catch (e: Exception) {
        }

    }

    private fun setListeners() {
        binding.btnVotar.visibility = View.INVISIBLE
        binding.btnVotar.setOnClickListener {
            Log.d("ACTIVITY VOTACION", "VALORACION: " + binding.sliderPuntuacion.rating)

            binding.btnVotar.visibility = View.INVISIBLE

        }
    }

    override fun onBackPressed() {


        super.onBackPressed()
    }

    private fun cargarUsuarios() {
        postListenerJugadores = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("VOTACION ACTIVITY", "CAMBIO EN LOS JUGADORES")
                BaseDatos.getInstance()
                    ?.getWrapperusuariosPuntosFromSala(wraperSala.id, object : IusuariosPuntos {
                        override fun OncallBack(lista: List<WrapperUsuarioPartida>) {


                            val rvContacts = findViewById<View>(R.id.recicleUsu) as RecyclerView
                            // Initialize contacts

                            val adapter = UsuarioAdapterVotacion(lista)
                            // Attach the adapter to the recyclerview to populate items
                            rvContacts.adapter = adapter


                        }

                    })
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("jugadores").addValueEventListener(postListenerJugadores!!)
    }

    private fun cogerWrapperSala() {
        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
        wraperSala = Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"), type)
    }


}


