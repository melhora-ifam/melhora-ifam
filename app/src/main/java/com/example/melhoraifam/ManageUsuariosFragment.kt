package com.example.melhoraifam

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ManageUsuariosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ManageUsuariosFragment : Fragment() {
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_usuarios, container, false)

        // Lógica do RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.Usuarios)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val usuariosList = arrayListOf(
            UsuarioModel(R.drawable.samus, "Fulana de Tal", true),
            UsuarioModel(R.drawable.samus, "Fulano de Tal", true),
            UsuarioModel(R.drawable.samus, "Ciclano de Tal", false),
            UsuarioModel(R.drawable.samus, "Beltrano de Tal", false),
            UsuarioModel(R.drawable.samus, "Ciclana de Tal", true),
            UsuarioModel(R.drawable.samus, "Fulana de Tal", true),
            UsuarioModel(R.drawable.samus, "Fulano de Tal", false),
            UsuarioModel(R.drawable.samus, "Ciclano de Tal", false),
        )
        recyclerView.adapter = UsuarioAdapter(usuariosList)

        // Lógica da search bar
        val search = view.findViewById<SearchView>(R.id.barraDePesquisaUsuarios)
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "Procurando por: $query", Toast.LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Toast.makeText(context, "Texto digitado: $newText", Toast.LENGTH_SHORT).show()
                return true
            }
        })

        // Lógica do navBar
        val navBar = requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navbar)
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
                    Toast.makeText(context, "Usuários", Toast.LENGTH_SHORT).show()
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ManageUsuariosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageUsuariosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}