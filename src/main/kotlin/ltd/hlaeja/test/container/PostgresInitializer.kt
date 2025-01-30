package ltd.hlaeja.test.container

import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.io.ClassPathResource
import org.testcontainers.containers.PostgreSQLContainer

@Suppress("unused")
class PostgresInitializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

    override fun initialize(applicationContext: ConfigurableApplicationContext) {
        postgres().apply {
            TestPropertyValues.of(
                "spring.r2dbc.url=r2dbc:pool:postgresql://$host:$firstMappedPort/$databaseName",
                "spring.r2dbc.username=$username",
                "spring.r2dbc.password=$password",
            ).applyTo(applicationContext)
        }
    }

    private fun postgres(): PostgreSQLContainer<*> = PostgreSQLContainer("postgres:17")
        .withReuse(true)
        .apply {
            "postgres/schema.sql".let {
                if (ClassPathResource(it).exists()) {
                    withInitScript(it)
                }
            }
            start()
        }
}
