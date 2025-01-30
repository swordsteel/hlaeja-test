package ltd.hlaeja.test.container

import java.io.BufferedReader
import java.io.InputStreamReader
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.core.io.ClassPathResource
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlinx.coroutines.runBlocking
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull

class PostgresExtension : BeforeAllCallback, AfterAllCallback {

    override fun beforeAll(context: ExtensionContext) {
        executeSqlFile(context, "postgres/data.sql")
    }

    override fun afterAll(context: ExtensionContext) {
        executeSqlFile(context, "postgres/reset.sql")
    }

    private fun executeSqlFile(
        context: ExtensionContext,
        resourceSourcePath: String,
    ) = runBlocking {
        if (ClassPathResource(resourceSourcePath).exists()) {
            executeSqlStatements(
                postgresConnection(context),
                makeSqlStatements(ClassPathResource(resourceSourcePath)),
            )
        }
    }

    @Suppress("TooGenericExceptionThrown")
    private suspend fun postgresConnection(
        context: ExtensionContext,
    ) = SpringExtension.getApplicationContext(context)
        .getBean(ConnectionFactory::class.java)
        .create()
        .awaitFirstOrElse { throw RuntimeException("Connection factory could not be created") }

    private suspend fun executeSqlStatements(
        connection: Connection,
        statements: List<String>,
    ) {
        try {
            statements.forEach { statement ->
                connection.createStatement(statement)
                    .execute()
                    .awaitFirstOrNull()
            }
        } finally {
            connection.close().awaitFirstOrNull()
        }
    }

    private fun makeSqlStatements(
        classPathResource: ClassPathResource,
    ): List<String> = classPathResource.inputStream.use { inputStream ->
        BufferedReader(InputStreamReader(inputStream))
            .lines()
            .filter { it.isNotEmpty() && !it.startsWith("--") }
            .map { it.trim() }
            .toList()
            .joinToString(" ")
            .split(';')
            .filter { it.isNotBlank() }
            .map { "${it.trim()};" }
    }
}
