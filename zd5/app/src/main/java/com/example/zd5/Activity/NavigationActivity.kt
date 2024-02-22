package com.example.zd5.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.zd5.DBCurrentUser.DBHelperCurrentUser
import com.example.zd5.DBSpecialty.DBHelperSpecialty
import com.example.zd5.DBSpecialty.Specialty
import com.example.zd5.DBUniversity.DBHelperUniversites
import com.example.zd5.DBUniversity.University
import com.example.zd5.DBUniversity.UniversityAdapter
import com.example.zd5.DBUser.DBHelperUser
import com.example.zd5.R
import com.example.zd5.databinding.ActivityNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONObject

class NavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigationBinding
    private lateinit var login: String
    private lateinit var role: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = androidx.navigation.ui.AppBarConfiguration(
            setOf(
                R.id.navigation_university, R.id.navigation_teacher, R.id.navigation_students, R.id.navigation_specialty)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        definitionRole()

        if (role == "admin") {
            var toast = Toast.makeText(this, R.string.adminMessage, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun definitionRole()
    {
        var helperCurrentUser = DBHelperCurrentUser(this)
        var array = helperCurrentUser.getCurrentUser()
        array.forEach {
            login = "${it.login.toString()}"
            role = "${it.role.toString()}"
        }
    }
    fun getAllSpec(): ArrayList<Specialty>
    {
        var helper = DBHelperSpecialty(this)
        return helper.readAllSpec()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_top_admin, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menuItemExit -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Do you really want to leave?")
                    .setCancelable(true)
                    .setPositiveButton("OK")
                    { _, _ ->
                        var intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel")
                    { _, _ ->
                    }
                val dialog = builder.create()
                dialog.show()
            }
            R.id.addStudent -> {
                if (role == "admin")
                {
                    var array = getAllSpec()
                    if (array.size>=1) {
                        var intent = Intent(this, AddStudentActivity::class.java)
                        startActivity(intent)
                    }
                    else
                    {
                        var toast = Toast.makeText(this, R.string.errorNoSpec, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                else
                {
                    var toast = Toast.makeText(this, R.string.errorClickMenu, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            R.id.addSpecialty -> {
                if (role == "admin")
                {
                    var intent = Intent(this, AddSpecialtyActivity::class.java)
                    startActivity(intent)
                }
                else
                {
                    var toast = Toast.makeText(this, R.string.errorClickMenu, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            R.id.addTeacher -> {
                if (role == "admin") {
                    var array = getAllSpec()
                    if (array.size >= 1) {
                        var intent = Intent(this, AddTeacherActivity::class.java)
                        startActivity(intent)
                    } else {
                        var toast = Toast.makeText(this, R.string.errorNoSpec, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
                else
                {
                    var toast = Toast.makeText(this, R.string.errorClickMenu, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}