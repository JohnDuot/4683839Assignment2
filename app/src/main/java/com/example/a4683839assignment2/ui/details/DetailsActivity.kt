package com.example.a4683839assignment2.ui.details

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.a4683839assignment2.R
import com.example.a4683839assignment2.databinding.ActivityDetailsBinding
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ENTITY_JSON = "entity_json"
    }

    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { finish() }

        val json = intent.getStringExtra(EXTRA_ENTITY_JSON).orEmpty()
        val type = object : TypeToken<Map<String, JsonElement>>() {}.type
        val entity: Map<String, JsonElement> = Gson().fromJson(json, type) ?: emptyMap()

        entity.forEach { (key, value) ->
            val label = TextView(this).apply {
                text = prettyLabel(key)
                textSize = 14f
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                setPadding(0, 20, 0, 4)
                setTextColor(getColor(R.color.primary_green))
            }
            val content = TextView(this).apply {
                text = value.asDisplayText()
                textSize = 16f
                setLineSpacing(0f, 1.15f)
                setTextColor(getColor(R.color.text_light))
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
            binding.detailsContainer.addView(label)
            binding.detailsContainer.addView(content)
        }
    }

    private fun prettyLabel(value: String): String =
        value.replace("_", " ").replaceFirstChar { it.uppercase() }

    private fun JsonElement.asDisplayText(): String =
        if (isJsonNull) "" else if (isJsonPrimitive) asJsonPrimitive.asString else toString()
}
