package skanderbeg

import CountryAndDevClicks
import com.google.gson.Gson
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

private const val SKANDERBEG_URL = "https://skanderbeg.pm/api/eu4/getLedgerDataNew.php"
private const val EMPTY_DATA_MESSAGE =
    "Received empty data for your save\nMight be skanderbeg saves are not working now"

class SkanderbegClient(val apiKey: String) {
    private val client = HttpClient(CIO) {
        defaultRequest {
            url(SKANDERBEG_URL)
        }
    }

    suspend fun getCountryDevClicks(save: String): List<CountryAndDevClicks> {
        val response = client.post {
            parameter("fileid", save)
            header("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")
//            parameter("key", apiKey)
            setBody(DevClicksFormDefault().value)
        }
        if (response.status != HttpStatusCode.OK) {
            throw Exception("Received status code ${response.status}")
        }
        val resultValue = Gson().fromJson(response.bodyAsText(), Stats::class.java)
        if (resultValue.data == null) {
            throw SkanderbegException(EMPTY_DATA_MESSAGE)
        }
        return resultValue.toCountryAndDevClicksList()
    }
}