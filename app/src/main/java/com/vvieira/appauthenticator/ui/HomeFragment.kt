package com.vvieira.appauthenticator.ui.idle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.vvieira.appauthenticator.R
import com.vvieira.appauthenticator.databinding.HomeFragmentBinding
import com.vvieira.appauthenticator.domain.model.Categoria
import com.vvieira.appauthenticator.domain.model.MaisVendidos
import com.vvieira.appauthenticator.recyclerviews.adapter.BannersCarouselAdapter
import com.vvieira.appauthenticator.recyclerviews.adapter.CategoriasAdapter
import com.vvieira.appauthenticator.recyclerviews.adapter.MaisVendidosAdapter

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var adapter: BannersCarouselAdapter
    private val images = listOf(
        R.drawable.vidros,
        R.drawable.aluminio,
        R.drawable.cadastro_background
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = BannersCarouselAdapter(images)
        binding.viewPager2.adapter = adapter
        setupIndicators()
        setCurrentIndicator(0)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
//        setupActionBar()
//        setupBottomNavigation()
        setupRecyclerView()
        setupMaisVendidos()
    }

    private fun setupBanners() {



    }

//    private fun setupBottomNavigation() {
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when (it.itemId) {
//                R.id.inicio -> {
//                    Log.d("teste", "inicio")
//                }
//
//                R.id.favoritos -> {
//                    Log.d("teste", "inicio")
//                }
//
//                R.id.carrinho -> {
//                    Log.d("teste", "inicio")
//                }
//
//                R.id.perfil -> {
//                    Log.d("teste", "inicio")
//                }
//
//                R.id.pesquisar -> {
//                    Log.d("teste", "inicio")
//                }
//
//                else -> {}
//            }
//            true
//        }
//    }

    private fun setupRecyclerView() {
        val categoriasList = listOf(
            Categoria("Vidros", R.drawable.vidros),
            Categoria("Materiais", R.drawable.aluminio),
            Categoria("Orçamentos", R.drawable.orcamento),
            Categoria("Outros", R.drawable.outros),
            Categoria("Outros2", R.drawable.outros),
            Categoria("Outros3", R.drawable.outros),
            Categoria("Outros4", R.drawable.outros),
        )

        val categoriasAdapter = CategoriasAdapter(requireContext(), categoriasList)
        binding.recyclerCategorias.adapter = categoriasAdapter
    }

    private fun setupMaisVendidos() {
        val maisVendidosList = listOf(
            MaisVendidos(R.drawable.vidros, 1990.0,
                "Vidros", "Teste nome", 1),
            MaisVendidos(R.drawable.vidros, 990.0,
                "Vidros", "Teste nome2", 2),
            MaisVendidos(R.drawable.vidros, 100.0,
                "Vidros", "Teste nome3", 3),
            MaisVendidos(R.drawable.vidros, 200.0,
                "Vidros", "Teste nome4", 4),
            )
        val maisVendidosAdapter = MaisVendidosAdapter(requireContext(), maisVendidosList)
        binding.recyclerMaisVendidos.adapter = maisVendidosAdapter
    }

    private fun setupIndicators() {
        val indicators = arrayOfNulls<ImageView>(images.size)
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)

        for (i in images.indices) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.indicator_inactive // Crie um drawable para o indicador inativo
                )
            )
            indicators[i]?.layoutParams = layoutParams
            binding.indicatorLayout.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(position: Int) {
        val childCount = binding.indicatorLayout.childCount
        for (i in 0 until childCount) {
            val imageView = binding.indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_active // Crie um drawable para o indicador ativo
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
            }
        }
    }

//    private fun setupActionBar() {
//        val toolbar = binding.toolbar
//        // Para usar a Toolbar dentro de um Fragment, precisamos do AppCompatActivity hospedeiro
//        val activity = requireActivity() as AppCompatActivity
//        activity.setSupportActionBar(toolbar)
//        activity.supportActionBar?.title = ""
//        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        activity.setSupportActionBar(toolbar)
//
//        drawerLayout = binding.drawerLayout
//        navView = binding.navView
//        toggle = ActionBarDrawerToggle(activity, drawerLayout, R.string.open, R.string.close)
//        drawerLayout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        navView.setNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.item_1 -> {
//                    drawerLayout.closeDrawer(navView)
//                    true
//                }
//
//                else -> {
//                    drawerLayout.closeDrawer(navView)
//                    true
//                }
//            }
//            true
//        }
//
//        drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
//            override fun onDrawerStateChanged(newState: Int) {
//                super.onDrawerStateChanged(newState)
//                if (newState == DrawerLayout.STATE_IDLE && !drawerLayout.isDrawerOpen(navView)) {
//                    drawerLayout.closeDrawer(navView)
//                }
//            }
//        })
//
//        // A lógica do OnBackPressedCallback precisa ser adaptada para o Fragment
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : androidx.activity.OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
//                    drawerLayout.closeDrawer(GravityCompat.START)
//                } else {
//                    requireActivity().onBackPressedDispatcher.onBackPressed()
//                }
//            }
//        })
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}