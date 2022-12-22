package skanderbeg

data class StatsDto(
    val draw: String,
    val data: Array<Map<String,Any>>,
    val recordsTotal: Int,
    val recordsFiltered: Int


)

fun getName(country : Map<String,Any>) = country["1"].toString()
fun getDevClicks(country : Map<String,Any>) = country["15"].toString().replace(" ", "").toFloat()