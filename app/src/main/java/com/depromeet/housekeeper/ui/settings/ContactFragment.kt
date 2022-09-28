package com.depromeet.housekeeper.ui.settings

import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.base.BaseFragment
import com.depromeet.housekeeper.databinding.FragmentContactBinding
import com.depromeet.housekeeper.util.BindingAdapter

class ContactFragment : BaseFragment<FragmentContactBinding>(R.layout.fragment_contact) {

    override fun createView(binding: FragmentContactBinding) {
    }

    override fun viewCreated() {
        initListener()
    }

    private fun initListener() {
        binding.contactHeader.apply {
            defaultHeaderTitleTv.text = resources.getString(R.string.contact_header_text)
            defaultHeaderBackBtn.setOnClickListener {
                it.findNavController().navigateUp()
            }
        }
        BindingAdapter.setLinkify(
            binding.contactInstagramTv,
            getString(R.string.fairer_instagram),
            getString(R.string.fairer_instagram_link)
        )
    }


}