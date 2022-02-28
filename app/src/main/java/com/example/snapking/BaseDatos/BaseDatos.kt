package com.example.snapking.BaseDatos

import android.net.Uri
import android.nfc.Tag
import android.os.Debug
import android.text.BoringLayout
import android.util.Log
import com.example.snapking.Firebase.User
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.modelo.*
import com.google.android.gms.auth.api.signin.internal.Storage
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File

import java.sql.Wrapper
import kotlin.random.Random


class BaseDatos(){

    var database: FirebaseDatabase = Firebase.database
    val reference = database.reference
    val storageReference=Firebase.storage.reference
    lateinit var listaSalasglobal:ArrayList<WrapperSala>




    fun getUser(id:String, IGetUser:IGetUser) : Usuario?
    {
        var user : Usuario? = null

        var usuarios = reference.child("usuarios").get().addOnSuccessListener {
            for (usuario in it.children)
            {

                if(usuario.key.equals(id))
                {
                    Log.d(TAG,"Existe el usuario por lo que llamamos a la funcion de reconstruir.")
                    user = User.getInstancia()!!.reconstruirUsuario(usuario)
                    IGetUser.OnCallBack(user!!)
                }

            }
        }

        return user
    }

    fun getUsers(listaIds:ArrayList<String>, iGetUsuarios: IGetUsuarios){

        var users = ArrayList<Usuario>()
        var user : Usuario

        var usuarios = reference.child("usuarios").get().addOnSuccessListener {
            for (usuario in it.children)
            {
                if(listaIds.contains(usuario.key.toString()))
                {
                    Log.d(TAG,"Existe el usuario por lo que llamamos a la funcion de reconstruir.")
                    user = User.getInstancia()!!.reconstruirUsuario(usuario)
                    users.add(user)
                }

            }
            iGetUsuarios.OnCallBack(users)
        }


    }



    fun elminarJugadorSala(idSala:String,idJugador:String){
        reference.child("salas").child(idSala)
            .get().addOnSuccessListener { datasnapshot->
                var anfitrion=datasnapshot.child("anfitrion").value as String


               if(anfitrion.equals(idJugador)){
                    datasnapshot.ref.removeValue()
               }else {


                   for (jugadorsnap in datasnapshot.child("jugadores").children) {
                       var idBd = jugadorsnap.child("id").value as String
                       if (idBd.equals(idJugador)) {
                          jugadorsnap.ref.removeValue()
                       }
                   }
               }
        }
    }

    fun escribrSala(sala:WrapperSala){
        Log.d(BaseDatos.TAG,"Escribiendo sala")
        reference.child("salas").push().setValue(sala)

    }

