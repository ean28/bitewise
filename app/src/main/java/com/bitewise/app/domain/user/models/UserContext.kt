package com.bitewise.app.domain.user.models

data class UserContext(
    val age: Int,
    val weight: Double,
    val height: Double,
    val activity: String,
    val conditions: List<String>,
    val dietary: List<String>,
    val allergies: List<String>
) {
    val dietType: String get() = dietary.firstOrNull() ?: "Standard"

    companion object {
        const val PROMPT_VERSION = 1
    }

    fun isComplete(): Boolean {
        return age > 0 && 
               weight > 0.0 && 
               height > 0.0 && 
               activity.isNotBlank() &&
               dietary.isNotEmpty()
    }

//    val contextHash: Int by lazy {
//        var result = age
//        result = 31 * result + weight.hashCode()
//        result = 31 * result + height.hashCode()
//        result = 31 * result + activity.hashCode()
//        result = 31 * result + conditions.hashCode()
//        result = 31 * result + dietary.hashCode()
//        result = 31 * result + allergies.hashCode()
//        result = 31 * result + PROMPT_VERSION
//        result
//    }

    override fun hashCode(): Int {
        var result = age
        result = 31 * result + weight.hashCode()
        result = 31 * result + height.hashCode()
        result = 31 * result + activity.hashCode()
        result = 31 * result + conditions.hashCode()
        result = 31 * result + dietary.hashCode()
        result = 31 * result + allergies.hashCode()
        result = 31 * result + PROMPT_VERSION
        return result.hashCode()
    }
}
