package com.example.snapking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.Adapters.AmigoAdapter
import com.example.snapking.Adapters.AmigoAdapterOnline
import com.example.snapking.Adapters.UsuarioAdapter
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.Firebase.User

class AmigosActivity : AppCompatActivity() {

    private lateinit var etBusqueda: EditText
    private lateinit var rvAmigos: RecyclerView
    private lateinit var ibLupa: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_amigos)

        inicializarInterfaz()
        setListeners()
        cargarAmigos()
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
                        ?.let { it1 -> AmigoAdapter(it1) }

                rvAmigos.adapter = adapter

            }else
            {
                Toast.makeText(this,"El campo no puede ser nulo",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cargarAmigos()
    {
        var adapter =
            BaseDatos.getInstance()!!.getListWrapperUsuariosFromListIds(User.getInstancia()!!.user.amigos)
                ?.let { it1 -> AmigoAdapterOnline(it1) }

        rvAmigos.adapter = adapter

    }

}
