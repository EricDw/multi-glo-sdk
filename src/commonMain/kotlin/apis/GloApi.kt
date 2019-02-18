package apis

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class GloApi(
    private val authenticationToken: String,
    private val httpClient: HttpClient =
        HttpClient()
)
{

    suspend fun getUser(): String = httpClient.get {
        url(
            "$USER_ENDPOINT$authenticationToken"
        )
    }

    companion object
    {
        internal const val API_VERSION = "v1"
        internal const val BASE_URL = "https://gloapi.gitkraken.com/$API_VERSION/glo"
        internal const val QUERY_ACCESS_TOKEN = "?access_token="

        internal const val USER_ENDPOINT = "$BASE_URL/user$QUERY_ACCESS_TOKEN"
    }

}