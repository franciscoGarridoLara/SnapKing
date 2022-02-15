package com.example.snapking.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.snapking.Wrapper.WrapperUsuarioPartida
import com.example.snapking.databinding.UsuariosVotacionBinding

class UsuarioAdapterVotacion(val wraperusuario: List<WrapperUsuarioPartida>) :
    RecyclerView.Adapter<UsuarioAdapterVotacion.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            UsuariosVotacionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val usuario = wraperusuario[position]
        holder.bind(usuario)
    }

    override fun getItemCount(): Int = wraperusuario.size


    class ViewHolder(val binding: UsuariosVotacionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wraperusuario: WrapperUsuarioPartida) {

            binding.txtPuntos.text = wraperusuario.puntos.toString()
            //binding.avatar.s

        }
    }
}
