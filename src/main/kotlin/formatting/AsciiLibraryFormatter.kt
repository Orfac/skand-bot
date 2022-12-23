package formatting

import CountryAndDevClicks
import de.vandermeer.asciitable.AsciiTable

class AsciiLibraryFormatter : Formatter {
    override fun formatDevClicks(devClicks: List<CountryAndDevClicks>): List<String> {
        var at = AsciiTable()
        at.addRule()
        at.addRow("№", "Country", "Development Clicks")
        at.addRule()
        var counter = 0
        var pos = 1
        val messages = arrayListOf<String>()
        devClicks.sortedBy { it.devClicks }.reversed()
            .forEach {
                at.addRow(pos.toString(), it.name, it.devClicks.toString())
                pos++
                at.addRule()
                counter++
                if (counter == 9) {
                    counter = 0
                    messages.add(at.render(50))
                    at = AsciiTable()
                    at.addRule()
                }
            }
        if (counter != 0) {
            messages.add(at.render(50))
        }
        messages.forEach {
            println(it.length)
        }
        return messages
    }
}