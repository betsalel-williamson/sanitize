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


class buildSensitiveStringsTests : Spek({


    describe("a sanitizer") {

        it("should return a list of names") {

            val s = "	La Wa <l.w@s.net>, Ea Za <t@g.com>, d@g.com"
            val result = Sanitizer.BuildSensitiveStrings(listOf(s))

            assertEquals(10, result.size)
            assertTrue(result.contains("l".toUpperCase()))
            assertTrue(result.contains("w".toUpperCase()))
            assertTrue(result.contains("s".toUpperCase()))
            assertTrue(result.contains("t".toUpperCase()))
            assertTrue(result.contains("g".toUpperCase()))
            assertTrue(result.contains("d".toUpperCase()))
            assertTrue(result.contains("La".toUpperCase()))
            assertTrue(result.contains("Wa".toUpperCase()))
            assertTrue(result.contains("Ea".toUpperCase()))
            assertTrue(result.contains("Za".toUpperCase()))
        }

        it("should return one name with interesting whitespace"){
            val s = listOf(" la	La")
            val result = Sanitizer.BuildSensitiveStrings(s)
            assertEquals(1, result.size)
            assertTrue(result.contains("LA"))

        }

    }
})
