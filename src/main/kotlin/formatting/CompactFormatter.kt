package formatting

import CountryAndDevClicks


class CompactFormatter : Formatter {

    data class Column(var header : String, var content : List<String> = emptyList())

    override fun formatDevClicks(devClicks: List<CountryAndDevClicks>): List<String> {
        val columns = listOf(Column("№"), Column("Country"), Column("Dev clicks"))

        val devClicksArrays = listOf<ArrayList<String>>(arrayListOf(), arrayListOf(), arrayListOf())
        devClicks.forEachIndexed { index, devClicksDto ->
            run {
                devClicksArrays[0].add(index.toString())
                devClicksArrays[1].add(devClicksDto.name)
                devClicksArrays[2].add(devClicksDto.devClicks.toString())
            }
        }
        columns.forEachIndexed{
            index, column ->  formatColumn(column, devClicksArrays[index])
        }

        val devClicksMessages = arrayListOf<String>()
        for (i in columns[0].content.indices){
            var message ="|"
            columns.forEach{ message += """${it.content[i]}|""" }
            message+= "\n"
            devClicksMessages.add(message)
        }

        val lineLength = devClicksMessages[0].length
        val separator = "-".repeat(lineLength - 1) + "\n"

        devClicksMessages.addAll(0, buildHeader(columns, separator))
        devClicksMessages.add(separator)

        return devClicksMessages
    }

    private fun buildHeader(columns: List<Column>, separator: String): Collection<String> {
        val header = arrayListOf<String>()
        header.add(separator)
        var headerLine = "|"
        columns.forEach{ headerLine += """${it.header}|""" }
        headerLine+= "\n"
        header.add(headerLine)
        header.add(separator)
        return header
    }

    private fun formatColumn(column: Column, strings: List<String>) {
        var maxLength = strings.maxOf { it.length }
        if (column.header.length > maxLength){
            maxLength = column.header.length
        }

        column.header = updateWithLength(column.header, maxLength)
        column.content = strings.map { updateWithLength(it, maxLength) }.toList()
    }

    private fun updateWithLength(text: String, length: Int): String {
        var result = text
        if (text.length < length) {
            for (i in 0 until length - text.length) {
                result += " "
            }
        }
        return " $result "
    }
}