package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.vvieira.appauthenticator.Home
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.ActivityIdleBinding
import com.vvieira.appauthenticator.domain.model.Categoria
import com.vvieira.appauthenticator.recyclerviews.adapter.CategoriasAdapter

class IdleActivity : AppCompatActivity() {

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityIdleBinding // Declare binding as lateinit

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityIdleBinding.inflate(layoutInflater) // Initialize binding
        setContentView(binding.root)
        setupActionBar()

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> { Log.d("teste", "inicio")}
                R.id.favoritos -> {Log.d("teste", "inicio")}
                R.id.carrinho -> {Log.d("teste", "inicio")}
                R.id.perfil -> {Log.d("teste", "inicio")}
                R.id.pesquisar -> {Log.d("teste", "inicio")}

                else -> {}
            }
            true
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView(){
        val categoriasList = listOf(
            Categoria("Vidros", R.drawable.vidros),
            Categoria("Materiais", R.drawable.aluminio),
            Categoria("Orçamentos", R.drawable.orcamento),
            Categoria("Outros", R.drawable.outros)
        )

        val categoriasAdapter = CategoriasAdapter(this, categoriasList)
        binding.recyclerCategorias.adapter = categoriasAdapter
    }

    private fun setupActionBar(){
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_1 -> {
                    binding.drawerLayout.closeDrawer(binding.navView)
                    true
                }
                else -> {
                    binding.drawerLayout.closeDrawer(binding.navView)
                    true
                }
            }
            true
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                if (newState == DrawerLayout.STATE_IDLE && !drawerLayout.isDrawerOpen(navView)) {
                    // Se o drawer estiver fechado e o estado for STATE_IDLE, feche-o manualmente
                    drawerLayout.closeDrawer(navView)
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                else {
                    onBackPressedDispatcher.onBackPressed()
                }
                // Code that you need to execute on back press, e.g. finish()
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}