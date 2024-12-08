package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * Fragmento que cuida do LOGIN
 */
class LoginFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        Log.d("LoginFragment", "onStart chamado")
        // Verifica se o usuário já está logado, e se sim, redireciona para a Homepage
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("LoginFragment", "Usuário já logado: ${currentUser.email}")
            val intent = Intent(requireContext(), Homepage::class.java)
            startActivity(intent)
        } else {
            Log.d("LoginFragment", "Nenhum usuário logado")
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("LoginFragment", "onCreateView chamado")

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = Firebase.auth

        val emailEditText = view.findViewById<EditText>(R.id.email)
        val senhaEditText = view.findViewById<EditText>(R.id.senha)
        val buttonEntrar = view.findViewById<Button>(R.id.buttonLogin)
        val tvCadastrarUsuario = view.findViewById<TextView>(R.id.cadastrar)
        val tvRecuperarSenha = view.findViewById<TextView>(R.id.recuperarSenha)

        buttonEntrar.setOnClickListener {
            val email = emailEditText.text.toString()
            val senha = senhaEditText.text.toString()

            // Condições de controle
            val emailVazio = email.isEmpty()
            val senhaVazia = senha.isEmpty()
            val emailValido = Patterns.EMAIL_ADDRESS.matcher(email).matches()

            if (emailVazio) {
                Toast.makeText(context, "O e-mail não pode estar vazio", Toast.LENGTH_SHORT).show()
            } else if (senhaVazia) {
                Toast.makeText(context, "Por favor insira uma senha", Toast.LENGTH_SHORT).show()
            } else if (! emailValido){
                Toast.makeText(context, "Formato de e-mail inválido", Toast.LENGTH_SHORT).show()
            } else {
                // E-mail válido
                realizarLogin(email, senha)
            }
        }

        // Trocar de fragmento, caso queira cadastrar novo usuário
        tvCadastrarUsuario.setOnClickListener {
            val cadastro: Fragment = CadastroFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutMain, cadastro)
            transaction.commit()
        }

        // Recuperar a senha
        tvRecuperarSenha.setOnClickListener {
            val recuperacao: Fragment = RedefinirSenhaFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayoutMain, recuperacao)
            transaction.commit()
        }

        return view
    }

    private fun realizarLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Log.d("LoginFragment", "Login bem-sucedido para: ${user?.email}")
                    val intent = Intent(requireContext(), Homepage::class.java)
                    startActivity(intent)
                } else {
                    Log.e("LoginFragment", "Erro ao fazer login")
                    Toast.makeText(context, "E-mail ou senha incorretos", Toast.LENGTH_SHORT).show()
                }
            }
    }
}