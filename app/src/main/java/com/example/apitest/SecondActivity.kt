package com.example.apitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.apitest.databinding.ActivitySecondBinding

lateinit var binding2: ActivitySecondBinding
class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding2 = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding2.root)
        val data = intent.extras?.getString("text")
        binding2.edt2.setText(data)
    }

    fun openFragment(){
        val myfragment = First()
        val bundle = Bundle()
        bundle.putString("text", intent.extras?.getString("text").toString())
        myfragment.arguments = bundle

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame1, myfragment)
            .addToBackStack(null)
            .commit()
    }
    fun onBackButtonClick(view: View){
        var i = Intent()
        i.putExtra("text", binding2.edt2.text.toString())

        setResult(RESULT_OK, i)
        finish()
    }

    fun onAddButtonClick(view: View){
        openFragment()
    }


}