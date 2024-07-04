import gradle.`is`.awesome.FileDiffTask

interface FileDiffPluginExtension {
    val file1: RegularFileProperty
    val file2: RegularFileProperty
}
class FileDiffPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create<FileDiffPluginExtension>("fileDiff")

        project.tasks.register<FileDiffTask>("fileDiff") {
            file1.set(extension.file1)
            file2.set(extension.file2)
        }}
}
apply<FileDiffPlugin>()

configure<FileDiffPluginExtension> {
    file1.set(file("rollercoaster.txt"))
    file2.set(file("logflume.txt"))
}
