package com.example.melhoraifam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import android.app.AlertDialog
import android.widget.Button
import android.content.Context
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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

    private lateinit var database: DatabaseReference
    private lateinit var userList: MutableList<UsuarioModel>
    private lateinit var idsList: MutableList<String>
    private lateinit var adapter: UsuarioAdapter
    private lateinit var userRecyclerView: RecyclerView

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
        database = Firebase.database.reference
        userList = mutableListOf()
        idsList = mutableListOf()
        adapter = UsuarioAdapter(userList, idsList)
        userRecyclerView = view.findViewById<RecyclerView>(R.id.Usuarios)
        userRecyclerView.layoutManager = LinearLayoutManager(context)
        users()

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

    private fun users() {
        database.child("users").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    userList.clear()
                    idsList.clear()
                    for (userSnapshot in snapshot.children) {
                        val user = userSnapshot.getValue(UsuarioModel::class.java)
                        val id = userSnapshot.key
                        if (user != null && id != null) {
                            userList.add(user)
                            idsList.add(id)
                        }
                    }
                    adapter = UsuarioAdapter(userList, idsList)
                    userRecyclerView.adapter = adapter

                    // Adiciona interatividade nos cards
                    adapter.setOnItemClickListener(object: UsuarioAdapter.OnItemClickListener{
                        override fun onItemClick(id: String) {
                            Toast.makeText(context, "Card com o id $id selecionado", Toast.LENGTH_SHORT).show() // aqui abre o promver admin
                            abrirDialog(id)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeAdminFragment", "Erro ao recuperar dados: ${error.message}")            }
        })
    }


    fun abrirDialog(userId: String) {
        Log.d("ManageUsuariosFragment", "Abrindo diálogo...")
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_promover_admin, null)

        builder.setView(view)
        val dialogo = builder.create()

        val btnSim = view.findViewById<Button>(R.id.buttonSim)
        val btnNao = view.findViewById<Button>(R.id.buttonNao)

        btnSim.setOnClickListener {
            val userRef = Firebase.database.reference.child("users").child(userId)
            userRef.child("admin").setValue(true)
            Toast.makeText(context, "Usuário promovido!", Toast.LENGTH_SHORT).show()
            dialogo.dismiss()
        }

        btnNao.setOnClickListener {
            Toast.makeText(context, "Ação cancelada!", Toast.LENGTH_SHORT).show()
            dialogo.dismiss()
        }

        dialogo.show()
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