package com.example.melhoraifam

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

/**
 * Fragmento responsável pelo CADASTRO de novos usuários
 */
class CadastroFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cadastro, container, false)
        auth = Firebase.auth

        val nomeEditText = view.findViewById<EditText>(R.id.nome)
        val emailEditText = view.findViewById<EditText>(R.id.email)
        val celularEditText = view.findViewById<EditText>(R.id.celular)
        val senhaEditText = view.findViewById<EditText>(R.id.senha)
        val cadastrarButton = view.findViewById<Button>(R.id.buttonCadastrar)

        cadastrarButton.setOnClickListener {
            val nome = nomeEditText.text.toString()
            val email = emailEditText.text.toString()
            val celular = celularEditText.text.toString()
            val senha = senhaEditText.text.toString()

            // Condições de controle
            val camposVazios = nome.isEmpty() || email.isEmpty() || celular.isEmpty() || senha.isEmpty()
            val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()
            val dominioIFAM = email.endsWith("@ifam.edu.br")
            val senhaMinDigitos = senha.length >= 8

            if (camposVazios) {
                Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
            } else if (! emailValido) {
                Toast.makeText(context, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show()
            } else if (! dominioIFAM) {
                Toast.makeText(context, "E-mail deve pertencer ao domínio do IFAM", Toast.LENGTH_SHORT).show()
            } else if (! senhaMinDigitos) {
                Toast.makeText(context, "Senha muito fraca. Deve ter no mínimo 8 caracteres", Toast.LENGTH_SHORT).show()
            } else {
                realizarCadastro(nome, email, celular, senha)
            }
        }

        return view
    }

    private fun realizarCadastro(nome: String, email: String, celular: String, senha: String) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    // Salvando dados no Realtime Database
                    val database = Firebase.database
                    val userRef = database.getReference("users").child(userId!!)

                    val userMap = mapOf(
                        "nome" to nome,
                        "email" to email,
                        "celular" to celular,
                        "admin" to false
                    )

                    userRef.setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            // Navegar para login
                            val loginFragment = LoginFragment()
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.replace(R.id.frameLayoutMain, loginFragment)
                            transaction.commit()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Erro ao salvar dados no banco", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(context, "Falha no cadastro...", Toast.LENGTH_SHORT).show()
                }
            }
    }
}