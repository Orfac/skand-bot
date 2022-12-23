package skanderbeg

import CountryAndDevClicks

data class Stats(
    val draw: String,
    val data: List<Map<String, Any>>,
    val recordsTotal: Int,
    val recordsFiltered: Int
)

private fun Map<String, Any>.getName() = this["1"].toString()
private fun Map<String, Any>.getDevClicks() = this["15"].toString().replace(" ", "").toFloat()

fun Stats.toCountryAndDevClicksList(): List<CountryAndDevClicks> =
    this.data.map { CountryAndDevClicks(it.getName(), it.getDevClicks()) }