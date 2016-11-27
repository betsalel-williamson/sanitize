package sanitizerTests

import components.Sanitizer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Created by work on 11/27/16.
 */

class findProperNounsTests : Spek({

    describe("a sanitizer") {

        it("should return a list of first and last names") {

            val result = Sanitizer.FindProperNouns("	La Ws <la.ws@sa.net>, Ea Zs <ta@gs.com>, da@gs.com")

            assertEquals(4, result.size)
            assertTrue(result.contains("La".toUpperCase()))
            assertTrue(result.contains("Ws".toUpperCase()))
            assertTrue(result.contains("Ea".toUpperCase()))
            assertTrue(result.contains("Zs".toUpperCase()))
        }


        it("should not contain name with colon following the name") {

            val result = Sanitizer.FindProperNouns("To: =?utf-8?Q?Bets=20Will?= <bits@gmail.com>")

            assertFalse(result.remove("TO"))
            assertEquals(2, result.size)
        }


        it("should return an empty list of names") {

            val result = Sanitizer.FindProperNouns("	asdlaws <la.ws@sa.net>,eazs <ta@gs.com>, da@gs.com")

            assertEquals(0, result.size)
        }

        it("should return one name with interesting whitespace") {
            val s = " la	La"
            val result = Sanitizer.FindProperNouns(s)
            assertEquals(1, result.size)
            assertTrue(result.contains("LA"))
        }

        it("should not find any names here") {
            val s = listOf<String>(" Authentication-Results: mx.google.com;",
                    "X-Received: by 10.55.102.18 with SMTP id a18mr1864453qkc.193.1479285563924;")

            s.forEach {
                val result = Sanitizer.FindProperNouns(it)
                assertEquals(0, result.size)
            }
        }

        it("should return one name with quote punctuation") {
            val s = "hope you enjoyed P J us F C Bay's birthday."
            val result = Sanitizer.FindProperNouns(s)
            assertEquals(1, result.size)
            assertTrue(result.contains("Bay".toUpperCase()))
        }

        it("should return one name with period punctuation") {
            val s = "it's time to upload your photos and help out C Bay."
            val result = Sanitizer.FindProperNouns(s)
            assertEquals(1, result.size)
            assertTrue(result.contains("Bay".toUpperCase()))
        }

        it("should return one name with start of line punctuation") {
            val s = "Hope you enjoyed P J us F C bay's birthday."
            val result = Sanitizer.FindProperNouns(s)
            assertEquals(1, result.size)
            assertTrue(result.contains("Hope".toUpperCase()))
        }
    }
})
