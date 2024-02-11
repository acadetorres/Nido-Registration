package com.acdetorres.nidoregistration

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acdetorres.nidoregistration.dao.model.Form
import com.acdetorres.nidoregistration.dao.model.Ambassador
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ActivityMainViewModel @Inject constructor(private val repository: AppRepository, @ApplicationContext val context : Context) :ViewModel() {

    var didSign = false

    var didChooseParent = false

    var didAgree = false

    var signImage : Bitmap? = null

    val savedImages = ArrayList<File>()

    private val mError = MutableLiveData<String>()

    val error : LiveData<String> get() = mError

    val mSuccessSubmit = MutableLiveData<Pair<Boolean, String>>()

    val successSubmit : LiveData<Pair<Boolean, String>> get() = mSuccessSubmit


    private val mLoading = MutableLiveData<Boolean>()

    val loading : LiveData<Boolean> get() = mLoading

    val count : LiveData<Int> get() = mCount

    private val mCount = MutableLiveData<Int> ()

   private val mAmbassadors = MutableLiveData<List<Ambassador>?>()

    val ambassador : LiveData<List<Ambassador>?> get () = mAmbassadors

    val loggedOnAmbassador : LiveData<LoggedOnAmbassador> get () = mLoggedOnAmbassador

    private val mLoggedOnAmbassador = MutableLiveData<LoggedOnAmbassador> ()

    private val mForms = MutableLiveData<List<Form>?> ()

    val forms : MutableLiveData<List<Form>?> get() = mForms

    fun getLoggedOnAmbassador() {
        viewModelScope.launch(IO) {
            repository.getLoggedOnAmbassador().collect {
                mLoggedOnAmbassador.postValue(it.handleRepositoryFlowResponse())
            }
        }
    }
    fun insertLoggedOnAmbassador(ambassador: Ambassador) {
        viewModelScope.launch (IO){
            repository.insertLoggedOnAmbassador(ambassador).collect {
                it.handleRepositoryFlowResponse()
            }
        }
    }
    fun getAmbassadors() {
        viewModelScope.launch(IO) {
            repository.getAmbassadors().collect() {
                val data = it.handleRepositoryFlowResponse()

                data?.data?.let { ambassadors ->
                    if (ambassadors.isNotEmpty()) {
                        viewModelScope.launch(Main) {
                            mAmbassadors.value = ambassadors
                        }
                        repository.insertAmbassadors(ambassadors).collect { result ->
                            result.handleRepositoryFlowResponse()
                        }
                    }
                }
            }
        }
    }

    fun getLocalAmbassadors() {
        viewModelScope.launch(IO) {
            repository.getLocalAmbassadors().collect {

                val data = it.handleRepositoryFlowResponse()
                mAmbassadors.postValue(data)

                Log.d("ambassadors", ambassador.value.toString())
            }

        }
    }

    suspend fun deleteAllRecords() {
        repository.deleteAllRecords().collect {
            it.handleRepositoryFlowResponse()
        }
    }

    fun getAllRecords () {

        viewModelScope.launch(IO) {
            repository.getAllRecords().collect {
                val data = it.handleRepositoryFlowResponse()

                mForms.valueOnMain(data)

                data?.forEach {

                    Log.d("json name", it.firstName)
                    val json = Gson().fromJson<List<String>>(it.ages, ArrayList::class.java)
                    json.forEach {
                        Log.d("json foreach", it)
                    }
                }
            }
        }
    }

    fun submitForm(
        relationship: String,
        firstName: String,
        lastName: String,
        dob: String,
        contactNum: String,
        email: String,
        numOfChild: String,
        agesChildren: List<String>,
        currentBrand: String,
        timeStamp: String,
        parent : Boolean,
        province : String,
        city : String,
        barangay : String
    ) {
//        val form = Form(
//            System.currentTimeMillis(),
//            "a",
//            "b",
//            "c",
//            "d",
//            "e",
//            "f",
//            "g",
//            Gson().toJson(listOf("a","b")),
//            "j",
//            "k")

         val form = Form(
             System.currentTimeMillis(),
             relationship,
             firstName,
             lastName,
             dob,
             contactNum,
             email,
             numOfChild,
             Gson().toJson(agesChildren),
             currentBrand,
             timeStamp,
             parent,
             province,
             city,
             barangay)


        Log.d("Ages Prior Convert", agesChildren.toString())
        Log.d("JSON Convert", form.ages)

         val isFormValid = validateForm(form)


         if (isFormValid && didSign) {

//             val img = signImage?.let {
//                 storeImage(it, timeStamp)
//             }

//             if (!img.isNullOrEmpty()) {
                 viewModelScope.launch (IO){
                     repository.insertSubmit(form).collect {
                         val data = it.handleRepositoryFlowResponse()

                         if (data != null) {
                             getRecordsCount()
                             mSuccessSubmit.postValue(Pair(true, timeStamp))
                         }

                     }
                 }
//             }



         }

    }

    private fun storeImage(image: Bitmap, timeStamp: String) : String{
        try {
            val mImageName = "$timeStamp.jpg"
            val fos = context.openFileOutput(mImageName, AppCompatActivity.MODE_PRIVATE)
            image.compress(Bitmap.CompressFormat.PNG, 90, fos)
            fos.close()
            return mImageName
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getRecordsCount() {

        viewModelScope.launch(IO) {
            repository.getRecordsCount().collect {
                val resultCount = it.handleRepositoryFlowResponse()

                resultCount?.let { result ->
                    mCount.postValue(result)
                }

            }
        }
    }

    private fun validateForm(form: Form): Boolean {

        if (form.province.isEmpty()) {
            mError.value = "Please select a province"
            return false
        }

        if (form.city.isEmpty()) {
            mError.value = "Please input city"
            return false
        }

        if (form.barangay.isEmpty()) {
            mError.value = "Please input barangay"
            return false
        }

        if (!didChooseParent) {
            mError.value = "Please choose your relationship"
            return false
        }
        if (!didSign) {
            mError.value = "Needs to consent to submit"
            return false
        }

        if (!didAgree) {
            mError.value = "Please agree to data privacy consent"
            return false
        }

        if (form.firstName.isEmpty()) {
            mError.value = "First name is empty"
            return false
        }

        if (form.lastName.isEmpty()) {
            mError.value = "Last name is empty"
            return false
        }

        if (form.relationship.isEmpty()) {
            mError.value = "Relationship is empty"
            return false
        }

        if (form.dob.isEmpty()) {
            mError.value = "Date of birth is empty"
            return false
        }

        if (form.contactNum.isEmpty()) {
            mError.value = "Contact number is empty"
            return false
        }

        if (form.email.isEmpty()) {
            mError.value = "Email is empty"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(form.email).matches()) {
            mError.value = "Email is not valid format"
            return false
        }

        return true

    }

    private fun setLoadingState(loading : Boolean) {
            viewModelScope.launch(Main) {
                mLoading.value = (loading)
            }
        }

        private fun <T:Any> AppState<T>.handleRepositoryFlowResponse() : T? {
            when (this) {

                is AppState.Progress -> {
                    setLoadingState(this.isLoading)
                }
                is AppState.Success -> {
                    Log.d("handleRepositoryFlowResponse", data.toString())
                    return this.data
                }

                is AppState.Error -> {
                    mError.postValue(message)
                }
            }
            return null
        }


    fun <T:Any?> MutableLiveData<T>.valueOnMain(data : T) {
        viewModelScope.launch(Main) {
            this@valueOnMain.value = data
        }
    }


}