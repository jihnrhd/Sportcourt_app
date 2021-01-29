package com.PowerpuffGirls_TI2.sportcourt.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.PowerpuffGirls_TI2.sportcourt.MainActivity
import com.PowerpuffGirls_TI2.sportcourt.R
import com.PowerpuffGirls_TI2.sportcourt.model.Users
import com.PowerpuffGirls_TI2.sportcourt.ui.setting.SettingActivity
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var users: Users
    private lateinit var tvUserName: TextView
    private lateinit var tvUserPhone: TextView
    private lateinit var ivProfile: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_keranjang,
                R.id.nav_transaksi,
                R.id.nav_profile,
                R.id.nav_about
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val navLogOutItem: MenuItem = navView.menu.findItem(R.id.logout)
        navLogOutItem.setOnMenuItemClickListener {
            logout()
            false
        }

        val headerView: View = navView.getHeaderView(0)
        tvUserName = headerView.findViewById(R.id.tvUsername)
        tvUserPhone = headerView.findViewById(R.id.tvEmailUser)
        ivProfile = headerView.findViewById(R.id.imageView)

        getData()
    }

    private fun getData() {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("id", userID)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        users = Users(
                            document.getString("id"),
                            document.getString("email"),
                            document.getString("image_url"),
                            document.getString("username"),
                        )

                        tvUserName.text = users.username
                        tvUserPhone.text = users.email
                        Glide.with(this)
                            .load(users.image_url)
                            .into(ivProfile)

                    }
                } else {
                    Log.d("Dashboard", "Error getting documents.", task.exception)
                }
            }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(this@DashboardActivity, SettingActivity::class.java))
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@DashboardActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}