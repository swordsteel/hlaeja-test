package ltd.hlaeja.test

import java.util.UUID
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.AbstractStringAssert

@Suppress("unused")
fun <SELF : AbstractStringAssert<SELF>?> AbstractStringAssert<SELF>.compareToFile(
    file: String,
): AbstractStringAssert<SELF> = this::class.java.classLoader
    .getResourceAsStream(file)
    ?.bufferedReader()
    ?.readText()
    ?.let { this.isEqualTo(it) }
    ?: throw UnsupportedOperationException(
        "Attempted to compare assertion object to context of a file but expected file was not found: $file",
    )

@Suppress("unused")
fun <SELF : AbstractAssert<SELF, ACTUAL>?, ACTUAL> AbstractAssert<SELF, ACTUAL>.isEqualToUuid(
    uuid: String,
): SELF = try {
    UUID.fromString(uuid).let { expected -> this.isEqualTo(expected) }
} catch (e: IllegalArgumentException) {
    throw UnsupportedOperationException("Invalid UUID string provided: $uuid", e)
}
