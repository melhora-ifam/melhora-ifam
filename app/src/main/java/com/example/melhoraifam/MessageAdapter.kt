package com.example.melhoraifam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MensagemAdapter(private val mensagens: List<Mensagem>) : RecyclerView.Adapter<MensagemAdapter.MensagemViewHolder>() {

    class MensagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textoMensagem: TextView = itemView.findViewById(R.id.textoMensagem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensagemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_mensagem, parent, false)
        return MensagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: MensagemViewHolder, position: Int) {
        val mensagem = mensagens[position]
        holder.textoMensagem.text = "${mensagem.remetente.nome}: ${mensagem.conteudo}"
    }

    override fun getItemCount(): Int {
        return mensagens.size
    }
}
