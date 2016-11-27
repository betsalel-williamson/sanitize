package sanitizerTests

import components.Sanitizer
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertEquals

/**
 * Created by work on 11/27/16.
 */

class findUrlsTests : Spek({

    describe("a sanitizer") {

        it("should return a url") {
            var s = "<tr style=\"display:block; height:1px; line-height:1px;\">            <td> <img src=\"http://li.E.com/imp?s=180017&sz=1x1&li=birthday_Fkids&e=B@G.com&p=77d8f50d400349279c0ecb8cf4368d730f45780dc5a14b9cbeca0e139c9b04a5\" height=\"1\" width=\"10\" /> </td>"

            var results = Sanitizer.FindUrls(s)

            assertEquals(1, results.size)

        }
    }
})