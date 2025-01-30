package ltd.hlaeja.test.container

import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.ContextConfiguration

@Suppress("unused")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@ExtendWith(PostgresExtension::class)
@ContextConfiguration(initializers = [PostgresInitializer::class])
annotation class PostgresContainer
