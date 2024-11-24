package com.example.melhoraifam

import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [login.newInstance] factory method to
 * create an instance of this fragment.
 */
class login : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        // Referenciar o EditText e o TextView para erro
        val emailEditText = view.findViewById<EditText>(R.id.email)
        val errorTextView = view.findViewById<TextView>(R.id.erroLogin)
        val buttonEntrar = view.findViewById<Button>(R.id.buttonEntrar)


        buttonEntrar.setOnClickListener {

            val email = emailEditText.text.toString()

            if (email.isEmpty()) {

                errorTextView.text = "O email não pode estar vazio"
                errorTextView.visibility = View.VISIBLE // Tornar o erro visível
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Exibir erro se o email for inválido
                errorTextView.text = "Por favor, insira um email válido"
                errorTextView.visibility = View.VISIBLE // Tornar o erro visível
            } else {
                // Se o email for válido, esconder a mensagem de erro
                errorTextView.visibility = View.GONE
                // Proseguir com a ação (exemplo: login)
            }
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment login.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            login().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}