       fun leerSala(interfazSala:ISalasLectura): ArrayList<WrapperSala> {
        var listasalas:ArrayList<WrapperSala>
        Log.d("-----------------fff","entradon")
        listaSalasglobal=ArrayList()
        listasalas=ArrayList()
           var listanueva: ArrayList<WrapperSala>? =null

        var datasnapshot = reference.child("salas").get().addOnSuccessListener {
            Log.d("-----------------fff","entradon2")
            for(child in it.children){

                var ronda:Ronda?
                try {
                    var fotos : ArrayList<Foto> = ArrayList()

                    for(foto in child.child("ronda").child("fotos").children){
                        fotos.add(Foto(foto.key.toString(),foto.value.toString()))
                    }
                    var numero=  child.child("ronda").child("numero").value as Long
                    var tiempo=child.child("ronda").child("tiempo").value as Long
                    tiempo.toInt()
                    ronda=Ronda(
                      numero.toInt(),
                        child.child("ronda").child("id_tematica").value as String,
                        fotos,
                        tiempo.toInt()

                    )
                } catch (e: NullPointerException) {
                    ronda=null
                }
                Log.d("-----------------fff","entradon3")
                var jugadores=ArrayList<Jugador>()

                    for(jugadorsnap in child.child("jugadores").children){
                        var long= jugadorsnap.child("punto").value as Long

                      var j:String?
                          j=jugadorsnap.key

                        Log.d("-----------------fff","entradon4")

                        if (j != null) {

                            var jugadorclass=Jugador(j,false, 0)
                            jugadores.add(jugadorclass)
                        }


                    }

                Log.d("-----------------fff","entradon5")
                    var sala=Sala(
                        child.child("nombre").value as String,
                        child.child("capacidad").value as Long,
                        child.child("anfitrion").value as String,
                        child.child("estado").value as Boolean,
                        ronda,
                        child.child("rondas_totales").value as Long,
                        jugadores
                    )
                    var clave=child.key
                var wrapperSala:WrapperSala?
                  wrapperSala=null
                if(clave!=null){
                     wrapperSala= WrapperSala(clave,sala)
                }

                Log.d("-----------------fff","entradon6")
                    if(wrapperSala!= null){
                        listasalas.add(wrapperSala)
                        Log.d("------------------mmmmmmm","base de datos size "+listasalas.size.toString())
                    }else{
                        Log.d("------------------mmmmmmm","nulllazooooooooo")

                    }
                    listaSalasglobal=listasalas




            }
            Log.d("-----------------fff","entradon7")
            interfazSala.OncallBack(listasalas)
            Log.d("-----------------fff","entradon8")

        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)

            //interfazSala.OncallBack(ArrayList<WrapperSala>())

        }

           Log.d("firebase", "--------------------------------"+listasalas.size.toString())




