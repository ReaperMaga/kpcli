package com.github.reapermaga.kpcli.wizard

import com.github.reapermaga.kpcli.processor.Dependencies
import com.github.reapermaga.kpcli.processor.GithubWorkflows
import org.jline.consoleui.prompt.CheckboxResult
import org.jline.consoleui.prompt.ConsolePrompt

class BaseWizardResult(
    override val dependencies: Set<String>,
    val githubWorkflows: Set<String>,
) : GradleWizardResult

class BaseWizard : Wizard<BaseWizardResult> {
    override fun prompt(prompt: ConsolePrompt): BaseWizardResult {
        val builder = prompt.promptBuilder
        // Dependencies
        builder
            .createCheckboxPrompt()
            .name("dependencies")
            .message("Please select the dependencies you want to include:")
            .newItem()
            .name(Dependencies.KTLINT)
            .text("Ktlint")
            .add()
            .newItem()
            .name(Dependencies.SHADOWJAR)
            .text("ShadowJar")
            .add()
            .newItem()
            .name(Dependencies.REAPERMAGA_COMMON_LIBRARY)
            .text("ReaperMaga's Common Library")
            .add()
            .addPrompt()

        // CI/CD
        builder
            .createCheckboxPrompt()
            .name("workflows")
            .message("Do you want any github workflows to be included?:")
            .newItem()
            .name(GithubWorkflows.CI)
            .text("Continuous integration")
            .add()
            .newItem()
            .name(GithubWorkflows.CI_KTLINT)
            .text("Continuous integration with Ktlint")
            .add()
            .addPrompt()
        val result = prompt.prompt(builder.build())
        val chosenDeps = (result["dependencies"] as CheckboxResult).selectedIds
        val chosenWorkflows = (result["workflows"] as CheckboxResult).selectedIds
        return BaseWizardResult(chosenDeps, chosenWorkflows)
    }
}
