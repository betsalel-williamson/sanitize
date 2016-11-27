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

class findEmailsTests : Spek({

    describe("a sanitizer") {

        it("should return the email address ") {
            val result = Sanitizer.FindEmails("Received-SPF: pass (google.com: domain of a.b@gmail.com designates 2a00:1450:400c:c09::230 as permitted sender) client-ip=2a00:1450:400c:c09::230;")
            assertEquals("a.b@gmail.com", result.elementAt(0))
        }

        it("should return the email address ") {
            val result = Sanitizer.FindEmails("Reply-To: \"DICK'S Sporting Goods\" <DSG.3O5A94S.89@email.dcsg.com>"
            )
            assertEquals("DSG.3O5A94S.89@email.dcsg.com", result.elementAt(0))
        }

        it("should not find an email address ") {
            val result = Sanitizer.FindEmails("	by bm1-13.bo3.e-dialog.com (envelope-from <FJ7EZA-7R4I9-ITZV8-26M2Q59-EB7CAC-M-M2-20161107-5c5ad655f8af2e7bdsportgoods.bounce.ed10.net>)" +
                    "(ecelerity 2.2.2.45 r(34222M)) with ECSTREAM")

            assertEquals(0, result.size)
        }

        it("should not find an email address ") {
            val result = Sanitizer.FindEmails("       dkim=pass header.i=@email.appshopper.com;")

            assertEquals(0, result.size)
        }

        it("should return 3 email addresses ") {
            val result = Sanitizer.FindEmails("	L W <l.w@s.net>, E Z <t@g.com>, d@g.com")
            assertEquals("l.w@s.net", result.elementAt(0))
            assertEquals("t@g.com", result.elementAt(1))
            assertEquals("d@g.com", result.elementAt(2))
        }
    }
})

