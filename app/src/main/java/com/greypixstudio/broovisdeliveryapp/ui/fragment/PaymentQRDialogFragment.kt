package com.greypixstudio.broovisdeliveryapp.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.greypixstudio.broovisdeliveryapp.R
import com.greypixstudio.broovisdeliveryapp.databinding.FragmentPaymentQRDialogBinding


/**
 * create an instance of this fragment.
 */
class PaymentQRDialogFragment : DialogFragment() {

    lateinit var binding: FragmentPaymentQRDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_payment_q_r_dialog,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.cashCollectedBtn.setOnClickListener {
           showDialog()
        }
    }

    private fun showDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.sub_delivered_order)
        builder.setMessage(R.string.delivered_order_desc)
        builder.setPositiveButton(getString(R.string.lbl_confirm)) { dialogInterface, _ ->
            dialogInterface.dismiss()
          //  orderDeliveredObserver(onGoingOrderList.type , onGoingOrderList.oRDERNUMBER)

        }
        builder.setNegativeButton(getString(R.string.lbl_cancel)) { dialogInterface, _ ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        dialog!!.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
    }

}