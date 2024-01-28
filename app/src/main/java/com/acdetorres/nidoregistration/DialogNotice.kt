package com.acdetorres.nidoregistration

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acdetorres.nidoregistration.databinding.DialogNoticeBinding

class DialogNotice (
    val subtitle : String,
    val title : String? = null,
    val listener : OnSuccessListener? = null
) : DialogFragment(){


    interface OnSuccessListener {
        fun onSuccess()
    }

    lateinit var binding : DialogNoticeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogNoticeBinding.inflate(inflater, container, false)
        isCancelable = false
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (title != null) binding.tvTitle.text = title
        binding.tvSubtitle.text = subtitle

        binding.clOk.setOnClickListener {
            listener?.onSuccess()
            dismiss()
        }
    }

}