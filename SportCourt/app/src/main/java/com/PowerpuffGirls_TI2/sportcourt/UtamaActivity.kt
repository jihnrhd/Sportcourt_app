package com.PowerpuffGirls_TI2.sportcourt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_utama.*
import kotlinx.android.synthetic.main.app_bar_main.*

class UtamaActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var areapeminjamanFragment: AreapeminjamanFragment
    lateinit var keranjangFragment: KeranjangFragment
    lateinit var transaksiFragment: TransaksiFragment
    lateinit var profilFragment: ProfilFragment
    lateinit var aboutusFragment: AboutusFragment
    lateinit var logoutFragment: LogoutFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_utama)



        setSupportActionBar(toolBar)
        val actionBar = supportActionBar
        actionBar?.title = "Sport Court"

        val drawerToggle : ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolBar,
            (R.string.open),
            (R.string.close)
        ) {
        }
        drawerToggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener (this)

        /*areapeminjamanFragment = AreapeminjamanFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout,areapeminjamanFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()*/

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId){
            R.id.listlap -> {
                areapeminjamanFragment = AreapeminjamanFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,areapeminjamanFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.keranjang -> {
                keranjangFragment = KeranjangFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,keranjangFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.transaksi -> {
                transaksiFragment = TransaksiFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,transaksiFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.profil -> {
                profilFragment = ProfilFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,profilFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.aboutus -> {
                aboutusFragment = AboutusFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,aboutusFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
            R.id.logout -> {
                logoutFragment = LogoutFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frameLayout,logoutFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }

    }
}