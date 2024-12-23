package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PerfilFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var isAdmin: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflar o layout para este fragmento
        val view = inflater.inflate(R.layout.activity_perfil, container, false)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            checkAdminStatus(currentUser)
        }

        // Configurar a barra de navegação
        setupNavBar()

        val encerrar_sessao = view.findViewById<LinearLayout>(R.id.encerrar_sessao)
        val encerrar_sessao_tv = view.findViewById<TextView>(R.id.tvLogout)
        encerrar_sessao.setOnClickListener { finalizarSessao() }
        encerrar_sessao_tv.setOnClickListener { finalizarSessao() }

        val alterarSenha = view.findViewById<LinearLayout>(R.id.alterarSenhaPerfil)
        alterarSenha.setOnClickListener {
            val recuperacao: Fragment = RedefinirSenhaFragment()
            val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main, recuperacao)
            transaction.commit()
        }

        return view
    }

    private fun checkAdminStatus(currentUser: FirebaseUser) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.getReference("users").child(currentUser.uid)

        userRef.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                isAdmin = snapshot.getValue(Boolean::class.java) ?: false
                setupNavBar() // Atualiza a lógica do NavBar após verificar o status
            }

            override fun onCancelled(error: DatabaseError) {
                // Log de erro se necessário
            }
        })
    }

    private fun setupNavBar() {
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navbar)

        navBar.setOnItemSelectedListener { menuItem ->
            when {
                isAdmin -> handleAdminNavigation(menuItem.itemId)
                else -> handleUserNavigation(menuItem.itemId)
            }
        }
    }

    private fun handleAdminNavigation(itemId: Int): Boolean {
        return when (itemId) {
            R.id.home_navbar -> {
                navigateToFragment(HomeAdminFragment())
                true
            }
            R.id.usuarios_navbar -> {
                navigateToFragment(ManageUsuariosFragment())
                true
            }
            R.id.perfil_navbar -> {
                navigateToFragment(PerfilFragment())
                true
            }
            else -> false
        }
    }

    private fun handleUserNavigation(itemId: Int): Boolean {
        return when (itemId) {
            R.id.home_navbar -> {
                navigateToFragment(HomeUserFragment())
                true
            }
            R.id.ocorrencias_navbar -> {
                navigateToFragment(MyOcorrenciasFragment())
                true
            }
            R.id.perfil_navbar -> {
                navigateToFragment(PerfilFragment())
                true
            }
            else -> false
        }
    }

    private fun navigateToFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayoutHome, fragment)
        transaction.commit()
    }

    private fun finalizarSessao() {
        Toast.makeText(context, "Encerrando a sessão...", Toast.LENGTH_SHORT).show()
        auth.signOut()
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
    }

}
