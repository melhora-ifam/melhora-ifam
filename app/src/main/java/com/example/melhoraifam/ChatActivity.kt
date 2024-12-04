package com.example.melhoraifam

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class ChatActivity : AppCompatActivity() {

    private lateinit var mensagemRepository: MensagemRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MensagemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mensagemRepository = MensagemRepository()
        recyclerView = findViewById(R.id.messageRecyclerView)
        adapter = MensagemAdapter(mensagemRepository.obterMensagens())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.visibility = RecyclerView.INVISIBLE

        // Configurar o botão de enviar
        val sendButton: ImageView = findViewById(R.id.sendButton)
        val messageEditText: EditText = findViewById(R.id.messageEditText)

        sendButton.setOnClickListener {
            val conteudo = messageEditText.text.toString()
            if (conteudo.isNotEmpty()) {
                val remetente = Remetente("1", "Remetente Exemplo")
                val destinatario = Destinatario("2", "Destinatário Exemplo")
                val mensagem = Mensagem(conteudo, remetente, destinatario, System.currentTimeMillis())
                mensagemRepository.adicionarMensagem(mensagem)

                // Torna o RecyclerView visível ao enviar a mensagem
                recyclerView.visibility = RecyclerView.VISIBLE

                adapter.notifyDataSetChanged() // Atualiza o RecyclerView

                // Rola para a última mensagem
                recyclerView.scrollToPosition(adapter.itemCount - 1)

                messageEditText.text.clear() // Limpa o campo de entrada
            }
        }
    }
}