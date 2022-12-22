package formatting

import CountryAndDevClicks

open interface Formatter {
    fun formatDevClicks(devClicks: List<CountryAndDevClicks>): List<String>
}