        return listaSalasglobal

    }

    fun leerSalabyId(id:String ): Sala? {
        var sala:Sala?
        sala=null
        reference.child("salas").child(id).get().addOnSuccessListener {
            sala=it.getValue<Sala>()
        }
        return sala

    }

    fun escribirSala(sala: Sala): String? {
        var clave=reference.child("salas").push().key
        if (clave != null) {
            reference.child("salas").child(clave).setValue(sala)
        }
        return clave

    }
    fun meterJugadorSala(id:String,jugador:Jugador){
        reference.child("salas").child(id).child("jugadores").push().setValue(jugador)
    }

    fun getUsersWithNickname(nickname:String) : List<WrapperUsuario>?{
        var usuarios : ArrayList<WrapperUsuario>?
        usuarios = ArrayList<WrapperUsuario>()
        var userFound : WrapperUsuario?
        reference.child("usuarios").get().addOnSuccessListener {

            for (usuario in it.children)
            {
                var userNickname = usuario.child("nickname").value as String
                var lvl = usuario.child("nivel").value as Long
                var avatar = usuario.child("avatar").value as String

                if(userNickname.contains(nickname))
                {
                    userFound = WrapperUsuario(usuario.key.toString(),
                            Usuario(
                                userNickname,
                                avatar,
                                lvl,
                                ArrayList()
                            )
                        )

                    for(amigo in usuario.child("amigos").children)
                    {
                        var idAmigo = amigo.value as String
                        if(idAmigo != null)
                        {
                            userFound!!.usuario.amigos.add(idAmigo!!)
                        }
                    }


                    if(userFound != null)
                    {
                        usuarios.add(userFound!!)
                    }
                }
            }


        }
        return usuarios
    }

    /*
    fun getUsersFromSala(idSala:String, iGetUsersFromSala: IGetUsersFromSala){
        var usuarios : ArrayList<WrapperUsuarioLobby>?
        usuarios = ArrayList<WrapperUsuarioLobby>()
        var listaIds:ArrayList<String>
        var sala : WrapperSala

        reference.child("salas").child(idSala).child("jugadores").get().addOnSuccessListener {
            for(user in it.children)
            {
                var idUser:String = user.child("id").value as String
                var estado = user.child("ready").value as Boolean
                Log.d(TAG,idUser + ":" + estado.toString())

                getUser(idUser,object : IGetUser{
                    override fun OnCallBack(user: Usuario) {
                        usuarios.add(WrapperUsuarioLobby(user!!,estado))
                        Log.d(TAG, "USER: -> " + user.toString())
                    }
                })
            }

        }.addOnCompleteListener {
            Log.d(TAG,usuarios.toString())
            iGetUsersFromSala.OnCallBack(usuarios)
        }




    }


     */

    fun getUsersFromSala(idSala:String, iGetUsersFromSala: IGetUsersFromSala){
        var wrapperUsuarios : ArrayList<WrapperUsuarioLobby> = ArrayList()
        var users : ArrayList<Usuario>

        var listaIds:ArrayList<String> = ArrayList()
        var listaEstados:ArrayList<Boolean> = ArrayList()
        var sala : WrapperSala

        reference.child("salas").child(idSala).child("jugadores").get().addOnSuccessListener {

            for(jugador in it.children)
            {
                var idUser = jugador.child("id").value as String
                var estadoUser = jugador.child("ready").value as Boolean

                listaIds.add(idUser)
                listaEstados.add(estadoUser)

            }

            getUsers(listaIds, object: IGetUsuarios{
                override fun OnCallBack(usuarios: ArrayList<Usuario>) {

                    var i=0
                       while(i<usuarios.size) {
                           var usuario=usuarios[i]
                           var estado=listaEstados[i]
                           var wrapper : WrapperUsuarioLobby = WrapperUsuarioLobby(usuario,estado)
                           wrapperUsuarios.add(wrapper)
                           i++
                       }
                    iGetUsersFromSala.OnCallBack(wrapperUsuarios)
                }

            })



        }

    }

    fun getJugadoresFromSala(idSala:String,iGetJugadoresFromSala: IGetJugadoresFromSala){
        reference.child("salas").child(idSala).child("jugadores").get().addOnSuccessListener {datasnap->

            var jugadores=ArrayList<Jugador>()
            for(jugadorsnap in datasnap.children ){
                var jugador: Jugador? =jugadorsnap.getValue<Jugador>()

                if (jugador != null) {
                    jugadores.add(jugador)
                }
            }
            iGetJugadoresFromSala.OnCallback(jugadores)



        }
    }

    fun empezarPartida(idSala:String){
        reference.child("salas").child(idSala).child("start").setValue(true)
    }
    fun comprobarPartida(idSala:String,iComprobarStart: IComprobarStart){
        reference.child("salas").child(idSala).child("start").get().addOnSuccessListener {
            var ready=false
              try {
            ready=it.value as Boolean
        } catch (e: NullPointerException) {

        }
            iComprobarStart.OncallBack(ready)
        }
    }

    fun setUserReadySala(idSala:String, idJugador:String,ready:Boolean,iReady: IReady){
        reference.child("salas").child(idSala).child("jugadores").get().addOnSuccessListener {

            for(user in it.children)
            {
                var userId = user.child("id").value as String
                if(userId.equals(idJugador))
                {
                    user.child("ready").ref.setValue(ready)
                    break
                }
            }
            iReady.OnCallback()
        }
    }


    fun agregarUsuario(idUsuario: String)
    {
        var wrapperUsuario : WrapperUsuario = User.wrapper!!

        if(wrapperUsuario != null && wrapperUsuario.usuario != null && !wrapperUsuario.usuario.amigos.contains(idUsuario))
        {
            Log.d(TAG, "Agregando amigo " + idUsuario + " a el usuario " + wrapperUsuario.usuario.nickname)
            wrapperUsuario.usuario.amigos.add(idUsuario)
            actualizarUsuario(wrapperUsuario)
        }



    }

    fun actualizarUsuario(usuario : WrapperUsuario){
        reference.child("usuarios").child(usuario.id).setValue(usuario.usuario)
    }


    fun getTematicaRandom(iGetTematica : IGetTematica){

        var tematicas = reference.child("tematicas").get().addOnSuccessListener {
            var listaTematicas : ArrayList<Tematica> = ArrayList()

            for(categoria in it.children){
                var cat = categoria.key as String
                for(tematica in categoria.children){
                    var tematicaId = tematica.key as String
                    var tematicaNombre = tematica.value as String
                    listaTematicas.add(Tematica(tematicaId,cat,tematicaNombre))
                }
            }

            var random = Random.nextInt(0, listaTematicas.size - 1 )

            var tematicaElegida = listaTematicas[random]

            iGetTematica.OnCallBack(tematicaElegida)
        }

    }

    fun crearRonda(idSala: String, iGetRonda : IGetRonda){

        getTematicaRandom(object : IGetTematica{
            override fun OnCallBack(tematica: Tematica) {

                var sala = reference.child("salas").child(idSala).get().addOnSuccessListener {
                    var ronda : Ronda? = null
                    try {
                        Log.d(TAG,"Success Listener!")
                        ronda = it.child("ronda").getValue<Ronda>()!!
                        Log.d(TAG,"Ronda: " + ronda.toString())
                        ronda = Ronda(ronda.numero + 1, tematica.id, ronda.fotos,ronda.tiempo)
                        iGetRonda.OnCallBack(ronda)
                    } catch (e: Exception) {
                            ronda = Ronda(1,tematica.id,ArrayList(),60)
                            iGetRonda.OnCallBack(ronda)
                        Log.d(TAG,"No hay ronda-------- ")
                    }finally {
                        Log.d(TAG, "Escribiendo ronda en la sala FB")
                        it.ref.child("ronda").setValue(ronda!!)
                    }


                }

            }

        })



    }

    fun actualizarTiempo(idSala : String, segundos : Int){
        reference.child("salas").child(idSala).child("ronda").child("tiempo").setValue(segundos)
    }

    fun getSegundos(idSala : String, iGetSegundos: IGetSegundos){

        reference.child("salas").child(idSala).child("ronda").get().addOnSuccessListener {
            var segundos = it.child("tiempo").value as Int
            iGetSegundos.OnCallBack(segundos)
        }

    }

    fun subirfoto(fileURL: String,idSala: String,idUser:String) {


        var file = Uri.fromFile(File(fileURL))
        val riversRef = storageReference.child("fotos/${idSala}/${file}")
        var uploadTask = riversRef.putFile(file)

// Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
            Log.d(TAG,"foto no subida")
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
            Log.d(TAG,"foto subida")
            val downloadUri:String = uploadTask.result.toString()
            Log.d(TAG,"URL FICHERO "+downloadUri)

            escribirBDFoto(idSala,idUser,downloadUri)

        }

    }

    private fun escribirBDFoto(idSala:String,idUser:String,url:String){

        reference.child("salas").child(idSala).child("ronda").child("fotos").
                child(idUser).setValue(url)

    }

    fun elminarSala(id: String) {
        reference.child("salas").child(id).removeValue()

    }

    /*
    fun getListWrapperUsuariosFromListIds(ids : List<String>) : ArrayList<WrapperUsuario>
    {
        var listaWrapperUsuarios : ArrayList<WrapperUsuario> = ArrayList()
        Log.d(TAG,"SE LLAMA A LA FUNCION DE RECONSTRUIR -------------------")

        reference.child("usuarios").get().addOnSuccessListener {
            for(usuario in it.children)
            {
                var id:String = usuario.key.toString()
                Log.d(TAG,"Id de la base de datos: " + id.toString())
                for(idLista in ids)
                {
                    Log.d(TAG,"Id de la lista de amigos del usuario: " + idLista)
                    if(id.toString().equals(idLista))
                    {
                        listaWrapperUsuarios.add(WrapperUsuario(id,reconstruirUsuario(usuario)))
                        break
                    }
                }

            }


        }

        return listaWrapperUsuarios
    }


*/



    companion object {
        private var instance: BaseDatos? = null
        var TAG:String = "DATABASE LOGS"

        @Synchronized
        private fun createInstance() {
            if (instance == null) {
                instance = BaseDatos()
            }
        }


        fun getInstance(): BaseDatos? {
            if (instance == null) createInstance()
            return instance
        }
    }

    init {
        println("Singleton invoked")
    }


}