package com.bitewise.app.feature.search.ui

import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.bitewise.app.R
import com.bitewise.app.feature.search.domain.Country
fun AutoCompleteTextView.setupCountryPicker(
    countries: List<Country>,
    onSelect: (Country) -> Unit
) {
    val adapter =
        ArrayAdapter(
            context,
            android.R.layout.simple_dropdown_item_1line,
            countries)
    this.setAdapter(adapter)

    this.setOnItemClickListener { _, _, position, _ ->
        val selected: Country = countries[position]

        val formattedText: String =
            context.getString(
                R.string.country_dropdown_format,
                selected.flag)

        this.setText(formattedText, false)

        onSelect(selected)
    }
}
