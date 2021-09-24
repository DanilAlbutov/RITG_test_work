package com.example.apitest

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.apitest.databinding.ActivityMainBinding
import java.util.zip.Inflater

lateinit var binding: ActivityMainBinding
lateinit var pref: SharedPreferences

private const val STORAGE = "Storage"
class MainActivity : AppCompatActivity() {

    private var launcher: ActivityResultLauncher<Intent>? = null

    private fun saveData(value: Int, key: String){
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun getData(key: String, defaultVal: Int): Int {
        return pref.getInt(key, defaultVal)
    }

    fun checkCount(){
        pref = this.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
        var launchCount: Int = getData("count", 0)
        launchCount++;
        saveData(launchCount, "count")
        binding.tvCount.text = "This application has been launched " + launchCount.toString() + " times"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkCount()

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result: ActivityResult ->
            if(result.resultCode == RESULT_OK)
            {
                binding.edtData1.setText(result.data?.getStringExtra("text").toString())
                
            }

        }

    }

    fun onNextActivityButtonClick(view: View){
        var intent = Intent(this, SecondActivity::class.java)
        intent.putExtra("text", binding.edtData1.text.toString())
        launcher?.launch(intent)
    }

    fun onClearButtonClick(view: View){
        var launchCount = 0
        saveData(launchCount, "count")
        binding.tvCount.text =
            "This application has been launched " + launchCount.toString() + " times"
    }

    fun onApiButtonClick(view: View){
        var intent = Intent(this, ApiActivity::class.java)
        startActivity(intent)
    }

}
