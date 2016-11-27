package sanitizerTests

import components.Sanitizer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by work on 11/27/16.
 */

class removeSensitiveStringsTests : Spek({

    describe("a sanitizer") {

        it("should return a string list of sanitized names") {

            val s = "	La Ws <la.ws@sa.net>, Ea Zs <ta@gs.com>, da@gs.com"
            val result = Sanitizer.RemoveSensitiveStrings(listOf(s), Sanitizer.BuildSensitiveStrings(listOf(s)))

            val resultString = "	L W <L.W@S.net>, E Z <T@G.com>, D@G.com"

            assertEquals(1, result.size)
            assertEquals(resultString, result[0])
        }

        it("should return a list of names") {

            val s = listOf("	La Ws <la.ws@sa.net>, Ea Zs <ta@gs.com>, da@gs.com",
                    "	Za Qs <la.ws@sa.net>, Ea Zs <ea@zs.com>, da@gs.com")
            val result = Sanitizer.RemoveSensitiveStrings(s, Sanitizer.BuildSensitiveStrings(s))

            val resultStrings = listOf("	L W <L.W@S.net>, E Z <T@G.com>, D@G.com",
                    "	Z Q <L.W@S.net>, E Z <E@Z.com>, D@G.com"  )

            assertEquals(2, result.size)
            assertEquals(resultStrings[0], result[0])
            assertEquals(resultStrings[1], result[1])
        }

        it("should remove even lower case occurance") {

            val s = listOf(" la	La")
            val result = Sanitizer.RemoveSensitiveStrings(s, Sanitizer.BuildSensitiveStrings(s))

            val resultStrings = listOf(" L	L")

            assertEquals(1, result.size)
            assertEquals(resultStrings[0], result[0])
        }
    }

})