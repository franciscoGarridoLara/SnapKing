package com.example.snapking.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.R
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.databinding.UsuarioCardLobbyBinding

class UsuarioAdapter(val usuarios:List<WrapperUsuarioLobby>) : RecyclerView.Adapter<UsuarioAdapter.ViewHolder>() {





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=UsuarioCardLobbyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val usuario=usuarios[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int =usuarios.size


    class ViewHolder(val binding:UsuarioCardLobbyBinding):RecyclerView.ViewHolder(binding.root) {


        fun bind(wrapperUsuario: WrapperUsuarioLobby) {
            binding.txtjugador.text=wrapperUsuario.usuario.nickname
            if(!wrapperUsuario.estado){
                binding.prepadoIma.visibility=View.INVISIBLE
            }else{
                binding.prepadoIma.visibility=View.VISIBLE
            }

        }
    }
}