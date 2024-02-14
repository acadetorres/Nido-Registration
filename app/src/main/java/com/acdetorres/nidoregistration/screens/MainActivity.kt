package com.acdetorres.nidoregistration.screens

import android.app.ActionBar.LayoutParams
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
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
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.acdetorres.nidoregistration.ActivityMainViewModel
import com.acdetorres.nidoregistration.R
import com.acdetorres.nidoregistration.chooseDate
import com.acdetorres.nidoregistration.clearText
import com.acdetorres.nidoregistration.databinding.ActivityMainBinding
import com.acdetorres.nidoregistration.screens.fragments.FragmentViewPager
import com.acdetorres.nidoregistration.string
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    val binding : ActivityMainBinding? get() = mBinding

    private var mBinding : ActivityMainBinding? = null

    private val viewModel : ActivityMainViewModel by viewModels()

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    private fun storeImage(image: Bitmap, timeStamp: String) {
        try {
            val mImageName = "$timeStamp.jpg"
            val fos = openFileOutput(mImageName, MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.PNG, 60, fos)
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getStoreImage(fileName : String) {
        try {
            val img =  this@MainActivity.filesDir.toURI().toString() + "/" + fileName + ".jpg"

            val file = File(img)

            viewModel.savedImages.add(file)

            Log.d("FILE : ", file.path)
        }catch (e : Exception) {

        }
    }

    fun getAllImages(fileNames : List<String>) {
        fileNames.forEach {
            getStoreImage(it)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        viewModel.getRecordsCount()
        viewModel.getAllRecords()
        viewModel.getLocalAmbassadors()
        viewModel.getLoggedOnAmbassador()
        viewModel.getLocalProvinces()

//        val regionsString = this.assets.open("provinces.json").bufferedReader().use {
//            it.readText()
//        }
//
//        val provinces = Gson().fromJson(regionsString, Provinces::class.java)
//
//        val provincesList = provinces.map {
//            it.name
//        }.toMutableList()
//
//        provincesList.add(0, "")



//        Timber.e(regionsString)

//        Timber.e("Gumagana ba")




        binding?.run {

//            videoView.visibility = View.VISIBLE
//
//            videoView.setOnErrorListener { mediaPlayer, i, i2 ->
//
//                false
//            }
//
//            videoView.setOnCompletionListener {
////                startActivity(Intent(this@MainActivity, MainActivity::class.java))
////                finish()
//            }
//
//            val videoPath = "android.resource://" + packageName + "/" + R.raw.nido
//
//            videoView.setVideoURI(Uri.parse(videoPath))
//
//            videoView.start()


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

            val context = this@MainActivity

            viewModel.getRecordsCount()




            rbLegalParent.setOnClickListener {
                viewModel.didChooseParent = true
            }

            rbLegalInHousehold.setOnClickListener {
                viewModel.didChooseParent = true
            }

            viewModel.loading.observe(context) {

                Log.d("loading val", it.toString())
                if (it) {
                    loading.visibility = View.VISIBLE
                } else {
                    runOnUiThread {
                        Log.d("loading false", it.toString())
                        loading.visibility  = View.GONE
                    }
                }
            }

            viewModel.loggedOut.observe(context) {
                if (it) {
                    startActivity(Intent(context, LoginActivity::class.java))
                    finish()
                }
            }

            viewModel.provinces.observe(context) {
                if (it != null) {

                    val data = it.sortedBy {
                        it.provDesc
                    }

                    val provincesList = data.map {
                        it.provDesc
                    }.toMutableList()
                    provincesList.add(0, "")

                    sProvinces.adapter = ArrayAdapter (this@MainActivity, R.layout.simple_dropdown_item, provincesList)
                    sProvinces.selectedItemId
                    
                    sProvinces.onItemSelectedListener = object : OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            position: Int,
                            p3: Long
                        ) {
                            if (position == 0) {
                                viewModel.selectedProvince = "-1"
                            } else {
                                viewModel.selectedProvince = (data[position-1].id)
                                Log.d("data", data[position-1].toString())
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
//                            TODO("Not yet implemented")
                        }

                    }

//                    { adapterView, view, i, l ->
//                        viewModel.selectedProvince = (it[i-1].id).toString()
//                    }
                }
            }



            viewModel.forms.observe(context) { forms ->

                if (forms != null) {

//                    val images = forms.map {
//                        it.timeStamp.toString()
//                    }
//                        getAllImages(images)

                    Log.d("FORMS NOT NULL, ", forms.toString())


                    tvSync.setOnClickListener {
                        if (forms.isEmpty()) {
                            DialogNotice("No records to sync").show(supportFragmentManager, null)
                        }else {
                            viewModel.syncRecords(forms)
                        }
//                        Toast.makeText(this@MainActivity, "asuc", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            viewModel.successSync.observe(context) {
                if (it!= null) {
                    DialogNotice("Succesfully synced records").show(supportFragmentManager, null)
                    viewModel.deleteAllRecords()
                }
            }

            viewModel.loggedOnAmbassador.observe(context) {
                if (it != null) {
                    tvAdminName.text = "Hi ${it.fname}"
                    viewModel.userId = it.id
                }
            }


            viewModel.error.observe(context) {
                if (it!= null) {
                    Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
                }
            }

            viewModel.count.observe(context) {
                if (it != null) {
                    tvSync.text = "Sync $it Records"

                    tvLogout.setOnClickListener { v ->

                        if (it > 0) {

                            DialogNotice(
                                "You have ${it} records, you need to sync first or you will lose all records").show(supportFragmentManager, null)
                        } else {
                            viewModel.deleteLoggedOnAmbassador()
                        }
                    }
                }
            }

            viewModel.successSubmit.observe(this@MainActivity) {
                val isSuccessRegistration = it.first
                val timeStamp = it.second

                if (isSuccessRegistration) {

//                    viewModel.signImage?.let { it1 -> storeImage(it1, timeStamp) }

                    DialogNotice("Successfully submitted registrant", "Success", object : DialogNotice.OnSuccessListener {
                        override fun onSuccess() {

                            videoView.visibility = View.VISIBLE

                            lifecycleScope.launch(Main) {
                                videoView.setOnCompletionListener(object : OnCompletionListener {
                                    override fun onCompletion(mp: MediaPlayer) {
                                        startActivity(Intent(this@MainActivity, MainActivity::class.java))
                                        finish()
                                    }
                                })


                                val videoPath = "android.resource://" + packageName + "/" + R.raw.nido

                                videoView.setVideoURI(Uri.parse(videoPath))

                                videoView.start()
                            }


                        }

                    }).show(supportFragmentManager, null)
                }
            }


            rbAgree.setOnClickListener {
                viewModel.didAgree = rbAgree.isChecked
            }


            etDob.setOnClickListener {
                etDob.chooseDate(context)
            }

//            etDob.doOnTextChanged { text, start, before, count ->
//                if (text != null) {
//                    Log.d("DO ON TEXT", "before $before start $start count $count")
//                    when (text.length) {
//                        2 -> {
//                            if (start != 2) {
//                                etDob.setText("$text/")
//                                etDob.setSelection(etDob.string().length)
//                            }
//                        }
//                        5 -> {
//                            if (start != 5) {
//                                etDob.setText("$text/")
//                                etDob.setSelection(etDob.string().length)
//                            }
//                        }
//                    }
//                }
//
//            }

            val list = ArrayList<String>()

            for (i in 0..99)  {
                list.add("+$i")
            }



            val arrayAdapter =ArrayAdapter (this@MainActivity, android.R.layout.simple_dropdown_item_1line, list)

            sContact.adapter = arrayAdapter

            sContact.setSelection(63)

            rbConsent.setOnClickListener{
                viewModel.didSign = rbConsent.isChecked
            }

//            tvSignature.setOnClickListener {
//                val onSave : (Bitmap) -> Unit = { img ->
//                    fullScreen()
//                    viewModel.didSign = true
//                    viewModel.signImage = img
//                }
//                val onDelete : () -> Unit = {
//                    viewModel.didSign = false
//                }
//                DialogSignature(onSave, onDelete).show(supportFragmentManager, "")
//            }


            val ages = ArrayList<String>(4)




            tvResetAl.setOnClickListener {
                resetFields()
            }

            tvSubmit.setOnClickListener {
//                ages.clear()
//                llAgesChildren.children.forEach { ageEt ->
//                    val age = (ageEt as EditText)
//                    ages.add(age.text.toString())
//                    Log.d("Ages:", ages.toString())
//                }

                viewModel.submitForm(
                    etRelationship.string(),
                    etFirstName.string(),
                    etLastName.string(),
                    etDob.string(),
                    sContact.selectedItem.toString() + etContactNumber.string(),
                    etEmailAddress.string(),
                    etNumChild.string(),
                    etAgesChildren.string(),
                    etBrandMilk.string(),
                    System.currentTimeMillis().toString(),
                    rbConsent.isChecked,
                    sProvinces.selectedItem.toString(),
                    etCity.string(),
                    etBarangay.string()
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

                    if (position > 5) {
                        drawer.visibility = View.VISIBLE
                    }

                    if (position == 7) {
                        vp.visibility = View.GONE
                    }
//                    super.onPageSelected(position)

                }


//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

            })


            vp.adapter = object : FragmentStateAdapter (this@MainActivity) {

                override fun getItemCount(): Int {
                    return 8
                }

                override fun createFragment(position: Int): Fragment {


                    return FragmentViewPager(position, onAgree, dontSkip)


                }
            }

            etNumChild.doOnTextChanged { text, start, before, count ->

                etAgesChildren.clearText()

            }


            etAgesChildren.doOnTextChanged { text, start, before, count ->
                if (text != null) {
                    Log.d("DO ON TEXT", "before $before start $start count $count textLenght ${text.length} numchild empty ${etNumChild.string().isBlank()} ${etNumChild.string().isNotBlank()}")
                    if (etNumChild.string().isNotBlank()) {

                        if (text.length % 2 != 0 && before == 0 && (text.length / 2 != etNumChild.string()
                                .toInt())
                        ) {

                            etAgesChildren.setText("${text},")


                        }
                        if (text.length / 2 == etNumChild.string().toInt()) {
                            etAgesChildren.setText(text.dropLast(1))
                        }
                    } else {
                        if (etAgesChildren.string().isNotBlank()) {
                            etAgesChildren.setText("")
                        }
                    }


                    etAgesChildren.setSelection(etAgesChildren.text.length)

//                    if (c)
                }
            }


            val formsAgesThemeContext = ContextThemeWrapper(this@MainActivity, R.style.forms_ages)



            val linearLayoutParams = LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1.0f)

            linearLayoutParams.setMargins(0, 0, 10, 0)

            val scale = resources.displayMetrics.density
            val dpAsPixels = (3 * scale + 0.5f)


//            vp.visibility= View.GONE
//            drawer.visibility = View.VISIBLE

//            val numOfChild = MutableLiveData(1)

//            etNumChild.doOnTextChanged { text, start, before, count ->
//                if (text?.isEmpty() == true) {
//                    numOfChild.value = 1
//                } else {
//                    numOfChild.value = text.toString().toInt()
//                }
//            }

//            numOfChild.observe(this@MainActivity) {
//                val childrenSize = llAgesChildren.children.count()
//
//                llAgesChildren.removeAllViews()
//
//                for (i in 1..it) {
//                    val newEt = EditText(formsAgesThemeContext, null, R.style.forms_ages)
//
//                    newEt.focusable = View.FOCUSABLE
//
//                    newEt.isEnabled = true
//
//                    newEt.isFocusable = true
//
//                    newEt.layoutParams = linearLayoutParams
//
//                    newEt.isFocusableInTouchMode = true
//
//                    newEt.inputType = InputType.TYPE_CLASS_NUMBER
//
//                    newEt.setPadding(10, 10, 10,10)
//
//                    llAgesChildren.addView(newEt)
//                }
//            }

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

    fun convertDate(dateInMilliseconds: String, dateFormat: String?): String? {
        return DateFormat.format(dateFormat, dateInMilliseconds.toLong()).toString()
    }
}