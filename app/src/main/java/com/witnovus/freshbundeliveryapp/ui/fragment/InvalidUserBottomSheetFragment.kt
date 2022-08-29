package com.witnovus.freshbundeliveryapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.witnovus.nepzep.utils.Event
import com.witnovus.freshbundeliveryapp.R
import com.witnovus.freshbundeliveryapp.databinding.InvalidUserBottomSheetFragmentBinding
import com.witnovus.freshbundeliveryapp.ui.activity.MobileNumberActivity
import com.witnovus.freshbundeliveryapp.ui.base.BaseActivity
import com.witnovus.freshbundeliveryapp.utils.Constants
import io.paperdb.Paper

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * create an instance of this fragment.
 */
class InvalidUserBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: InvalidUserBottomSheetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_invalid_user_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val error = arguments?.get(Constants.ERROR_MESSAGE).toString()
        binding.msgTxtView.text = error
        binding.okBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MobileNumberActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            requireActivity().finish()
            (BaseActivity).sessionExpired.postValue(Event(true))
            Paper.book().delete(Constants.IS_LOGIN)
        }
    }
}