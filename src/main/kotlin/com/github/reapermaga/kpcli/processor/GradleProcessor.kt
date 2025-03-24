package com.github.reapermaga.kpcli.processor

import com.github.reapermaga.kpcli.Template
import com.github.reapermaga.kpcli.printError
import com.github.reapermaga.kpcli.wizard.GradleWizardResult
import java.io.File
import kotlin.reflect.KClass

object Dependencies {
    const val KTLINT = "ktlint"
    const val SHADOWJAR = "shadowjar"
    const val REAPERMAGA_COMMON_LIBRARY = "reapermaga-common-library"
}

class GradleProcessor : Processor<GradleWizardResult> {
    override val wizardResultType: KClass<GradleWizardResult> = GradleWizardResult::class

    override fun process(
        template: Template,
        folder: File,
        wizardResult: GradleWizardResult,
    ) {
        val buildGradleKtsFile =
            folder.listFiles()?.firstOrNull { it.name == "build.gradle.kts" }
        if (buildGradleKtsFile == null) {
            printError("Couldn't find build.gradle.kts inside template folder.")
            return
        }
        var text = buildGradleKtsFile.readText()

        val plugins: MutableMap<String, String> = mutableMapOf()
        val repositores: MutableSet<String> = mutableSetOf()
        val dependencies: MutableSet<String> = mutableSetOf()
        wizardResult.dependencies.forEach {
            if (it == Dependencies.KTLINT) {
                plugins["org.jlleitschuh.gradle.ktlint"] = "12.2.0"
            } else if (it == Dependencies.REAPERMAGA_COMMON_LIBRARY) {
                repositores.add("https://repo.repsy.io/mvn/reapermaga/library")
                dependencies.add("com.github.reapermaga.library:common:+")
            } else if (it == Dependencies.SHADOWJAR) {
                plugins["com.gradleup.shadow"] = "9.0.0-beta2"
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
                            append("maven { url = uri(\"${it}\") }\n")
                        }
                    },
                )
        }
        buildGradleKtsFile.writeText(text)
    }
}
