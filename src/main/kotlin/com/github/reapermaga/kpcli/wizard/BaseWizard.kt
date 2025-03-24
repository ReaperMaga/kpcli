package com.github.reapermaga.kpcli.wizard

import org.jline.consoleui.prompt.CheckboxResult
import org.jline.consoleui.prompt.ConsolePrompt

class BaseWizardResult(
    override val dependencies: Set<String>,
) : GradleWizardResult {
    override fun toString(): String = "BaseWizardResult: [deps: ${dependencies.joinToString(", ")}]"
}

class BaseWizard : Wizard<BaseWizardResult> {
    override fun prompt(prompt: ConsolePrompt): BaseWizardResult {
        val builder = prompt.promptBuilder
        builder
            .createCheckboxPrompt()
            .name("dependencies")
            .message("Please select the dependencies you want to include:")
            .newItem()
            .name("ktlint")
            .text("Ktlint")
            .add()
            .newItem()
            .name("reapermaga-common-library")
            .text("ReaperMaga's Common Library")
            .add()
            .addPrompt()
        val result = prompt.prompt(builder.build())
        val chosenDeps = (result["dependencies"] as CheckboxResult).selectedIds
        return BaseWizardResult(chosenDeps)
    }
}
