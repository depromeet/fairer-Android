package com.depromeet.housekeeper.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.depromeet.housekeeper.R
import com.depromeet.housekeeper.databinding.FragmentMainBinding

class MainFragment : Fragment() {

  lateinit var binding: FragmentMainBinding
  private val mainViewModel: MainViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View {
    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
    binding.lifecycleOwner = this.viewLifecycleOwner
    binding.vm = mainViewModel
    
    binding.mainAddTodoFb.setOnClickListener {
      navigateToAddTodoPage()
    }

    return binding.root
  }

  private fun navigateToAddTodoPage() {
    binding.mainAddTodoFb.findNavController().navigate(R.id.action_mainFragment_to_addTodoFragment1)
  }

}