package com.example.zd5.Activity

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.zd5.DBCurrentUser.CurrentUser
import com.example.zd5.DBCurrentUser.DBHelperCurrentUser
import com.example.zd5.DBUser.DBHelperUser
import com.example.zd5.DBUser.User
import com.example.zd5.R
import com.example.zd5.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dataStore: SharedPreferences
    var prefs_name: String = "PrefersFile"
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var helperCurrentUser = DBHelperCurrentUser(this)
        helperCurrentUser.deleteCurrentUser()

        saveDBadmin("admin","123", "admin@gmail.com", "admin")
        //Установка значения в спиннер по умолчанию
        binding.spinnerEmailTags.setSelection(0)

        dataStore = getSharedPreferences(prefs_name, MODE_PRIVATE)
        getPreferencesData()

        binding.buttonSignIn.setOnClickListener()
        {
            var email: String = "${binding.edittextEmail.text.trim().toString()}${binding.spinnerEmailTags.selectedItem.toString()}"
            var email_saveSP: String = binding.edittextEmail.text.trim().toString()
            var login: String = binding.edittextLogin.text.trim().toString()
            var password: String = binding.edittextPassword.text.trim().toString()
            var selectionID: Int = binding.spinnerEmailTags.selectedItemPosition

            var checkFields: Boolean = CheckFieldsInfo(email,login,password)

            if (checkFields)
            {
                var helper = DBHelperUser(this)
                var result = helper.getUser("${login}", "${email}")
                if (result==true) {
                    if (binding.checkboxRememberMe.isChecked) {
                        setPreferencesData(email_saveSP, login, password, selectionID)
                        var toast = Toast.makeText(this, R.string.savedInfo, Toast.LENGTH_SHORT)
                        toast.show()
                        var role = getRolefromUserDB(login)
                        if (role.isNotEmpty()) {
                            helperCurrentUser.addCurrentUser(CurrentUser("${login}", "${role}"))
                            setNextPage()
                        }
                    } else {
                        dataStore.edit().clear().apply()
                        setNextPage()
                    }
                }
                else
                {
                    var toast = Toast.makeText(this,R.string.errorSignin, Toast.LENGTH_SHORT)
                    toast.show()
                }
            }
            else
            {
                var toast = Toast.makeText(this, R.string.errorInput, Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun getRolefromUserDB(login: String) : String
    {
        var role: String = ""
        var helperUser = DBHelperUser(this)
        var result = helperUser.getUserRole("${login}","admin")
        if (!result)
        {
            result = helperUser.getUserRole("${login}","student")
            if (!result)
            {
                result = helperUser.getUserRole("${login}","teacher")
                if (result)
                {
                    role = "teacher"
                    return role
                }
            }
            else
            {
                role = "student"
                return role
            }
        }
        else
        {
            role = "admin"
            return role
        }
        return role
    }
    private fun saveDBadmin(login: String, password: String, email: String, role:String)
    {
        var helper = DBHelperUser(this)
        var result = helper.getUser(login, email)
        if (result == false)
        {
            var user = User("${login}","${password}", "${email}", "${role}")
            helper.addUser(user)
        }
    }

    private fun setNextPage(){
        var intent = Intent(this, NavigationActivity::class.java)
        startActivity(intent)
    }

    private fun getPreferencesData() {
        dataStore = getPreferences(MODE_PRIVATE);
        binding.edittextEmail.setText(dataStore.getString("email", ""))
        binding.edittextLogin.setText(dataStore.getString("login", ""))
        binding.edittextPassword.setText(dataStore.getString("password", ""))
        binding.spinnerEmailTags.setSelection(dataStore.getInt("selectionID",0))
    }

    private fun setPreferencesData(email: String, login: String, password: String, selectionID: Int) {
        dataStore = getPreferences(MODE_PRIVATE)
        val ed: SharedPreferences.Editor = dataStore.edit()
        ed.putString("email", email)
        ed.putString("login", login)
        ed.putString("password", password)
        ed.putInt("selectionID", selectionID)
        ed.apply()
    }

    fun CheckFieldsInfo(email: String, login: String, password: String): Boolean
    {
        if (email.isNotEmpty() && login.isNotEmpty() && password.isNotEmpty())
        {
            if (email.length>=4 && login.length>=4 && password.length>=3)
            {
                return true
            }
        }
        return false
    }
    fun onClickTextView(view:View)
    {
        var toast = Toast.makeText(this, R.string.noAccountMessage, Toast.LENGTH_LONG)
        toast.show()
    }
}