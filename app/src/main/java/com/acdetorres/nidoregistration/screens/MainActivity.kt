package com.acdetorres.nidoregistration.screens

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acdetorres.nidoregistration.ActivityMainViewModel
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
                            resetFields()
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

            vp.registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (!didAgree && position > 1) {
                        vp.currentItem = 1
                        Toast.makeText(this@MainActivity, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show()
                    }
//                    super.onPageSelected(position)

                }


//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            })


            vp.adapter = object : FragmentStateAdapter (this@MainActivity) {

                override fun getItemCount(): Int {
                    return 3
                }

                override fun createFragment(position: Int): Fragment {


                    return FragmentViewPager(position, onAgree)


                }
            }





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