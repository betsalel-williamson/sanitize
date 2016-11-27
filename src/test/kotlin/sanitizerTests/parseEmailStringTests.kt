package sanitizerTests

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.test.assertEquals

import components.Sanitizer;

/**
 * Created by work on 11/25/16.
 */

class parseEmailStringTests : Spek({

    describe("a sanitizer") {

        it("should return the name in an email") {
            val result = Sanitizer.ParseEmailString("<bj@wana.fr>")
            assertEquals("bj".toUpperCase(), result[0])
        }

        it("should return the first name in an email") {
            val result = Sanitizer.ParseEmailString("<bj.foo@wana.fr>")
            assertEquals("bj".toUpperCase(), result[0])
        }

        it("should return the second name in an email") {
            val result = Sanitizer.ParseEmailString("<bj.foo@wana.fr>")
            assertEquals("foo".toUpperCase(), result[1])
        }

        it("should not return the domain type") {
            val result = Sanitizer.ParseEmailString("<bj.foo@wana.fr>")
            assertEquals(3, result.size)
        }
    }
})

