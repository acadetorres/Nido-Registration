package com.acdetorres.nidoregistration

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.acdetorres.nidoregistration.databinding.DialogSignatureBinding

class DialogSignature(
    val onSave: (Bitmap) -> Unit,
    val onDelete: () -> Unit
) : DialogFragment(){

    fun DialogFragment.setWidthHeight(width: Int, height : Int) {
//        val percent = percentage.toFloat() / 100
        val newWidth = width.toFloat() / 100
        val newHeight = width.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * newWidth
        val percentHeigt = rect.height() * newHeight
        dialog?.window?.setLayout(percentWidth.toInt(), percentHeigt.toInt())
    }


    fun DialogFragment.setHeightPercent(percentage: Int) {
        val percent = percentage.toFloat() / 100
        val dm = Resources.getSystem().displayMetrics
        val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
        val percentWidth = rect.width() * percent
        dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    val binding : DialogSignatureBinding? get () = mBinding

    var mBinding : DialogSignatureBinding? = null

    override fun onDestroy() {
        super.onDestroy()
        mBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DialogSignatureBinding.inflate(inflater, container, false)

        return (binding?.root)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setWidthHeight(70, 50)
        isCancelable = false
        binding?.also {
            it.tvExit.setOnClickListener {
                dismiss()
            }

            it.tvClear.setOnClickListener {v ->
                it.spSignature.clear()
                onDelete()
            }

            it.tvSave.setOnClickListener { v ->
                val img = it.spSignature.signatureBitmap
                onSave(img)
                dismiss()
            }

        }
    }

}