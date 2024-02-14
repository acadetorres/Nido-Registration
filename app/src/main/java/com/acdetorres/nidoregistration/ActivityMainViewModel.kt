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
import com.acdetorres.nidoregistration.dao.model.GetProvincesResponse
import com.acdetorres.nidoregistration.dao.model.LoggedOnAmbassador
import com.acdetorres.nidoregistration.dao.model.Meta
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

    var userId: String = ""

    var selectedProvince = "-1"

    var didSign = false

    var didChooseParent = false

    var didAgree = false

    var signImage : Bitmap? = null

    val savedImages = ArrayList<File>()

    private val mError = MutableLiveData<String>()

    val error : LiveData<String> get() = mError

    val mSuccessSync = MutableLiveData<Meta?>()

    val successSync : LiveData<Meta?> get() = mSuccessSync

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

    private val mProvinces = MutableLiveData<List<GetProvincesResponse.Province>?>()
    val provinces : LiveData<List<GetProvincesResponse.Province>?> get() = mProvinces

    val mLoggedOut = MutableLiveData<Boolean>(false)

    val loggedOut : LiveData<Boolean> get() = mLoggedOut


    fun deleteLoggedOnAmbassador() {
        viewModelScope.launch(IO) {
            repository.deleteLoggedOnAmbassador().collect {
                it.handleRepositoryFlowResponse {
                    mLoggedOut.valueOnMain(true)
                }
            }
        }
    }
    fun syncRecords(forms : List<Form>) {
        viewModelScope.launch(IO) {
            repository.syncRecords(userId, forms).collect {
                val meta = it.handleRepositoryFlowResponse()
                Log.d("SYNC", "THIS SYNC")
                if (meta != null) {
                    mSuccessSync.valueOnMain(meta)
                }
            }
        }
    }

    fun getLocalProvinces() {
        viewModelScope.launch(IO) {
            repository.getLocalProvince().collect {
                mProvinces.valueOnMain(it)
            }
        }
    }
    fun getProvinces() {
        viewModelScope.launch(IO) {
            repository.getProvinces().collect {
                val provinces = it.handleRepositoryFlowResponse()?.data

                if (provinces != null) {
                    repository.insertLocalProvinces(provinces).collect {
                        mProvinces.valueOnMain(provinces)
                    }
                }
            }
        }
    }
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

    fun deleteAllRecords() {
        viewModelScope.launch(IO) {
            repository.deleteAllRecords().collect {
                val onSuccess : (Unit) -> Unit = {
                    getRecordsCount()
                    getAllRecords()
                }

                it.handleRepositoryFlowResponse((onSuccess))

            }
        }
    }

    fun getAllRecords () {

        viewModelScope.launch(IO) {
            repository.getAllRecords().collect {
                val data = it.handleRepositoryFlowResponse()

                mForms.valueOnMain(data)

                data?.forEach {

                    Log.d("json name", it.firstName)
//                    val json = Gson().fromJson<List<String>>(it.ages, ArrayList::class.java)
//                    json.forEach {
//                        Log.d("json foreach", it)
//                    }
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
        agesChildren: String,
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
             convertDate(System.currentTimeMillis(), "yyyy-MM-dd hh:mm:ss"),
             relationship,
             firstName,
             lastName,
             dob,
             contactNum,
             email,
             numOfChild,
             agesChildren,
             currentBrand,
             timeStamp,
             if (parent) "1" else "2",
             selectedProvince,
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
                    mCount.valueOnMain(result)
                }

            }
        }
    }

    private fun validateForm(form: Form): Boolean {

        if (form.provinceId == "-1") {
            mError.postValue("Please select a province")
            return false
        }

//        if (form.province.isEmpty()) {
//            mError.value = "Please select a province"
//            return false
//        }

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

        private fun <T:Any> AppState<T>.handleRepositoryFlowResponse(onSuccess: (Unit) -> Unit = {}): T? {
            when (this) {

                is AppState.Progress -> {
                    Log.d("loading val,", this.isLoading.toString())
                    setLoadingState(this.isLoading)
                }
                is AppState.Success -> {
                    Log.d("handleRepositoryFlowResponse", data.toString())
                    onSuccess(Unit)
                    return this.data
                }

                is AppState.Error -> {
                    mError.valueOnMain(message)
                    Log.d("APPSTATE ERROR", message)                }
            }
            return null
        }


    fun <T:Any?> MutableLiveData<T>.valueOnMain(data : T) {
        viewModelScope.launch(Main) {
            this@valueOnMain.value = data
        }
    }


}