package com.example.snapking

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.snapking.Adapters.UsuarioAdapter
import com.example.snapking.BaseDatos.*
import com.example.snapking.Firebase.User
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.databinding.ActivityLobbyBinding
import com.example.snapking.modelo.Etapa
import com.example.snapking.modelo.Jugador
import com.example.snapking.modelo.WrapperSala
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class LobbyActivity : AppCompatActivity() {
    var wraperSala:WrapperSala?=null
    var binding : ActivityLobbyBinding? = null
    var ready=true
    var chequearStart=true
    var inicio : Boolean = true
    var postListener: ValueEventListener? =null

    companion object {
        var activityActual:Activity?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLobbyBinding.inflate(layoutInflater)
        activityActual= this

        setContentView(binding!!.root)


        val intent = intent
        val type: Type = object : TypeToken<WrapperSala>() {}.type
         wraperSala=Gson().fromJson<WrapperSala>(intent.getStringExtra("wrapersala"),type)
        Log.d("ACTIVITY LOBBY",wraperSala.toString())


        setListeners()
        cargarUsuarios()


    }

   override fun onDestroy() {

       if (inicio) {
           if(wraperSala?.sala?.anfitrion.equals(User.getInstancia()?.printToken())){
               BaseDatos.getInstance()?.elminarSala(wraperSala!!.id )

            }else{
                BaseDatos.getInstance()?.elminarJugadorSala(wraperSala!!.id, User.getInstancia()!!.printToken())
            }


       }
       super.onDestroy()
   }
//paco
    private fun setListeners() {
        binding!!.btnReady.setOnClickListener(){
            Log.d("ACTIVITY LOBBY", "PULSANDO EL BOTON DE READY!")
            //escribir en la base de datos el ready

            BaseDatos.getInstance()!!.setUserReadySala(wraperSala!!.id, User.getInstancia()!!.printToken(),ready,object:
                IReady {
                override fun OnCallback() {
                    ready=!ready
                }

            })
        }
    }

    private fun cargarUsuarios() {
        postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (inicio) {
                    BaseDatos!!.getInstance()!!.getUsersFromSala(wraperSala!!.id,object : IGetUsersFromSala{
                        override fun OnCallBack(lista: ArrayList<WrapperUsuarioLobby>) {
                            Log.d("-----LOBBY ACTIVITY",lista.toString())
                            var adapter = lista.let { it -> UsuarioAdapter(it) }
                            binding!!.recicle.adapter = adapter

                            if(wraperSala!!.sala?.anfitrion.equals(User.getInstancia()!!.printToken())){
                                var votos=lista.count {
                                    it.estado==true
                                }
                                Log.d("------LOBBY ACITVITY", "Numero ready: " + votos.toString())
                                Log.d("--------LOBBY ACITVITY", "lista size: " + lista.size.toString())
                                if(votos==lista.size&&votos>1){
                                    inicio = false
                                    BaseDatos.getInstance()?.empezarPartida(wraperSala!!.id)
                                    BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListener!!)
                                    pasoDeActividad()
                                }
                            }else{
                                BaseDatos.getInstance()?.comprobarPartida(wraperSala!!.id,object:IComprobarStart{
                                    override fun OncallBack(ready: Boolean) {

                                        if (ready && inicio) {
                                            inicio = false

                                            Log.d("LOBBY ACTIVITY","CERRANDO ESCUCHADOR CARGAR USUARIOS.")
                                            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListener!!)
                                            pasoDeActividad()
                                        }

                                    }



                                })
                            }

                        }

                        private fun pasoDeActividad() {
                            var intent = Intent(applicationContext, TematicaActivity::class.java)
                            intent.putExtra("wrapersala", Gson().toJson(wraperSala))
                            startActivity(intent)
                            if (wraperSala!!.sala.anfitrion.equals(User.getInstancia()?.printToken())) {
                                BaseDatos.getInstance()!!.cambiarEstadoJugadorSala(wraperSala!!.id,User.getInstancia()!!.printToken(),Etapa.PARTIDA)
                            }
                            finish()
                        }
                    })

                    BaseDatos.getInstance()?.getJugadoresFromSala(wraperSala!!.id,object:
                        IGetJugadoresFromSala {
                        override fun OnCallback(lista: ArrayList<Jugador>) {
                            wraperSala!!.sala.jugadores=lista
                        }

                    })
                }else
                {
                    Log.d("ACTIVITY LOBBY", "REMOVIENDO EL LISTENER DE CARGAR DATOS.")
                    BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(this)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ACTIVITY LOBBY", "loadPost:onCancelled", databaseError.toException())

                BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(this)
            }
        }


        BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).addValueEventListener(postListener!!)
    }


    override fun onBackPressed() {
        // code here to show dialog
        Toast.makeText(applicationContext,
            android.R.string.yes, Toast.LENGTH_SHORT).show()
        chequearStart=false
        Log.d("LOBBY ACITVITY", "REMOVIENDO ESCUCHADOR DE SALA")
        BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListener!!)
        //llamar a base de datos para eliminar la sala.
        BaseDatos.getInstance()?.elminarJugadorSala(wraperSala!!.id, User.getInstancia()!!.printToken())

        startActivity(Intent(this,PrincipalActivity::class.java))
        finish()

        //super.onBackPressed() // optional depending on your needs
    }



  /*  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event != null) {
            if(keyCode==event.keyCode)
            {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Salir de la sala")
                builder.setMessage("¿Quieres salir de la sala?")
                //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

                builder.setPositiveButton(android.R.string.yes) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.yes, Toast.LENGTH_SHORT).show()
                    chequearStart=false
                    Log.d("LOBBY ACITVITY", "REMOVIENDO ESCUCHADOR DE SALA")
                    BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListener!!)
                    //llamar a base de datos para eliminar la sala.
                    BaseDatos.getInstance()?.elminarJugadorSala(wraperSala!!.id, User.getInstancia()!!.printToken())

                    startActivity(Intent(this,PrincipalActivity::class.java))
                    finish()

                }

                builder.setNegativeButton(android.R.string.no) { dialog, which ->
                    Toast.makeText(applicationContext,
                        android.R.string.no, Toast.LENGTH_SHORT).show()
                }

                builder.show()

            }

        }
        return super.onKeyDown(keyCode, event)
    }*/
}