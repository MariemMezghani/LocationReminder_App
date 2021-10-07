package com.github.mariemmezghani.locationreminder.locationreminders

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.firebase.ui.auth.AuthUI
import com.github.mariemmezghani.locationreminder.R
import com.github.mariemmezghani.locationreminder.authentication.AuthenticationActivity
import com.github.mariemmezghani.locationreminder.authentication.LoginViewModel
import kotlinx.android.synthetic.main.activity_reminders.*

class RemindersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminders)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
                return true
            }R.id.logout ->{
            AuthUI.getInstance().signOut(this)
            intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}