package com.example.melhoraifam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OcorrenciaAdapter(
    private val ocorrenciasList: MutableList<OcorrenciaModel>,
    private val idsList: MutableList<String>
) : RecyclerView.Adapter<OcorrenciaAdapter.OcorrenciaViewHolder>() {
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(id: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OcorrenciaAdapter.OcorrenciaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.ocorrencia_card, parent, false)
        return OcorrenciaViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: OcorrenciaAdapter.OcorrenciaViewHolder, position: Int) {
        val ocorrencia = ocorrenciasList[position]
        holder.titulo.text = ocorrencia.titulo
        holder.descricao.text = ocorrencia.descricao
        holder.status.text = ocorrencia.status
        holder.prioridade.text = ocorrencia.prioridade
        holder.local.text = ocorrencia.local
        holder.categoria.text = ocorrencia.categoria

        holder.itemView.setOnClickListener {
            val id = idsList[position]
            mListener.onItemClick(id)
        }
    }

    override fun getItemCount(): Int {
        return ocorrenciasList.size
    }

    class OcorrenciaViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val titulo = itemView.findViewById<TextView>(R.id.titulo_ocorrencia)
        val descricao = itemView.findViewById<TextView>(R.id.descricao_ocorrencia)
        val status = itemView.findViewById<TextView>(R.id.card_status_txt)
        val prioridade = itemView.findViewById<TextView>(R.id.card_prioridade_txt)
        val local = itemView.findViewById<TextView>(R.id.card_local_txt)
        val categoria = itemView.findViewById<TextView>(R.id.card_categoria_txt)
        val imagem = itemView.findViewById<ImageView>(R.id.imagem_ocorrencia)

        /*init {
            itemView.setOnClickListener() {
                listener.onItemClick(adapterPosition)
            }
        }*/
    }
}