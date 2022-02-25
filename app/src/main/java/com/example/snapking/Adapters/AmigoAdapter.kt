package com.example.snapking.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.R
import com.example.snapking.Wrapper.WrapperUsuarioLobby
import com.example.snapking.databinding.UsuarioCardAddBinding
import com.example.snapking.databinding.UsuarioCardLobbyBinding
import com.example.snapking.modelo.Usuario

class AmigoAdapter(val usuarios:List<Usuario>) : RecyclerView.Adapter<AmigoAdapter.ViewHolder>() {

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

        fun bind(usuario: Usuario) {
            binding.txtjugador.text=usuario.nickname

            binding.txtjugador.setOnClickListener()
            {

            }
        }
    }
}