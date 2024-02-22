package com.example.zd5.Activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.zd5.DBSpecialty.DBHelperSpecialty
import com.example.zd5.DBStudent.DBHelperStudent
import com.example.zd5.DBStudent.Student
import com.example.zd5.DBUser.DBHelperUser
import com.example.zd5.DBUser.User
import com.example.zd5.R
import com.example.zd5.databinding.ActivityAddStudentBinding
import java.util.*

class AddStudentActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddStudentBinding
    val arrayItem: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityAddStudentBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setCalendarDate() // настройка datepicker
        updateSpecSpinner() // добавление всех специальностей в spinner
        binding.spinnerSpecialty.setSelection(0) //выбор spinner по умолчанию
        binding.buttonAdd.setOnClickListener()
        {
            clickAddStudent()
        }//обработка нажатия на кнопку добавления
    }
    fun setCalendarDate()
    {
        val calendarMin = Calendar.getInstance()
        calendarMin.set(1980, 0, 1)
        val minDate = calendarMin.timeInMillis
        binding.dateBirthday.minDate = minDate

        val calendarMax = Calendar.getInstance()
        calendarMax.set(2004, 0, 1)
        val maxDate = calendarMax.timeInMillis
        binding.dateBirthday.maxDate = maxDate
    }
    fun updateSpecSpinner()
    {
        var helper = DBHelperSpecialty(this)
        var array = helper.readAllSpec()

        array.forEach {
            var string = "${it.title.toString()}"
            arrayItem.add(string.toString())
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayItem)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSpecialty.adapter = adapter
    }
    fun clickAddStudent()
    {
        var login = binding.edittextLogin.text.trim().toString()
        var password = binding.edittextPassword.text.trim().toString()
        var email_total = "${binding.edittextEmail.text.trim().toString()}${binding.spinnerEmailTags.selectedItem.toString()}"
        var email = binding.edittextEmail.text.trim().toString()
        var fullname = binding.edittextfullname.text.trim().toString()
        var point = binding.edittextPoint.text.trim().toString()
        var date = getDate()
        var specialty = binding.spinnerSpecialty.selectedItem.toString()

        var result = checkInfoFields(login, password, email, fullname, point)
        if (result)
        {
            var resultPropertyDB = checkPropertyDB(login, email_total, specialty)
            if (resultPropertyDB)
            {
                if (getStudentLoginExcist("$login") == true)
                {
                    var resultCheckCurrentInfoAddingStudent = checkCurrentInfoAddingStudent(login, fullname, date, point, password, email_total)
                        if (resultCheckCurrentInfoAddingStudent)
                        {
                        addtoDBStudent("${login}", "${password}", "${email_total}",
                            "${fullname}", "${date}", "${point}", "${specialty}")
                        binding.edittextEmail.text.clear()
                        binding.edittextLogin.text.clear()
                        binding.edittextPassword.text.clear()
                        binding.edittextfullname.text.clear()
                        binding.edittextPoint.text.clear()
                        var toast = Toast.makeText(this, R.string.add, Toast.LENGTH_SHORT)
                        toast.show()
                    } else {
                        var toast =
                            Toast.makeText(this, R.string.errorAddExistSyudent, Toast.LENGTH_SHORT)
                        toast.show()
                        changingExistStudentInfo("${login}")
                    }
                }
                else
                {
                    var helperUser = DBHelperUser(this)
                    var check = helperUser.getUser("$login")
                    if (!check) {
                        addtoDBStudent("${login}", "${password}", "${email_total}",
                            "${fullname}", "${date}", "${point}", "${specialty}")
                        addtoDBUser("${login}", "${password}", "${email_total}")
                        binding.edittextEmail.text.clear()
                        binding.edittextLogin.text.clear()
                        binding.edittextPassword.text.clear()
                        binding.edittextfullname.text.clear()
                        binding.edittextPoint.text.clear()
                        var toast = Toast.makeText(this, R.string.add, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                    else
                    {
                        var toast = Toast.makeText(this, R.string.errorAddExistUser, Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
            else
            {
                var toast = Toast.makeText(this, R.string.errorAddStudent, Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        else
        {
            var toast = Toast.makeText(this, R.string.errorInput, Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun getStudentLoginExcist(login: String): Boolean
    {
        var helper = DBHelperStudent(this)
        return helper.getStudent("$login")
    }
    fun changingExistStudentInfo(login: String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Substitute values from the database?")
            .setCancelable(true)
            .setPositiveButton("OK")
            { _, _ ->
                var helper = DBHelperStudent(this)
                var arrayStudentInfo = helper.getStudentLogin(login)
                var fullname: String = ""
                var date: String = ""
                var point: String = ""
                var email: String = ""
                var password: String = ""
                arrayStudentInfo.forEach {
                    fullname = "${it.fullname}"
                    date = "${it.date}"
                    point = "${it.point}"
                    email = "${it.email}"
                    password = "${it.password}"
                }
                var splitEmail = email.split("@")
                if (splitEmail[1]=="mail.ru")
                {
                    binding.spinnerEmailTags.setSelection(0)
                }
                else
                {
                    binding.spinnerEmailTags.setSelection(1)
                }
                binding.edittextPassword.setText("${password}")
                binding.edittextfullname.setText("${fullname}")
                binding.edittextPoint.setText("${point}")
                binding.edittextEmail.setText("${splitEmail[0]}")
                val splitDate = date.split(".")
                var day = splitDate[0].toInt()
                var month = splitDate[1].toInt()-1
                var year = splitDate[2].toInt()

                binding.dateBirthday.init(year, month, day, null)
            }
            .setNegativeButton("Cancel")
            { _, _ ->
            }
        val dialog = builder.create()
        dialog.show()
    }
    fun checkCurrentInfoAddingStudent(login: String, fullname: String, date:String, point: String, password: String, email:String) : Boolean
    {
        var helper = DBHelperStudent(this)
        var result = helper.getStudent("$login")
        if (result)
        {
            var arrayStudentInfo = helper.getStudentLogin("$login")
            var fullnameStudentInfo: String = ""
            var dateStudentInfo: String = ""
            var pointStudentInfo: String = ""
            var emailStudentInfo: String = ""
            var passwordStudentInfo: String = ""
            arrayStudentInfo.forEach {
                fullnameStudentInfo = "${it.fullname.toString()}"
                dateStudentInfo = "${it.date.toString()}"
                pointStudentInfo = "${it.point.toString()}"
                emailStudentInfo = "${it.email}"
                passwordStudentInfo = "${it.password}"
            }
            if (fullnameStudentInfo==fullname && dateStudentInfo==date && pointStudentInfo == point
                && emailStudentInfo == email && passwordStudentInfo == password)
            {
                return true
            }
        }
        return false
    }

    fun addtoDBStudent(login: String,password: String,email_total: String,fullname: String,date: String,point: String,specialty:String)
    {
        var helper = DBHelperStudent(this)
        var student = Student("${login}", "${password}", "${email_total}",
            "${fullname}", "${date}", "${point}", "${specialty}")
        helper.addStudent(student)
    }
    fun addtoDBUser(login: String, password: String, email_total: String)
    {
        var user = User("${login}", "${password}", "${email_total}","student")
        var helperUser = DBHelperUser(this)
        helperUser.addUser(user)
    }

    fun checkPropertyDB(login:String, email_total: String, spec: String): Boolean
    {
        var helper = DBHelperStudent(this)
        var result = helper.getStudent(login, email_total, spec)
        if (result)
        {
            return false
        }
        return true
    }
    fun checkInfoFields(login:String, password:String, email:String, fullname:String, point: String):Boolean
    {
        if (login.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()
            && fullname.isNotEmpty())
        {
            if (binding.spinnerSpecialty.selectedItem!=0) {
                if (login.length >= 4 && password.length >= 3 &&
                    email.length >= 4 && fullname.length >= 10) {
                    try
                    {
                        var pointDouble = point.toDouble()
                        if (pointDouble >= 2.0 && pointDouble <= 5.0) {
                            return true
                        }
                    } catch (e: Exception) {
                        return false
                    }
                }
            }
        }
        return false
    }

    fun getDate(): String
    {
        var stringDate: String = ""
        var calendar = binding.dateBirthday
        var day = calendar.dayOfMonth
        var month = calendar.month + 1
        var year = calendar.year
        stringDate = "$day.$month.$year"
        return stringDate
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_top_student, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menuItemDeleteAll -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Clear database?")
                    .setCancelable(true)
                    .setPositiveButton("OK")
                    { _, _ ->
                        var helper = DBHelperStudent(this)
                        var result = helper.deleteAllStudents()

                        var helperUser = DBHelperUser(this)
                        helperUser.deleteAllRole("student")
                        if (result)
                        {
                            var toast = Toast.makeText(this, R.string.cleared, Toast.LENGTH_SHORT)
                            toast.show()
                        }
                        else
                        {
                            var toast = Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                    .setNegativeButton("Cancel")
                    { _, _ ->
                    }
                val dialog = builder.create()
                dialog.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}