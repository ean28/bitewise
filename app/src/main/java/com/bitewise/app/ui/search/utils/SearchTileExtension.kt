package com.bitewise.app.ui.search.util

import com.bitewise.app.R

object ScoreMapper {

    fun getNutriDrawable(grade: String?): Int =
        when (grade?.lowercase()) {
        "a" -> R.drawable.grade_nutriscore_a
        "b" -> R.drawable.grade_nutriscore_b
        "c" -> R.drawable.grade_nutriscore_c
        "d" -> R.drawable.grade_nutriscore_d
        "e" -> R.drawable.grade_nutriscore_e
        else -> R.drawable.grade_nutriscore_none
    }

    fun getEcoDrawable(grade: String?): Int =
        when (grade?.lowercase()) {
        "a" -> R.drawable.grade_greenscore_a
        "b" -> R.drawable.grade_greenscore_b
        "c" -> R.drawable.grade_greenscore_c
        "d" -> R.drawable.grade_greenscore_d
        "e" -> R.drawable.grade_greenscore_e
        else -> R.drawable.grade_greenscore_none
    }

    fun getNovaDrawable(group: Int?): Int =
        when (group) {
        1 -> R.drawable.grade_novagroup_1
        2 -> R.drawable.grade_novagroup_2
        3 -> R.drawable.grade_novagroup_3
        4 -> R.drawable.grade_novagroup_4
        else -> R.drawable.grade_novagroup_none
    }
}