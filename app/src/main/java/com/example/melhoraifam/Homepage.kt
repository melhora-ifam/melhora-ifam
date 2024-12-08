package com.example.melhoraifam

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.melhoraifam.databinding.ActivityHomepageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Homepage : AppCompatActivity() {
    private lateinit var binding : ActivityHomepageBinding
    // private lateinit var databaseReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Carregar o fragmento correto dependendo se for admin ou não
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            Log.d("Homepage", "Usuário logado: ${currentUser.uid}")
            checkAdminStatus(currentUser)
        }

        val notificacoes = findViewById<ImageView>(R.id.sininho)
        var currentIcon = true
        notificacoes.setOnClickListener {
            if (currentIcon) {
                notificacoes.setImageResource(R.drawable.bell_ringing)
            } else {
                notificacoes.setImageResource(R.drawable.bell)
            }
            currentIcon = !currentIcon
        }

        // Adicionar aqui a lógica de trocar de fragmento

        /*enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayoutHome, fragment)
        fragmentTransaction.commit()
    }

    private fun checkAdminStatus(currentUser: FirebaseUser) {
        val userID = currentUser.uid
        val database = Firebase.database
        val userRef = database.getReference("users").child(userID)

        // Lógica de acesso ao atributo admin no realtime database
        userRef.child("admin").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val isAdmin = snapshot.getValue(Boolean::class.java) ?: false
                    Log.d("Homepage", "Atributo admin encontrado: $isAdmin")
                    if (isAdmin) {
                        loadAdmin()
                    } else {
                        loadUser()
                    }
                } else {
                    Log.d("Homepage", "Atributo admin não encontrado")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Homepage", "Erro ao acessar o banco de dados: ${error.message}")
            }
        })
    }

    private fun loadAdmin() {
        val bottomNavBar = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navbar)
        bottomNavBar.menu.clear()
        bottomNavBar.inflateMenu(R.menu.admin_navbar)

        replaceFragment(HomeAdminFragment())
    }

    private fun loadUser() {
        val bottomNavBar = findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(R.id.bottom_navbar)
        bottomNavBar.menu.clear()
        bottomNavBar.inflateMenu(R.menu.bottom_navbar)

        replaceFragment(HomeUserFragment())
    }
}