package com.example.allfavour.ui

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.allfavour.DecoratedActivity

import com.example.allfavour.R
import kotlinx.android.synthetic.main.choose_side_fragment.*

class ChooseSideFragment : DialogFragment() {

    companion object {
        fun newInstance() = ChooseSideFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.choose_side_fragment, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Disables back behaviour */
        isCancelable = false

        val activity = requireActivity() as DecoratedActivity
        choose_consumer_side_button.setOnClickListener {
            activity.navigateToConsumerOrProvider("consumer")
        }

        choose_provider_side_button.setOnClickListener {
            activity.navigateToConsumerOrProvider("provider")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        val activity = requireActivity() as DecoratedActivity
        activity.toggleBottomNavVisibility(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val fm = activity!!.supportFragmentManager
        val frag = fm.findFragmentById(R.id.maps_autocomplete_fragment_basic_forms)

        if (frag != null)
            fm.beginTransaction().remove(frag).commit()
    }

    override fun onStart() {
        super.onStart()

        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT

            dialog!!.window!!.setLayout(width, height)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.setWindowAnimations(R.style.AllFavour_SlideDialog)
        }

    }
}
