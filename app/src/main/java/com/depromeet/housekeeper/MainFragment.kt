package com.depromeet.housekeeper

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.depromeet.housekeeper.databinding.FragmentMainBinding

class MainFragment : Fragment() {

  lateinit var binding: FragmentMainBinding

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

    binding.lifecycleOwner = this.viewLifecycleOwner
    return binding.root
  }

}