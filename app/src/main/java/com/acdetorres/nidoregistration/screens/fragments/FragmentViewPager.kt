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

class FragmentViewPager(
    val position: Int,
    val onAgree: (Boolean) -> Unit
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

            Log.d("Position", position.toString())
            val img = when (position) {
                0 -> {
                    R.drawable.probing
                }
                1 -> {
                    R.drawable.probing2
                }

                else -> {
                    R.drawable.ic_dropdown
                }
            }
            Glide.with(ivIntro)
                .load(img)
                .into(ivIntro)

            if (position == 1) {
                view.setOnClickListener {
                    onAgree(true)
                }
            }
        }




    }

}