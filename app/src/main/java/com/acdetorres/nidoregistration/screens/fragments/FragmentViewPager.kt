package com.acdetorres.nidoregistration.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.MutableLiveData
import com.acdetorres.nidoregistration.R
import com.acdetorres.nidoregistration.databinding.FragmentViewPagerBinding

class FragmentViewPager(
    val position: Int,
    val onAgree: (Boolean) -> Unit,
    val dontSkip: () -> Unit,
    val isHospital: (Boolean) -> Unit,
    val forHospital: MutableLiveData<Boolean>
) : Fragment() {


//    var forHospital = false

    var mBinding : FragmentViewPagerBinding? = null

    val binding : FragmentViewPagerBinding? get() = mBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       mBinding = FragmentViewPagerBinding.inflate(inflater, container, false)
        return mBinding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.run {


            tvHospital.setOnClickListener {
                isHospital(true)
//                forHospital = true
            }

            tvHouseToHouse.setOnClickListener {
                isHospital(false)
//                forHospital = false
            }

            if (position > 0) {
                ivRegistrationIcon.visibility = View.GONE
                tvHospital.visibility = View.GONE
                tvHouseToHouse.visibility = View.GONE
            } else {
                ivRegistrationIcon.visibility = View.VISIBLE
                tvHospital.visibility = View.VISIBLE
                tvHouseToHouse.visibility = View.VISIBLE
            }
            Log.d("Position", position.toString())

            forHospital.observe(viewLifecycleOwner) {

                val img = when (position) {
                    0 -> {
                        R.drawable.iv_establishment_selector
                    }
                    1 -> {
                        R.drawable.probing2
                    }
                    2 -> {
//                    cvSkip.visibility = View.VISIBLE
//                    tvDontSkip.setOnClickListener {
//                        dontSkip()
//                    }
                        if (it) R.drawable.probing3 else R.drawable.functionalbenefit1
                    }
                    3 -> {
                        R.drawable.functionalbenefit22
                    }
                    4 -> {
                        if (it) R.drawable.functional_benefit34 else R.drawable.functionalbenefit33
                    }
                    5 -> {
                        R.drawable.brandcomparison6
                    }
                    6 -> {
                        if (it) R.drawable.functionalenefit41 else R.drawable.functionalbenefit4
                    }
                    7 -> {
                        if (it) R.drawable.nutricheck else R.drawable.functionalbenefit5
                    }

                    8 -> {
//                    cvSkip.visibility = View.GONE
//                    Timber.e("POSITION $position")
                        R.drawable.outro

                    }


                    else -> {
                        0
                    }
                }

                if (forHospital.value == false && position == 8) {
                    ivIntro.visibility = View.GONE
                } else {
                    ivIntro.visibility = View.VISIBLE
                }


                if (position != 9) {
                    ivIntro.setImageDrawable(context?.let { AppCompatResources.getDrawable(it, img ) })
//                Glide.with(ivIntro)
//                    .load(img)
//                    .into(ivIntro)
                }

            }



            if (position == 1) {
                vIAgree.setOnClickListener {
                    onAgree(true)
                }
            }

//            if (position == 2) {
//                view.setOnClickListener {
//                    dontSkip()
//                }
//            }
        }




    }

}