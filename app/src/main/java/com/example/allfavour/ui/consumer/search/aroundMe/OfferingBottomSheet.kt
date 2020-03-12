package com.example.allfavour.ui.consumer.search.aroundMe

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.allfavour.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class OfferingBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = OfferingBottomSheet()
    }

    private lateinit var viewModel: OfferingBottomSheetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.offering_bottom_sheet_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OfferingBottomSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
