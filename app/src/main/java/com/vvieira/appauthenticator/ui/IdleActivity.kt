package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import com.google.android.material.navigation.NavigationView
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.ActivityIdleBinding
import com.vvieira.appauthenticator.ui.idle.HomeFragment

class IdleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIdleBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivityIdleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupBottomNavigation()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fragment_container, HomeFragment())
                // Se você usar o Navigation Component, a navegação será gerenciada de outra forma
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> {
                    Log.d("teste", "inicio")
                    supportFragmentManager.commit {
                        replace(R.id.fragment_container, HomeFragment())
                    }
                    true
                }
                R.id.favoritos -> {
                    Log.d("teste", "favoritos")
                    // Substitua pelo seu Fragment de favoritos
                    true
                }
                R.id.carrinho -> {
                    Log.d("teste", "carrinho")
                    // Substitua pelo seu Fragment de carrinho
                    true
                }
                R.id.perfil -> {
                    Log.d("teste", "perfil")
                    // Substitua pelo seu Fragment de perfil
                    true
                }
                R.id.pesquisar -> {
                    Log.d("teste", "pesquisar")
                    // Substitua pelo seu Fragment de pesquisa
                    true
                }
                else -> false
            }
        }
    }

    private fun setupActionBar() {
        drawerLayout = binding.drawerLayout
        navView = binding.navView
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_1 -> {
                    drawerLayout.closeDrawer(navView)
                    true
                }
                else -> {
                    drawerLayout.closeDrawer(navView)
                    true
                }
            }
            true
        }

        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                if (newState == DrawerLayout.STATE_IDLE && !drawerLayout.isDrawerOpen(navView)) {
                    drawerLayout.closeDrawer(navView)
                }
            }
        })

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    onBackPressedDispatcher.onBackPressed()
                }
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