package com.church.sdahymnal.ui.setting

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.church.sdahymnal.R
import com.church.sdahymnal.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAboutUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about_us)

        onClick()

    }

    private fun onClick(){
        // phone
        binding.txtPhone1.setOnClickListener {
           phoneCall(it)
        }

        binding.txtPhone2.setOnClickListener {
            phoneCall(it)
        }

        binding.txtPhone3.setOnClickListener {
            phoneCall(it)
        }

        // email

        binding.txtEmail.setOnClickListener {
            sendEmail(((it) as TextView).text.toString())
        }

        // webiste

        binding.txtWebsite.setOnClickListener {
            openWebsite("https://www.adventistmm.org")
        }

        binding.container28Beliefs.setOnClickListener{
            openWebsite("https://www.adventist.org/beliefs/")
        }

        binding.imgFacebook.setOnClickListener {
            openFacebook()
        }

        binding.imgYoutube.setOnClickListener {
            openYoutube()
        }
    }

    private fun  openYoutube(){
        try{
            val i = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.youtube.com/channel/UCVhaudyEEH9NLp9AT7xzXvQ")
            }
            startActivity(i)
        }catch(e : Exception){
            Toast.makeText(this, "Couldn't find any app to handle the request!", Toast.LENGTH_SHORT).show()
        }

    }

    private fun openFacebook(){
        try{
            packageManager.getPackageInfo("com.facebook.katana", 0)
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/220334284975499"))
            startActivity(i)
        }catch(e : Exception){
            val i = Intent(Intent.ACTION_VIEW, Uri.parse("https//www.facebook.com/MyanmarUnionMission"))
            startActivity(i)
        }
    }


    private fun openWebsite(url : String){
        try{
            val website : Uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, website)
            if(intent.resolveActivity(this.packageManager)!=null){
                startActivity(intent)
            }
        }catch(e : java.lang.Exception){
            Toast.makeText(this, "Couldn't find any app to handle the request!", Toast.LENGTH_SHORT).show()
        }

    }


    private fun sendEmail(to : String) {
        val TO = arrayOf(to)
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, TO)
            putExtra(Intent.EXTRA_SUBJECT, "SDA HYMNAL")
        }
        val packageManager = this.packageManager
        if(packageManager?.let { emailIntent.resolveActivity(it) } != null){
            startActivity(emailIntent)
        }else{
            Toast.makeText(this, "Couldn't find any app to handle the request!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun phoneCall( view : View){
        val i = Intent(Intent.ACTION_DIAL)
        i.setData(Uri.parse("tel:"+"${((view) as TextView).getText()}"))
        startActivity(i)
    }

}