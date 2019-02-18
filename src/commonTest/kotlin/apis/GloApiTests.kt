package apis

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.launch
import kotlinx.io.charsets.Charsets
import kotlinx.io.core.toByteArray
import kotlin.test.Test
import kotlin.test.assertEquals

class GloApiTests
{
    @Suppress("SpellCheckingInspection")
    private val testPAT = "randombugaloo"

    private val userJSON = """{"username":"testUser","id":"random-gib-er-i-sh"}"""

    private val httpMockEngine = MockEngine {
        when (url.fullUrl)
        {
            "${GloApi.USER_ENDPOINT}$testPAT" ->
            {

                MockHttpResponse(
                    call,
                    HttpStatusCode.OK,
                    ByteReadChannel(userJSON.toByteArray(Charsets.UTF_8)),
                    headersOf("Content-Type" to listOf(ContentType.Text.Plain.toString()))
                )
            }
            else ->
            {
                error("Unhandled ${url.fullUrl}")
            }
        }
    }

    private val client = HttpClient(httpMockEngine)

    private val gloApi = GloApi(testPAT, client)

    @Test
    fun `given validPAT when getUser then return userJSON`()
    {
        // Arrange
        val expected = userJSON
        var actual = ""
        // Act
        GlobalScope.launch(Dispatchers.Unconfined) {
            actual = gloApi.getUser()
        }

        // Assert
        assertEquals(expected, actual)
    }

    private val Url.hostWithPortIfRequired: String get() = if (port == protocol.defaultPort) host else hostWithPort
    private val Url.fullUrl: String get() = "${protocol.name}://$hostWithPortIfRequired$fullPath"

}