package com.github.reapermaga.kpcli.wizard

import org.jline.consoleui.prompt.ConsolePrompt

interface Wizard<T : WizardResult> {

    fun prompt(prompt: ConsolePrompt): T

}