package com.example.snapking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.Adapters.UsuarioAdapterVotacion
import com.example.snapking.BaseDatos.*
import com.example.snapking.Firebase.User
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
    lateinit var postListenerTiempo: ValueEventListener
     var postListenerEtapa : ValueEventListener?=null
    lateinit var postListenerPasarDeEscena:ValueEventListener
    private lateinit var postListenerInvitadoPasarEscena:ValueEventListener
    private var listaFotos=ArrayList<Foto>()
    var nFoto=0
    private lateinit var fotoActual:Foto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVotacionBinding.inflate(layoutInflater)

        setContentView(binding!!.root)
        cogerWrapperSala()
        cargarUsuarios()
        comprobarTiempo()


        setListeners()





    }
    //Esto se puede retocar a su gusto.
    private fun iniciarVotacion() {

        BaseDatos.getInstance()!!.getFotosFromRonda(wraperSala!!.id, object : IGetFotos{
            override fun OnCallBack(fotos: ArrayList<Foto>) {
                //Eliminamos la foto del propio usuario por que no se tiene que votar a el mismo.
                fotos.removeIf { it -> it.id.equals(User.getInstancia()!!.printToken().toString()) }
                listaFotos=fotos
                if (fotos.size>0) {
                    TematicaActivity.votacion = true
                    fotoActual = fotos[0]
                    if (wraperSala.sala.anfitrion.equals(User.getInstancia()?.printToken())) {
                        BaseDatos.getInstance()?.cambiarEstapaSala(wraperSala.id,Etapa.VOTACION)
                    }


                    BaseDatos.getInstance()?.getUser(fotoActual.id, object : IGetUser {
                        override fun OnCallBack(user: Usuario) {
                            switchFragment(user.nickname, fotoActual.url)
                            votar()
                        }


                    })
                }




            }

        })

    }



    private fun votar(){
        binding.btnVotar.visibility = View.VISIBLE

    }

    private fun switchFragment(nickname : String, url : String){

        fragment = VotacionFragment(nickname, url)
        binding.btnVotar.visibility=View.VISIBLE
        fragmentTransaction= supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameVotacion, fragment)
        //fragmentTransaction.addToBackStack(null)
        try {
            fragmentTransaction.commit()
        } catch (e: Exception) {
        }

    }
    private fun pasarDeEscenaTematica(){
        var intent = Intent(applicationContext, TematicaActivity::class.java)
        intent.putExtra("wrapersala", Gson().toJson(wraperSala))
        cerrarEscuchadores()
        startActivity(intent)
        finish()
    }
    private fun pasarDeEscenaGanador(){
        var intent = Intent(applicationContext, GanadorActivity::class.java)
        intent.putExtra("wrapersala", Gson().toJson(wraperSala))
        cerrarEscuchadores()
        startActivity(intent)
        finish()
    }

    private fun cerrarEscuchadores() {
        try {
            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("jugadores").removeEventListener(postListenerPasarDeEscena!!)
        } catch (e: Exception) {
        }
        try {
            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListenerTiempo!!)
        } catch (e: Exception) {
        }
        try {
            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("etapa").removeEventListener(postListenerEtapa!!)
        } catch (e: Exception) {
        }

        try {
            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala.id).child("etapa").removeEventListener(postListenerInvitadoPasarEscena!!)
        } catch (e: Exception) {
        }

    }

    private fun setListeners() {
        binding.btnVotar.visibility = View.INVISIBLE
        binding.btnVotar.setOnClickListener {
            Log.d("ACTIVITY VOTACION", "VALORACION: " + binding.sliderPuntuacion.rating)
            BaseDatos.getInstance()!!.sumarPuntosJugadorSala(wraperSala.id,fotoActual.id, binding.sliderPuntuacion.rating)
            binding.btnVotar.visibility = View.INVISIBLE
            nFoto++

            if (nFoto<listaFotos.size) {
                 fotoActual=listaFotos[nFoto]


                BaseDatos.getInstance()!!.getUser(fotoActual.id,object:IGetUser{
                    override fun OnCallBack(user: Usuario) {
                        switchFragment(user.nickname,fotoActual.url)
                    }

                })

            }else{
                BaseDatos.getInstance()!!.cambiarEstadoJugadorSala(wraperSala.id,User.getInstancia()!!.printToken(),Etapa.VOTADO)


                    postListenerPasarDeEscena = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (wraperSala.sala.anfitrion.equals(
                                    User.getInstancia()!!.printToken()
                                )
                            ) {

                                BaseDatos.getInstance()?.getJugadoresFromSala(wraperSala.id,
                                    object : IGetJugadoresFromSala {
                                        override fun OnCallback(lista: ArrayList<Jugador>) {
                                            var votados =
                                                lista.filter { it.etapa.equals(Etapa.VOTADO) }
                                                    .count()

                                            if (votados == lista.size) {

                                                BaseDatos.getInstance()
                                                    ?.getnronda(wraperSala.id,
                                                        object : InRonda {
                                                            override fun OncallBack(nRonda: Int) {

                                                                    if (nRonda < wraperSala.sala.rondas_totales.toInt()) {
                                                                        BaseDatos.getInstance()
                                                                            ?.cambiarEstapaSala(
                                                                                wraperSala.id,
                                                                                Etapa.PARTIDA
                                                                            )
                                                                        pasarDeEscenaTematica()
                                                                    } else {
                                                                        BaseDatos.getInstance()
                                                                            ?.cambiarEstapaSala(
                                                                                wraperSala.id,
                                                                                Etapa.GANADOR
                                                                            )
                                                                        pasarDeEscenaGanador()
                                                                    }


                                                            }

                                                        })

                                            }
                                        }


                                    })

                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            // Getting Post failed, log a message
                            //Mandar a la principal activity.
                            Log.d("VOTACION ACTIVITY","ON CANCELLED CERRANDO ESCUCHADOR SEGUNDOS")
                            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("jugadores").removeEventListener(postListenerPasarDeEscena!!)

                            Log.w("VOTACION LOBBY", "loadPost:onCancelled", databaseError.toException())
                            var intent = Intent(this@VotacionActivity,PrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                        }


                    }

                    BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("jugadores").addValueEventListener(postListenerPasarDeEscena!!)

            }


        }




    }

    private fun listenerInvitadoPasarEscena() {
        postListenerInvitadoPasarEscena = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                BaseDatos.getInstance()?.getEtapaSala(wraperSala.id,object:IGetEtapaSala{
                    override fun onCallBack(etapa: Etapa) {
                        if (etapa.equals(Etapa.PARTIDA)){
                            pasarDeEscenaTematica()
                        }else if(etapa.equals(Etapa.GANADOR)){
                            pasarDeEscenaGanador()
                        }
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
                BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala.id).child("etapa").removeEventListener(postListenerInvitadoPasarEscena!!)
            }
        }

        BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala.id).child("etapa").addValueEventListener(postListenerInvitadoPasarEscena!!)
    }

    override fun onBackPressed() {


        super.onBackPressed()
    }

    private fun comprobarTiempo(){
        postListenerTiempo = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                BaseDatos.getInstance()!!.getSegundos(wraperSala!!.id, object : IGetSegundos{
                    override fun OnCallBack(segundos: Int) {
                        Log.d("Votacion ACTIVITY", "Segundos desde el servidor.")
                        if(segundos > 0){
                            binding.tvJugador.text="Esperando a que todos los jugadores acaben de hacer fotos"
                            if(wraperSala.sala.anfitrion.equals(User.getInstancia()!!.printToken())){


                                        BaseDatos.getInstance()?.getJugadoresFromSala(wraperSala.id,object:IGetJugadoresFromSala{
                                            override fun OnCallback(lista: ArrayList<Jugador>) {

                                                var enVotacion:Int=lista.filter { it.etapa.equals(Etapa.VOTACION) }.count()
                                                if(enVotacion == lista.size){
                                                    iniciarVotacion()
                                                    BaseDatos.getInstance()?.cambiarEstapaSala(wraperSala!!.id,Etapa.VOTACION)
                                                    BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListenerTiempo!!)


                                                }
                                            }


                                        })




                            }else{
                                postListenerEtapa= object : ValueEventListener {
                                    var escuchar = true
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        BaseDatos.getInstance()!!.getEtapaSala(wraperSala.id,object: IGetEtapaSala{
                                            override fun onCallBack(etapa: Etapa) {

                                                if(etapa.equals(Etapa.VOTACION)){
                                                    if(!User.getInstancia()!!.printToken().equals(wraperSala.sala.anfitrion)){
                                                        listenerInvitadoPasarEscena()
                                                    }
                                                    iniciarVotacion()
                                                }
                                            }

                                        })


                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // Getting Post failed, log a message
                                        //Mandar a la principal activity.
                                            Log.w("VOTACION LOBBY", "loadPost:onCancelled", databaseError.toException())
                                        var intent = Intent(this@VotacionActivity,PrincipalActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }


                                }

                                BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("etapa").addValueEventListener(postListenerEtapa!!)
                            }

                        }else if(segundos < 0){
                            Log.d("Votacion ACTIVITY","CERRANDO ESCUCHADOR SEGUNDOS")
                            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListenerTiempo!!)
                            var intent = Intent(this@VotacionActivity,PrincipalActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else if(segundos == 0){

                            iniciarVotacion()
                            if(wraperSala.sala.anfitrion.equals(User))
                            BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListenerTiempo!!)
                        }
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                //Mandar a la principal activity.
                Log.d("TEMATICA ACTIVITY","ON CANCELLED CERRANDO ESCUCHADOR SEGUNDOS")
                BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).removeEventListener(postListenerTiempo!!)

                Log.w("ACTIVITY LOBBY", "loadPost:onCancelled", databaseError.toException())
                var intent = Intent(this@VotacionActivity,PrincipalActivity::class.java)
                startActivity(intent)
                finish()
            }


        }

        BaseDatos.getInstance()!!.reference.child("salas").child(wraperSala!!.id).child("ronda").child("tiempo").addValueEventListener(postListenerTiempo!!)
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


