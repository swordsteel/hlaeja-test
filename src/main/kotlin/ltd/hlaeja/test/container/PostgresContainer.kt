package ltd.hlaeja.test.container

import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestExecutionListeners
import org.springframework.test.context.TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS

@Suppress("unused")
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@ContextConfiguration(initializers = [PostgresInitializer::class])
@TestExecutionListeners(listeners = [PostgresInitializer::class], mergeMode = MERGE_WITH_DEFAULTS)
annotation class PostgresContainer
