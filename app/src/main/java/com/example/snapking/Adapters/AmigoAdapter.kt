package com.example.snapking.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.MainActivity
import com.example.snapking.databinding.UsuarioCardAddBinding
import com.example.snapking.modelo.WrapperUsuario
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*

class AmigoAdapter(val context: Context,val usuarios:List<WrapperUsuario>) : RecyclerView.Adapter<AmigoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=UsuarioCardAddBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario=usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int =usuarios.size


    class ViewHolder(val binding:UsuarioCardAddBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: WrapperUsuario){
            binding.txtjugador.text=usuario.usuario.nickname
            var avatar = usuario.usuario.avatar.toString()
            if(!avatar.isEmpty())
            {
                Picasso.get()
                    .load(avatar)
                    .into(binding.fotoPerfil)
            }

            binding.btnAdd.setOnClickListener()
            {
                BaseDatos.getInstance()!!.agregarUsuario(usuario.id)

            }
        }
    }
}