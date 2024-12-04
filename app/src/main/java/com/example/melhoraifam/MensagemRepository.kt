package com.example.melhoraifam

class MensagemRepository {
    private val mensagens = mutableListOf<Mensagem>()

    fun adicionarMensagem(mensagem: Mensagem) {
        mensagens.add(mensagem)
    }

    fun obterMensagens(): List<Mensagem> {
        return mensagens
    }
}
