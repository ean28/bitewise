import android.content.res.Resources
import com.bitewise.app.R

enum class GradeTypes(val arrayRes: Int) {
    NUTRI_SCORE(R.array.nutritional_grade_comments),
    ECO_SCORE(R.array.eco_grade_comments),
    NOVA_SCORE(R.array.nova_grade_comments);

    fun getColor(grade: Any?): Int {
        val input = grade?.toString()?.uppercase() ?: ""
        return when (this) {
            NUTRI_SCORE, ECO_SCORE -> when (input) {
                "A" -> R.color.grade_a
                "B" -> R.color.grade_b
                "C" -> R.color.grade_c
                "D" -> R.color.grade_d
                "E" -> R.color.grade_e
                "F" -> R.color.grade_f
                else -> R.color.grade_none
            }
            NOVA_SCORE -> when (input) {
                "1" -> R.color.nova_a
                "2" -> R.color.nova_b
                "3" -> R.color.nova_c
                "4" -> R.color.nova_d
                else -> R.color.nova_none
            }
        }
    }

    fun getComment(grade: Any?, resources: Resources): String {
        val input = grade?.toString()?.uppercase() ?: ""
        val comments = resources.getStringArray(this.arrayRes)

        val index = when (this) {
            NUTRI_SCORE, ECO_SCORE -> {
                val pos = if (input.length == 1 && input[0] in 'A'..'E') (input[0] - 'A' + 1) else 0
                pos
            }
            NOVA_SCORE -> {
                val pos = input.toIntOrNull() ?: 0
                if (pos in 1..4) pos else 0
            }
        }

        return comments.getOrNull(index) ?: comments[0]
    }
}
