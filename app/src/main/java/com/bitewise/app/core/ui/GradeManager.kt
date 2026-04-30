package com.bitewise.app.core.ui

import android.content.res.Resources
import com.bitewise.app.R

object GradeManager {

    fun getNutriDrawable(grade: String?): Int = when (grade?.uppercase()) {
        "A" -> R.drawable.grade_nutriscore_a
        "B" -> R.drawable.grade_nutriscore_b
        "C" -> R.drawable.grade_nutriscore_c
        "D" -> R.drawable.grade_nutriscore_d
        "E" -> R.drawable.grade_nutriscore_e
        else -> R.drawable.grade_nutriscore_none
    }

    fun getEcoDrawable(grade: String?): Int = when (grade?.uppercase()) {
        "A" -> R.drawable.grade_greenscore_a
        "B" -> R.drawable.grade_greenscore_b
        "C" -> R.drawable.grade_greenscore_c
        "D" -> R.drawable.grade_greenscore_d
        "E" -> R.drawable.grade_greenscore_e
        else -> R.drawable.grade_greenscore_none
    }

    fun getNovaDrawable(group: Int?): Int = when (group) {
        1 -> R.drawable.grade_novagroup_1
        2 -> R.drawable.grade_novagroup_2
        3 -> R.drawable.grade_novagroup_3
        4 -> R.drawable.grade_novagroup_4
        else -> R.drawable.grade_novagroup_none
    }

    fun getColor(type: GradeTypes, grade: Any?): Int = type.getColor(grade)

    fun getComment(type: GradeTypes, grade: Any?, resources: Resources): String = 
        type.getComment(grade, resources)
}
