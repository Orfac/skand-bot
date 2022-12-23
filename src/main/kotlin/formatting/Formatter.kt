package formatting

import CountryAndDevClicks

interface Formatter {
    fun formatDevClicks(devClicks: List<CountryAndDevClicks>): List<String>
}
