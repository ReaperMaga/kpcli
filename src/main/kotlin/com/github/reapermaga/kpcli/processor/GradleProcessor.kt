package com.github.reapermaga.kpcli.processor

import com.github.reapermaga.kpcli.Template
import com.github.reapermaga.kpcli.wizard.GradleWizardResult
import java.io.File
import kotlin.reflect.KClass

data class SupportedDependency(
    val displayName: String,
    val notation: String,
    val repository: String? = null,
)

val supportedDependencies =
    mapOf(
        "ktlint" to SupportedDependency("Ktlint", ""),
        "ktlint" to SupportedDependency("Ktlint", ""),
    )

class GradleProcessor : Processor<GradleWizardResult> {
    override val wizardResultType: KClass<GradleWizardResult> = GradleWizardResult::class

    override fun process(
        template: Template,
        folder: File,
        wizardResult: GradleWizardResult,
    ) {
        val buildGradleKtsFile =
            folder.listFiles()?.firstOrNull { it.name == "build.gradle.kts" } ?: error("Couldn't find build.gradle.kts")
        var text = buildGradleKtsFile.readText()

        val plugins: MutableMap<String, String> = mutableMapOf()
        val repositores: MutableSet<String> = mutableSetOf()
        val dependencies: MutableSet<String> = mutableSetOf()
        wizardResult.dependencies.forEach {
            if (it == "ktlint") {
                plugins["org.jlleitschuh.gradle.ktlint"] = "12.2.0"
            } else if (it == "reapermaga-common-library") {
                repositores.add("https://repo.repsy.io/mvn/reapermaga/library")
                dependencies.add("com.github.reapermaga.library:common:+")
            }
        }
        val pluginsComment = "// Plugins"
        val dependenciesComment = "// Dependencies"
        val repositoriesComment = "// Repositories"
        if (text.contains(pluginsComment)) {
            text =
                text.replace(
                    pluginsComment,
                    buildString {
                        plugins.forEach {
                            append("id(\"${it.key}\") version \"${it.value}\" \n")
                        }
                    },
                )
        }
        if (text.contains(dependenciesComment)) {
            text =
                text.replace(
                    dependenciesComment,
                    buildString {
                        dependencies.forEach {
                            append("implementation(\"${it}\")\n")
                        }
                    },
                )
        }
        if (text.contains(repositoriesComment)) {
            text =
                text.replace(
                    repositoriesComment,
                    buildString {
                        repositores.forEach {
                            append("maven { url = uri(\"${it}\")\n }")
                        }
                    },
                )
        }
        buildGradleKtsFile.writeText(text)
    }
}
