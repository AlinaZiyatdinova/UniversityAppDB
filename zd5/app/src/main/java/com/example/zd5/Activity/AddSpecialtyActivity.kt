package com.example.zd5.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.zd5.DBSpecialty.DBHelperSpecialty
import com.example.zd5.DBSpecialty.Specialty
import com.example.zd5.DBStudent.DBHelperStudent
import com.example.zd5.R
import com.example.zd5.databinding.ActivityAddSpecialtyBinding
import com.example.zd5.databinding.ActivityAddStudentBinding

class AddSpecialtyActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddSpecialtyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSpecialtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdd.setOnClickListener()
        {
            clickSpeciality()
        }
        binding.spinnerType.setSelection(0)
    }
    fun clickSpeciality()
    {
        var title = binding.edittextTitle.text.trim().toString()
        var type = binding.spinnerType.selectedItem.toString()
        var load = binding.edittextLoad.text.trim().toString()
        var result = checkInfo(title, type, load)
        if (result)
        {
            var presence = checkPresence(title)
            if (!presence) {
                var spec = Specialty(title, type, load)
                var helper = DBHelperSpecialty(this)
                helper.addSpecialty(spec)
                var toast = Toast.makeText(this, R.string.added, Toast.LENGTH_SHORT)
                toast.show()
                binding.edittextTitle.text.clear()
                binding.edittextLoad.text.clear()
                binding.spinnerType.setSelection(0)
            }
            else
            {
                var toast = Toast.makeText(this, R.string.errorAdd, Toast.LENGTH_SHORT)
                toast.show()
            }
        }
        else
        {
            var toast = Toast.makeText(this, R.string.errorInput, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
    fun checkInfo(title: String, type: String, load: String): Boolean
    {
        if (title.isNotEmpty() && type.isNotEmpty() && load.isNotEmpty())
        {
            if (title.length >= 5)
            {
                try
                {
                    var load_hour: Int = load.toInt()
                    if (load_hour<=200 && load_hour>=50)
                    {
                        return true
                    }
                }
                catch(e: java.lang.Exception)
                {
                    return false
                }
            }
        }
        return false
    }
    fun checkPresence(title: String): Boolean
    {
        var helper = DBHelperSpecialty(this)
        var result = helper.getSpec(title)
        if (result)
        {
            return true
        }
        return false
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_top_spec, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.clearDB -> {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Clear database?")
                    .setCancelable(true)
                    .setPositiveButton("OK")
                    { _, _ ->
                        var helperStudent = DBHelperStudent(this)
                        var resultDBSize = helperStudent.getDBSize()
                        if (!resultDBSize) {
                            var helper = DBHelperSpecialty(this)
                            var result = helper.deleteAll()
                            if (result) {
                                var toast =
                                    Toast.makeText(this, R.string.cleared, Toast.LENGTH_SHORT)
                                toast.show()
                            } else {
                                var toast = Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                        else
                        {
                            Toast.makeText(this,R.string.errorClearDBSpec , Toast.LENGTH_SHORT).show()
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