package com.github.reapermaga.kpcli.processor

import com.github.reapermaga.kpcli.Template
import com.github.reapermaga.kpcli.wizard.BaseWizardResult
import com.github.reapermaga.library.common.getFileFromResources
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
        val templateDirectory = "workflow_templates/"
        wizardResult.githubWorkflows.forEach {
            if (it == GithubWorkflows.CI) {
                getFileFromResources(templateDirectory.plus("ci.yaml")).apply {
                    copyTo(File(workflowsDirectoryPath.plus("ci.yaml")))
                }
            } else if (it == GithubWorkflows.CI_KTLINT) {
                getFileFromResources(templateDirectory.plus("ci_ktlint.yaml")).apply {
                    copyTo(File(workflowsDirectoryPath.plus("ci_ktlint.yaml")))
                }
            }
        }
    }
}
