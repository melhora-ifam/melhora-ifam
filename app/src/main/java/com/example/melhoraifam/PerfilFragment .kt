package com.example.melhoraifam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class PerfilFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        val view = inflater.inflate(R.layout.activity_perfil, container, false)

        // Lógica do navBar
        val navBar = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navbar)
        navBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_navbar -> {
                    val home: Fragment = HomeAdminFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, home)
                    transaction.commit()
                    true
                }
                R.id.usuarios_navbar -> {
                    val ocorrencias: Fragment = ManageUsuariosFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, ocorrencias)
                    transaction.commit()
                    true
                }
                R.id.perfil_navbar -> {
                    val perfilFragment: Fragment = PerfilFragment()
                    val transaction: FragmentTransaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayoutHome, perfilFragment)
                    transaction.commit()
                    true
                }
                else -> false
            }
        }

        return view
    }
}
