package com.vvieira.appauthenticator.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()

//        window.navigationBarColor = ContextCompat.getColor(this, R.color.np_fonseca_primary)
//        setTheme(R.style.Theme_NpFonseca)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            ),
            binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Verifique se o ID do destino atual PERTENCE ao auth_nav_graph
            // Isso é mais robusto do que listar cada fragmento de login/registro
            val isAuthDestination = destination.parent?.id == R.id.nav_graph_authentication ||
                    destination.id == R.id.cadastroFragment || // Caso o startDestination do NavHostFragment seja um fragmento direto
                    destination.id == R.id.acceptTermsFragment ||
                    destination.id == R.id.FinishRegisterFragment

            if (isAuthDestination) {
                supportActionBar?.hide()
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                binding.bottomNavigation.visibility = View.GONE
            } else {
                // Se não for um destino de autenticação, mostre a UI do app principal
                supportActionBar?.show()
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                binding.bottomNavigation.visibility = View.VISIBLE
            }
        }

        val usuarioAutenticado = false
        if (usuarioAutenticado) {
            navController.navigate(R.id.nav_graph_home, null,
                androidx.navigation.navOptions {
                    popUpTo(R.id.nav_graph_authentication) {
                        inclusive = true
                    } // Remove o grafo de autenticação
                    launchSingleTop = true // Evita múltiplas instâncias da Home
                }
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
