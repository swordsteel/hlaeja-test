package ltd.hlaeja.test.container

import io.github.oshai.kotlinlogging.KotlinLogging
import ltd.hlaeja.test.util.getProperty
import ltd.hlaeja.test.util.isResourceFile
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.TestContext
import org.springframework.test.context.TestExecutionListener
import org.testcontainers.containers.PostgreSQLContainer

private val log = KotlinLogging.logger {}

@Suppress("unused")
class PostgresInitializer : ApplicationContextInitializer<ConfigurableApplicationContext>, TestExecutionListener {

    companion object {
        const val SCRIPT_INIT = "container.postgres.init"
        const val SCRIPT_BEFORE = "container.postgres.before"
        const val SCRIPT_AFTER = "container.postgres.after"
        const val POSTGRES_VERSION = "container.postgres.version"
        const val POSTGRES_LATEST = "postgres:latest"
    }

    override fun initialize(
        context: ConfigurableApplicationContext,
    ) {
        postgres(context).apply {
            TestPropertyValues.of(
                "spring.r2dbc.url=r2dbc:pool:postgresql://$host:$firstMappedPort/$databaseName",
                "spring.r2dbc.username=$username",
                "spring.r2dbc.password=$password",
            ).applyTo(context)
        }
    }

    override fun beforeTestClass(
        context: TestContext,
    ) {
        context.testClass
            .also { log.debug { "Starting execution before class: ${it.simpleName}" } }
            .getAnnotation(PostgresContainer::class.java) ?: return
        context.getProperty(SCRIPT_BEFORE)
            ?.let { context.applicationContext.getBean(PostgresExecutor::class.java).executeSqlFile(it) }
    }

    override fun afterTestClass(
        context: TestContext,
    ) {
        context.testClass
            .also { log.debug { "Starting execution after class: ${it.simpleName}" } }
            .getAnnotation(PostgresContainer::class.java) ?: return
        context.getProperty(SCRIPT_AFTER)
            ?.let { context.applicationContext.getBean(PostgresExecutor::class.java).executeSqlFile(it) }
    }

    private fun postgres(
        context: ConfigurableApplicationContext,
    ): PostgreSQLContainer<*> = PostgreSQLContainer(context.getProperty(POSTGRES_VERSION, POSTGRES_LATEST)).apply {
        context.getProperty(SCRIPT_INIT)
            ?.isResourceFile()
            ?.let { lala -> withInitScript(lala.path) }
            ?: log.error { "Postgres init script not found" }
        start()
    }
}
