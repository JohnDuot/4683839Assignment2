package com.example.a4683839assignment2.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.a4683839assignment2.databinding.ItemEntityBinding
import com.google.gson.JsonElement

class EntityAdapter(
    private val onClick: (Map<String, JsonElement>) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    private val items = mutableListOf<Map<String, JsonElement>>()

    fun submitList(newItems: List<Map<String, JsonElement>>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class EntityViewHolder(
        private val binding: ItemEntityBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Map<String, JsonElement>) {
            val visibleFields = entity.entries
                .filterNot { it.key.equals("description", ignoreCase = true) }

            val first = visibleFields.firstOrNull()
            binding.entityTitle.text = first?.value?.asDisplayText() ?: "Entity"
            binding.entitySummary.text = visibleFields
                .drop(1)
                .joinToString("\n") { "${prettyLabel(it.key)}: ${it.value.asDisplayText()}" }
                .ifBlank { "Tap to view details" }

            binding.root.setOnClickListener { onClick(entity) }
        }
    }

    private fun JsonElement.asDisplayText(): String =
        if (isJsonNull) "" else if (isJsonPrimitive) asJsonPrimitive.asString else toString()

    private fun prettyLabel(value: String): String =
        value.replace("_", " ").replaceFirstChar { it.uppercase() }
}
