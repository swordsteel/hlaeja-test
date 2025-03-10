package ltd.hlaeja.test.container

import io.github.oshai.kotlinlogging.KotlinLogging
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlinx.coroutines.runBlocking
import ltd.hlaeja.test.util.isResourceFile
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.stereotype.Component

private val log = KotlinLogging.logger {}

@Component
class PostgresExecutor(
    private val databaseClient: DatabaseClient,
) {

    fun executeSqlFile(
        sqlFile: String,
    ) = runBlocking {
        sqlFile.isResourceFile()
            ?.inputStream
            ?.use {
                log.debug { "Executing SQL file: $sqlFile" }
                executeSqlStatements(makeSqlStatements(it))
            }
            ?: log.debug { "SQL file not found or not readable: $sqlFile" }
    }

    @Suppress("TooGenericExceptionThrown")
    private suspend fun executeSqlStatements(
        statements: List<String>,
    ) = try {
        statements.forEach { statement ->
            log.debug { "Running statement: $statement" }
            databaseClient.sql(statement).await()
        }
    } catch (e: Exception) {
        throw RuntimeException("Failed to execute SQL statements", e)
    }

    private suspend fun makeSqlStatements(
        inputStream: InputStream,
    ): List<String> = BufferedReader(InputStreamReader(inputStream))
        .lines()
        .filter { it.isNotEmpty() && !it.startsWith("--") }
        .map { it.trim() }
        .toList()
        .joinToString(" ")
        .split(';')
        .filter { it.isNotBlank() }
        .map { "${it.trim()};" }
}
