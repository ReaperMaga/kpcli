package com.github.reapermaga.kpcli

import com.github.reapermaga.kpcli.processor.processors
import com.github.reapermaga.kpcli.wizard.WizardResult
import org.jline.consoleui.prompt.ConsolePrompt
import org.jline.consoleui.prompt.InputResult
import org.jline.consoleui.prompt.ListResult
import org.jline.reader.LineReader
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder

val terminal: Terminal = TerminalBuilder.builder().build()
val reader: LineReader = LineReaderBuilder.builder().terminal(terminal).build()

fun main(vararg args: String) {
    if (args.isEmpty()) {
        printError("Please provide a path")
        return
    }
    val prompt = ConsolePrompt(reader, terminal, produceJLineUIConfig(terminal))
    val builder = prompt.promptBuilder
    // Template
    builder
        .createListPrompt()
        .name("template")
        .message("Please select your template:")
        .apply {
            templates.forEach {
                newItem().name(it.name).text(it.name).add()
            }
        }.addPrompt()
    // Project name
    builder
        .createInputPrompt()
        .name("name")
        .message("What will your project be called?:")
        .addPrompt()
    val result = prompt.prompt(mutableListOf(getHeader(Template::class.java.classLoader)), builder.build())

    val template =
        result
            .firstNotNullOfOrNull {
                if (it.key == "template") (it.value as ListResult).selectedId else null
            }?.let { templates.firstOrNull { templ -> templ.name == it } }
    if (template == null) {
        printError("Template not found")
        return
    }
    val projectName =
        result
            .firstNotNullOfOrNull {
                if (it.key == "name") (it.value as InputResult).result else null
            }
    if (projectName == null || projectName == "null") {
        printError("Please provide a valid project name")
        return
    }
    if (projectName.isEmpty()) {
        printError("Project name cannot be empty")
        return
    }
    val wizardResult: WizardResult = template.wizard.prompt(prompt)
    val folder = downloadTemplate(template, args[0], projectName)
    processors.forEach {
        if (it.wizardResultType == wizardResult::class || it.wizardResultType.java.isAssignableFrom(wizardResult::class.java)) {
            it.unsafeProcess(template, folder, wizardResult)
        }
    }
    printSpace()
    printSuccess("Project $projectName successfully initialized")
}
