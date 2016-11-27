package components

import java.util.regex.Pattern
import java.util.ArrayList
import java.util.HashSet
import java.util.HashMap
import org.apache.commons.*;
import org.apache.commons.lang3.StringUtils


class Sanitizer {
    companion object {
        fun ParseEmailString(s: String): List<String> {

            val emailPatt = Pattern.compile("(([A-Za-z0-9._%+-]+)@([A-Za-z0-9-]+)\\.([A-Za-z0-9]{2,4}|museum))")

            val m = emailPatt.matcher(s)

            val results: MutableList<String> = mutableListOf()

            if (m.find()) {
                val names = m.group(2).toUpperCase().split("[\\_\\-\\.]".toRegex())
                results.addAll(names)
                val domain = m.group(3).toUpperCase().split("[\\_\\-\\.]".toRegex())
                results.addAll(domain)

//                results.add(m.group(4)) // the address
//                results.add(m.group(1)) // the entire email
            }

            return results;
        }


        // possible improvement here would be to change from list of strings to a map so that I can find and replace using O(n) where n is the number of emails to replace
        fun FindEmails(s: String): HashSet<String> {

            val emailPatt = Pattern.compile("([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[A-Za-z0-9]{2,4}|museum)")

            val m = emailPatt.matcher(s)

            val results: HashSet<String> = hashSetOf()

            while (m.find()) {
                results.add(m.group(1))
            }

            return results;
        }

        fun BuildSensitiveStrings(s: List<String>): HashSet<String> {

            val names: HashSet<String> = hashSetOf()

            s.forEach {
                names.addAll(FindUrls(it))

                val emails = FindEmails(it)


                emails.forEach {
                    val tempResults = ParseEmailString(it)

                    names.addAll(tempResults)
                }

                names.addAll(FindProperNouns(it))
            }

            return names;
        }

        // http://stackoverflow.com/questions/1326682/java-replacing-multiple-different-substring-in-a-string-at-once-or-in-the-most
        fun RemoveSensitiveStrings(s: List<String>, sensitiveStrings: HashSet<String>): List<String> {

            val tokens = HashMap<String, String>()

            sensitiveStrings.forEach { tokens.put(it.toUpperCase(), it.substring(0, 1).toUpperCase()) }

            val patternString = "(?i)(" + StringUtils.join(tokens.keys, "|") + ")"
            val pattern = Pattern.compile(patternString)

            val results: MutableList<String> = mutableListOf()

            s.forEach {
                val sb = StringBuffer()

                val matcher = pattern.matcher(it)

                while (matcher.find()) {
                    matcher.appendReplacement(sb, tokens[matcher.group(1).toUpperCase()])
                }

                matcher.appendTail(sb)

                results.add(sb.toString())

            }

            return results
        }

        fun FindProperNouns(s: String): HashSet<String> {

            val properNamePatt = Pattern.compile("(?:[^A-Z-])([A-Z][a-z]+)(?=[=?'.\"\\s])|([A-Z][a-z]+)$|^([A-Z][a-z]+)(?=[=?'.\"\\s])")

            val m = properNamePatt.matcher(s)

            val results: HashSet<String> = hashSetOf()

            while (m.find()) {

                if (!m.group(1).isNullOrEmpty()) {
                    results.add(m.group(1).toUpperCase())
                } else if (!m.group(2).isNullOrEmpty()) {
                    results.add(m.group(2).toUpperCase())
                } else if (!m.group(3).isNullOrEmpty()) {
                    results.add(m.group(3).toUpperCase())
                }
            }

            return results
        }

        fun FindUrls(s: String): HashSet<String> {
            val properNamePatt = Pattern.compile("(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?")

            val m = properNamePatt.matcher(s)

            val results: HashSet<String> = hashSetOf()

            while (m.find()) {

                if (!m.group(0).isNullOrEmpty()) {
                    results.add(m.group(0))
                }
            }

            return results
        }
    }
}