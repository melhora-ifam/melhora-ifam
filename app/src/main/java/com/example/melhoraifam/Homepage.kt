package com.example.melhoraifam

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.melhoraifam.databinding.ActivityHomepageBinding

class Homepage : AppCompatActivity() {
    private lateinit var binding : ActivityHomepageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("Homepage", "onCreate: Layout carregado com sucesso");

        binding = ActivityHomepageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeUserFragment())

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
}