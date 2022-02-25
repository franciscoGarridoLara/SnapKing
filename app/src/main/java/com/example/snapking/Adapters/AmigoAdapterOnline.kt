package com.example.snapking.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.BaseDatos.BaseDatos
import com.example.snapking.databinding.UsuarioCardBinding
import com.example.snapking.modelo.WrapperUsuario
import com.squareup.picasso.Picasso

class AmigoAdapterOnline(val usuarios:List<WrapperUsuario>) : RecyclerView.Adapter<AmigoAdapterOnline.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=UsuarioCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario=usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int =usuarios.size


    class ViewHolder(val binding:UsuarioCardBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(usuario: WrapperUsuario) {
            binding.txtjugador.text=usuario.usuario.nickname
            var avatar = usuario.usuario.avatar.toString()
            if(!avatar.isEmpty())
            {
                Picasso.get()
                    .load(avatar)
                    .into(binding.fotoPerfil)
            }


        }
    }
}