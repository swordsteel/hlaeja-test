package ltd.hlaeja.test.util

import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.TestContext

fun ConfigurableApplicationContext.getProperty(
    property: String,
): String? = this.environment.getProperty(property)

fun ConfigurableApplicationContext.getProperty(
    property: String,
    default: String,
): String = this.environment.getProperty(property, default)

fun TestContext.getProperty(
    property: String,
): String? = this.applicationContext.environment.getProperty(property)

fun String.isResourceFile(): ClassPathResource? {
    val resource = ClassPathResource(this)
    return when {
        resource.exists() && resource.isReadable -> resource
        else -> null
    }
}
