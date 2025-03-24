package com.github.reapermaga.kpcli

import com.github.reapermaga.kpcli.processor.processors
import com.github.reapermaga.kpcli.util.produceJLineUIConfig
import com.github.reapermaga.kpcli.wizard.WizardResult
import org.jline.consoleui.prompt.ConsolePrompt
import org.jline.consoleui.prompt.InputResult
import org.jline.consoleui.prompt.ListResult
import org.jline.reader.LineReaderBuilder
import org.jline.terminal.TerminalBuilder
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.jline.utils.Colors

val terminal = TerminalBuilder.builder().build()
val reader = LineReaderBuilder.builder().terminal(terminal).build()

fun main() {
   val prompt = ConsolePrompt(reader, terminal, produceJLineUIConfig(terminal))
   val builder = prompt.promptBuilder
   //Template
   builder.createListPrompt().name("template").message("Please select your template:").apply {
      templates.forEach {
         newItem().name(it.name).text(it.name).add()
      }
   }.addPrompt()
   //Project name
   builder.createInputPrompt().name("name").message("What will your project be called?:").addPrompt()
   val result = prompt.prompt(builder.build())
   val template = result
      .firstNotNullOfOrNull {
         if (it.key == "template") (it.value as ListResult).selectedId else null
      }?.let { templates.firstOrNull { templ -> templ.name == it } } ?: error("Template not found")
   val projectName = result
      .firstNotNullOfOrNull {
         if (it.key == "name") (it.value as InputResult).result else null
      }
   if (projectName == null || projectName == "null") {
      println("Please provide a valid project name")
      return
   }
   if (projectName.isEmpty()) {
      println("Project name cannot be empty")
      return
   }
   val wizardResult: WizardResult = template.wizard.prompt(prompt)
   val folder = downloadTemplate(template, projectName)
   processors.forEach {
      if (it.wizardResultType == wizardResult::class || it.wizardResultType.java.isAssignableFrom(wizardResult::class.java)) {
         it.unsafeProcess(template, folder, wizardResult)
      }
   }
   success("Project $projectName successfully initialized")
}

fun success(message: String) {
   val greenMessage = AttributedString(
      message,
      AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)
   );
   terminal.writer().println(greenMessage.toAnsi(terminal))
}


