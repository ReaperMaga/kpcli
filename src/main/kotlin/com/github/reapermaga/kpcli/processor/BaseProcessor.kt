package com.github.reapermaga.kpcli.processor

import com.github.reapermaga.kpcli.Template
import com.github.reapermaga.kpcli.wizard.BaseWizardResult
import java.io.File
import kotlin.reflect.KClass

object GithubWorkflows {
    const val CI = "ci"
    const val CI_KTLINT = "ci-ktlint"
}

class BaseProcessor : Processor<BaseWizardResult> {
    override val wizardResultType: KClass<BaseWizardResult> = BaseWizardResult::class

    override fun process(
        template: Template,
        folder: File,
        wizardResult: BaseWizardResult,
    ) {
        val workflowsDirectoryPath =
            folder.absolutePath
                .plus(File.separator)
                .plus(".github")
                .plus(File.separator)
                .plus("workflows")
                .plus(File.separator)
        File(workflowsDirectoryPath).apply {
            mkdirs()
        }
        val templateDirectory = ""
        wizardResult.githubWorkflows.forEach {
            if (it == GithubWorkflows.CI) {
                getContentFromWorkflowsDirectory("ci.yaml").apply {
                    File(workflowsDirectoryPath.plus("ci.yaml"))
                }
            } else if (it == GithubWorkflows.CI_KTLINT) {
                getContentFromWorkflowsDirectory("ci_ktlint.yaml").apply {
                    File(workflowsDirectoryPath.plus("ci_ktlint.yaml"))
                }
            }
        }
    }

    private fun getContentFromWorkflowsDirectory(name: String): String =
        this::class.java.classLoader
            .getResource("workflow_templates/$name")
            ?.readText() ?: error("File $name not found")
}
