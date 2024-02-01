package com.acdetorres.nidoregistration.screens

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acdetorres.nidoregistration.ActivityMainViewModel
import com.acdetorres.nidoregistration.R
import com.acdetorres.nidoregistration.clearText
import com.acdetorres.nidoregistration.databinding.ActivityMainBinding
import com.acdetorres.nidoregistration.screens.fragments.FragmentViewPager
import com.acdetorres.nidoregistration.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.text.DateFormat.getDateInstance
import java.util.Date


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    val binding : ActivityMainBinding? get() = mBinding

    private var mBinding : ActivityMainBinding? = null

    private val viewModel : ActivityMainViewModel by viewModels()

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun storeImage(image: Bitmap) {
        try {
            val timeStamp: String = getDateInstance().format(Date())
            val mImageName = "MI_$timeStamp.jpg"
            val fos = openFileOutput(mImageName, MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getStoreImage(fileName : String) {
        try {
            val img =  this@MainActivity.filesDir.toURI().toString() + "/" + fileName

            val file = File(img)
        }catch (e : Exception) {

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.viewModelScope.launch(IO) {
            viewModel.getRecordsCount()
            viewModel.getAllRecords()
        }

        binding?.run {

//            etRelationship.setText("ABCD")
//
//
//            etBrandMilk.setText("ABCD")
//
//            etNumChild.setText("ABCD")
//
//            etEmailAddress.setText("ABCD")
//
//
//            etLastName.setText("ABCD")
//
//            etFirstName.setText("ABCD")



            viewModel.error.observe(this@MainActivity) {
                if (it!= null) {
                    Toast.makeText(this@MainActivity, "$it", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.count.observe(this@MainActivity) {
                if (it != null) {
                    tvSync.text = "Sync $it Records"
                }
            }

            viewModel.successSubmit.observe(this@MainActivity) {
                if (it != null) {
                    DialogNotice("Successfully submitted registrant", "Success", object : DialogNotice.OnSuccessListener {
                        override fun onSuccess() {
                            startActivity(Intent(this@MainActivity, MainActivity::class.java))
                            finish()
                        }

                    }).show(supportFragmentManager, null)
                }
            }


            rbAgree.setOnClickListener {
                viewModel.didAgree = rbAgree.isChecked
            }



            etDob.doOnTextChanged { text, start, before, count ->
                if (text != null) {
                    Log.d("DO ON TEXT", "before $before start $start count $count")
                    when (text.length) {
                        2 -> {
                            if (start != 2) {
                                etDob.setText("$text/")
                                etDob.setSelection(etDob.string().length)
                            }
                        }
                        5 -> {
                            if (start != 5) {
                                etDob.setText("$text/")
                                etDob.setSelection(etDob.string().length)
                            }
                        }
                    }
                }

            }

            val list = ArrayList<String>()

            for (i in 0..99)  {
                list.add("+$i")
            }



            val arrayAdapter =ArrayAdapter (this@MainActivity, android.R.layout.simple_dropdown_item_1line, list)

            sContact.adapter = arrayAdapter

            sContact.setSelection(63)


            tvSignature.setOnClickListener {
                val onSave : (Bitmap) -> Unit = { img ->
                    fullScreen()
                    viewModel.didSign = true
                    viewModel.signImage = img
                }
                val onDelete : () -> Unit = {
                    viewModel.didSign = false
                }
                DialogSignature(onSave, onDelete).show(supportFragmentManager, "")
            }


            val ages = ArrayList<String>(4)




            tvResetAl.setOnClickListener {
                resetFields()
            }

            tvSubmit.setOnClickListener {
                ages.clear()
                llAgesChildren.children.forEach { ageEt ->
                    val age = (ageEt as EditText)
                    ages.add(age.text.toString())
                    Log.d("Ages:", ages.toString())
                }

                viewModel.submitForm(
                    etRelationship.string(),
                    etFirstName.string(),
                    etLastName.string(),
                    etDob.string(),
                    etContactNumber.string(),
                    etEmailAddress.string(),
                    etNumChild.string(),
                    ages,
                    etBrandMilk.string(),
                    System.currentTimeMillis().toString()
                )
            }

            vp.currentItem = 2

            var didAgree = false

            val onAgree : (Boolean) -> Unit = { it ->

                didAgree = it
                vp.currentItem = 2

            }

            val dontSkip : () -> Unit = {
                vp.currentItem = 3
            }

            vp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {

                    if (position == 1) {
                        didAgree = false
                    }
                    if (!didAgree && position > 1) {
                        vp.currentItem = 1
                        Toast.makeText(this@MainActivity, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show()
                    }

                    if (position > 4) {
                        drawer.visibility = View.VISIBLE
                    }

                    if (position == 6) {
                        vp.visibility = View.GONE
                    }
//                    super.onPageSelected(position)

                }


//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            })


            vp.adapter = object : FragmentStateAdapter (this@MainActivity) {

                override fun getItemCount(): Int {
                    return 7
                }

                override fun createFragment(position: Int): Fragment {


                    return FragmentViewPager(position, onAgree, dontSkip)


                }
            }


            val formsAgesThemeContext = ContextThemeWrapper(this@MainActivity, R.style.forms_ages)



            val linearLayoutParams = LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1.0f)

            linearLayoutParams.setMargins(0, 0, 10, 0)

            val scale = resources.displayMetrics.density
            val dpAsPixels = (3 * scale + 0.5f)


//            vp.visibility= View.GONE
//            drawer.visibility = View.VISIBLE

            val numOfChild = MutableLiveData(1)

            etNumChild.doOnTextChanged { text, start, before, count ->
                if (text?.isEmpty() == true) {
                    numOfChild.value = 1
                } else {
                    numOfChild.value = text.toString().toInt()
                }
            }

            numOfChild.observe(this@MainActivity) {
                val childrenSize = llAgesChildren.children.count()

                llAgesChildren.removeAllViews()

                for (i in 1..it) {
                    val newEt = EditText(formsAgesThemeContext, null, R.style.forms_ages)

                    newEt.focusable = View.FOCUSABLE

                    newEt.isEnabled = true

                    newEt.isFocusable = true

                    newEt.layoutParams = linearLayoutParams

                    newEt.isFocusableInTouchMode = true

                    newEt.inputType = InputType.TYPE_CLASS_NUMBER

                    newEt.setPadding(10, 10, 10,10)

                    llAgesChildren.addView(newEt)
                }
            }

            for (i in 0..5) {
//                val newEt = EditText(formsAgesThemeContext, null, R.style.forms_ages)
//
//                newEt.focusable = View.FOCUSABLE
//
//                newEt.isEnabled = true
//
//                newEt.isFocusable = true
//
//                newEt.layoutParams = linearLayoutParams
//
//                newEt.isFocusableInTouchMode = true
//
//
//                newEt.setPadding(20, 20, 0,0)
//
//
//
//                llAgesChildren.addView(newEt)
            }
//            llAgesChildren.removeViewAt(llAgesChildren.children.count() -1)





        }

        fullScreen()
    }

    private fun resetFields() {

        binding?.run {
            rbAgree.isChecked = false
            viewModel.didSign = false
            viewModel.didAgree = false
            viewModel.signImage = null
            rgLegalGuardian.children.forEach {
                (it as RadioButton).isChecked = false
            }
            etRelationship.clearText()
            etFirstName.clearText()
            etLastName.clearText()
            etDob.clearText()
            etContactNumber.clearText()
            etEmailAddress.clearText()
            etNumChild.clearText()
            llAgesChildren.children.forEach {
                (it as EditText).clearText()
            }
            etBrandMilk.clearText()
            viewModel.didSign = false
        }
    }


    override fun onResume() {
        super.onResume()
        fullScreen()

    }

    private fun fullScreen() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.also {
                it.hide(WindowInsets.Type.statusBars())
                it.hide(WindowInsets.Type.navigationBars())
            }
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}