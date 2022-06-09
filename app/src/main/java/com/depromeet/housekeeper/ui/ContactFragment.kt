package com.depromeet.housekeeper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentContactBinding

class ContactFragment : Fragment() {
    lateinit var binding: FragmentContactBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contact, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.contactHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.contact_header_text)
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }
    }
}