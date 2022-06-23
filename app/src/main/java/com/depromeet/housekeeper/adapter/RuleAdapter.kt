package com.depromeet.housekeeper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.depromeet.housekeeper.databinding.ItemRuleBinding
import com.depromeet.housekeeper.model.RuleResponse

class RuleAdapter(
  private val list: MutableList<RuleResponse>,
  private val onClick: (Int) -> Unit,
) : RecyclerView.Adapter<RuleAdapter.ItemViewHolder>() {

  fun updateDate(rules: MutableList<RuleResponse>) {
    list.clear()
    list.addAll(rules)
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    return ItemViewHolder(
      ItemRuleBinding.inflate(LayoutInflater.from(parent.context),
        parent,
        false
      )
    )
  }

  override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    holder.bind(list[position])
  }

  override fun getItemCount(): Int = list.size

  inner class ItemViewHolder(private val binding: ItemRuleBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(ruleResponse: RuleResponse) {
      binding.ruleName = ruleResponse.ruleName
      binding.ivDelete.setOnClickListener {
        onClick.invoke(ruleResponse.ruleId)
      }
    }
  }
}