package com.acdetorres.nidoregistration.screens.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acdetorres.nidoregistration.R
import com.acdetorres.nidoregistration.databinding.FragmentViewPagerBinding
import com.bumptech.glide.Glide
import timber.log.Timber

class FragmentViewPager(
    val position: Int,
    val onAgree: (Boolean) -> Unit,
    val dontSkip : () -> Unit,
    val isHospital : (Boolean) -> Unit
) : Fragment() {

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
            }

            tvHouseToHouse.setOnClickListener {
                isHospital(false)
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
                    R.drawable.probing3
                }
                3 -> {
                    R.drawable.functionalbenefit22
                }
                4 -> {
                    R.drawable.functional_benefit34
                }
                5 -> {
                    R.drawable.brandcomparison6
                }
                6 -> {
                    R.drawable.functionalenefit41
                }
                7 -> {
                    R.drawable.nutricheck
                }

                8 -> {
//                    cvSkip.visibility = View.GONE
//                    Timber.e("POSITION $position")
                    R.drawable.outro

                }


                else -> {0}
            }

            if (position != 9) {
                Glide.with(ivIntro)
                    .load(img)
                    .into(ivIntro)
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