package com.github.reapermaga.kpcli

import com.github.reapermaga.kpcli.wizard.BaseWizard
import com.github.reapermaga.kpcli.wizard.Wizard
import com.github.reapermaga.kpcli.wizard.WizardResult
import com.github.reapermaga.library.common.download
import net.lingala.zip4j.ZipFile
import java.io.File

val templates =
    arrayOf(
        Template("Base", "https://github.com/ReaperMaga/kotlin-gradle-base-template/archive/refs/tags/1.0.2.zip", BaseWizard()),
    )

fun downloadTemplate(
    template: Template,
    projectName: String,
): File {
    val downloadedZipFile = File("project.zip")
    return download(template.url, downloadedZipFile.name).run {
        val projectFolder = File(".")
        val zipFile = ZipFile(name)
        val fileName = zipFile.fileHeaders.firstOrNull { it.isDirectory }?.fileName ?: error("Couldn't find a directory")
        zipFile.extractAll(projectFolder.path)
        val downloadedFile = File(fileName)
        val destinationFile =
            File(projectName).apply {
                mkdir()
            }
        downloadedFile.copyRecursively(destinationFile, true)
        downloadedFile.deleteRecursively()
        downloadedZipFile.delete()
        destinationFile
    }
}

data class Template(
    val name: String,
    val url: String,
    val wizard: Wizard<out WizardResult>,
)
