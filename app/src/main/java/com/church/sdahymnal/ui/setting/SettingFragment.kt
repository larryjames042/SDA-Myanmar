package com.church.sdahymnal.ui.setting

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.church.sdahymnal.R
import com.church.sdahymnal.databinding.FragmentSettingBinding
import com.church.sdahymnal.utils.Utils


class SettingFragment : Fragment() {

    private lateinit var binding : FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val isNightTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding.swDarkTheme.isChecked = isNightTheme == Configuration.UI_MODE_NIGHT_YES

        onClickListener()

    }

    private fun setDarkTheme(isDarkTheme : Boolean){

        if(isDarkTheme){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            Utils.saveToPreference(requireContext(), Utils.KEY_DARK_THEME, "true")
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Utils.saveToPreference(requireContext(), Utils.KEY_DARK_THEME, "false")
        }
    }



    private fun onClickListener(){
        binding.swDarkTheme.setOnCheckedChangeListener { compoundButton, b ->
            setDarkTheme(b)
        }

        binding.containerAboutUs.setOnClickListener {
            startActivity(Intent(activity, AboutUsActivity::class.java))
        }

        binding.containerAppInfo.setOnClickListener {

        }


        binding.containerShareApp.setOnClickListener {
            activity?.let { it1 -> shareApp(it1) }
        }

        binding.containerContactDeveloper.setOnClickListener {
            sendEmail()
        }
    }

    protected fun sendEmail() {
        val TO = arrayOf("larryjames042@gmail.com")
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, TO)
            putExtra(Intent.EXTRA_SUBJECT, "SDA HYMNAL")
        }
        val packageManager = activity?.packageManager
        if(packageManager?.let { emailIntent.resolveActivity(it) } != null){
            startActivity(emailIntent)
        }else{
            Toast.makeText(activity, "Nothing to handle this action!", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareApp(context: Context) {
        try{
            val appPackageName: String = context.getPackageName()
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(
                Intent.EXTRA_TEXT,
                "Check out the App at: https://play.google.com/store/apps/details?id=$appPackageName"
            )
            sendIntent.type = "text/plain"
            context.startActivity(sendIntent)
        }catch (e: Error){

        }

    }

    companion object {

    }
}