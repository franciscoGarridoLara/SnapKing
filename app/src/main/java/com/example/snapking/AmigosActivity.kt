package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.Adapters.AmigoAdapter
import com.example.snapking.Adapters.AmigoAdapterOnline
import com.example.snapking.Adapters.UsuarioAdapter
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.Firebase.User
import com.example.snapking.modelo.WrapperUsuario
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AmigosActivity : AppCompatActivity() {

    private lateinit var etBusqueda: EditText
    private lateinit var rvAmigos: RecyclerView
    private lateinit var ibLupa: ImageButton
    private var database = Firebase.database
    private var reference = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos)

        inicializarInterfaz()
        cargarAmigos()
        setListeners()

    }

    private fun inicializarInterfaz() {
        rvAmigos = findViewById(R.id.rvAmigos)
        etBusqueda = findViewById(R.id.etBusqueda)
        ibLupa = findViewById(R.id.lupa)
    }

    private fun setListeners() {
        ibLupa.setOnClickListener()
        {
            if(!etBusqueda.text.isEmpty())
            {
                var adapter =
                    BaseDatos.getInstance()!!.getUsersWithNickname(etBusqueda.text.toString())
                        ?.let { it1 -> AmigoAdapter(this,it1) }

                rvAmigos.adapter = adapter

            }else
            {
                Toast.makeText(this,"El campo no puede ser nulo",Toast.LENGTH_SHORT).show()
            }
        }

        listenerAmigos()
    }

    private fun listenerAmigos(){



    }


    private fun cargarAmigos()
    {
        /*Log.d("---------- USER ----------","Usuario AmigosActivity" + User.getInstancia()!!.user.toString())
        var adapter =
            BaseDatos.getInstance()!!.getListWrapperUsuariosFromListIds(User.getInstancia()!!.user.amigos)
                ?.let { it1 -> AmigoAdapterOnline(it1) }

        rvAmigos.adapter = adapter*/



            reference.child("usuarios").get().addOnSuccessListener {

                var listaWrapperUsuarios : ArrayList<WrapperUsuario> = ArrayList()

                for(usuario in it.children)
                {
                    var id:String = usuario.key.toString()

                    if(User.user?.amigos != null)
                    {
                        for(idLista in User.user?.amigos!!)
                        {
                            if(id.toString().equals(idLista))
                            {
                                listaWrapperUsuarios.add(WrapperUsuario(id,User.getInstancia()!!.reconstruirUsuario(usuario)))
                                break
                            }
                        }
                    }

                }
                var adapter = listaWrapperUsuarios.let { it -> AmigoAdapterOnline(it) }
                rvAmigos.adapter = adapter
            }



    }

}
