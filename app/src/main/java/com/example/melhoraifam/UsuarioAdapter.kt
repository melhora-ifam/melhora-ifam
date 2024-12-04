package com.example.melhoraifam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsuarioAdapter(private val usuariosList: ArrayList<UsuarioModel>) : RecyclerView.Adapter<UsuarioAdapter.UsuarioViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioAdapter.UsuarioViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.usuario_card, parent, false)
        return UsuarioViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UsuarioAdapter.UsuarioViewHolder, position: Int) {
        val usuario = usuariosList[position]
        holder.imagem.setImageResource(usuario.imagem)
        holder.nome.text = usuario.nome
        if (usuario.admin) {
            holder.admin.setImageResource(R.drawable.admin)
        } else {
            holder.admin.setImageResource(R.drawable.upgrade)
        }
    }

    override fun getItemCount(): Int {
        return usuariosList.size
    }

    class UsuarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagem = itemView.findViewById<ImageView>(R.id.imagem_usuario)
        val nome = itemView.findViewById<TextView>(R.id.nomeUsuarioTxt)
        val admin = itemView.findViewById<ImageView>(R.id.statusAdmin)
    }
}