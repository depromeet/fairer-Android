package com.depromeet.housekeeper.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import timber.log.Timber

abstract class BaseFragment<B: ViewDataBinding>(private val resId: Int): Fragment() {

    private var _binding: B? = null
    val binding get() = _binding!!

    abstract fun createView(binding: B)
    abstract fun viewCreated() // livedata observing, adapter setting 등의 작업 진행

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, resId, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        createView(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
