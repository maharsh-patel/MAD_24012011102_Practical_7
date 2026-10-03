package com.example.mad_24012011102_practical_7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_24012011102_practical_7.databinding.ItemPersonBinding

class PersonAdapter(
    private val persons: MutableList<Person>,
    private val onDeleteClick: (position: Int) -> Unit
) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val person = persons[position]
        holder.binding.textViewName.text = person.name
        holder.binding.textViewPhone.text = person.phoneNo
        holder.binding.textViewEmail.text = person.emailId
        holder.binding.textViewAddress.text = person.address
        holder.binding.buttonDelete.setOnClickListener { onDeleteClick(holder.bindingAdapterPosition) }
    }

    override fun getItemCount(): Int = persons.size
}
