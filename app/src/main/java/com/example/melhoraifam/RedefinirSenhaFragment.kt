package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Fragmento para recuperação de senha
 */
class RedefinirSenhaFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_redefinir_senha, container, false)
        auth = Firebase.auth

        val emailEditText = view.findViewById<EditText>(R.id.email)
        val buttonVoltar = view.findViewById<Button>(R.id.buttonVoltar)
        val buttonEnviar = view.findViewById<Button>(R.id.buttonEnviar)

        // Volta para a tela de login
        buttonVoltar.setOnClickListener {
            retornarParaTeladeLogin()
        }

        // Envia o e-mail de recuperação de senha
        buttonEnviar.setOnClickListener {
            val email = emailEditText.text.toString()
            if (email.isEmpty()) {
                Toast.makeText(context, "E-mail não pode estar vazio!", Toast.LENGTH_SHORT).show()
            } else {
                recuperarSenha(email)
            }
        }

        return view
    }

    private fun recuperarSenha(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "E-mail de recuperação de senha enviado.", Toast.LENGTH_SHORT).show()
                    retornarParaTeladeLogin()
                } else {
                    Toast.makeText(context, "Erro ao tentar enviar e-mail de recuperação", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun retornarParaTeladeLogin() {
        val login: Fragment = LoginFragment()
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutMain, login)
        transaction.commit()
    }
}