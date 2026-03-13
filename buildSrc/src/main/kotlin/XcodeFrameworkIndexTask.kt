import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class XcodeFrameworkIndexTask @Inject constructor(
    private val fileSystemOperations: FileSystemOperations
) : DefaultTask() {

    @get:Input
    @get:Optional
    abstract val configuration: Property<String>

    @get:Input
    @get:Optional
    abstract val sdkName: Property<String>

    @get:Internal
    abstract val xcodeFrameworksDir: DirectoryProperty

    init {
        group = "xcode"
        description = "Copies the generated ComposeApp framework into a stable platform directory for Xcode indexing."
    }

    @TaskAction
    fun sync() {
        val configurationValue = configuration.orNull ?: return
        val sdkNameValue = sdkName.orNull ?: return
        val platformName = sdkNameValue.takeWhile { !it.isDigit() && it != '.' }

        if (platformName.isBlank()) return

        val sourceDir = xcodeFrameworksDir.dir("$configurationValue/$sdkNameValue/ComposeApp.framework").get().asFile
        if (!sourceDir.exists()) return

        val targetDir = xcodeFrameworksDir.dir("$configurationValue/$platformName/ComposeApp.framework").get().asFile

        fileSystemOperations.delete {
            delete(targetDir)
        }
        fileSystemOperations.copy {
            from(sourceDir)
            into(targetDir)
        }
    }
}
