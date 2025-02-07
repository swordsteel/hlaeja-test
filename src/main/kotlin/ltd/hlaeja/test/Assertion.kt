package ltd.hlaeja.test

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
