package com.github.reapermaga.kpcli

import org.jline.consoleui.prompt.ConsolePrompt
import org.jline.terminal.Terminal
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.jline.utils.OSUtils

private val PREFIX =
    AttributedString(
        "!",
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN),
    )

fun getHeader(loader: ClassLoader): AttributedString {
    val text = loader.getResource("HEADER")?.readText()
    return AttributedString(text)
}

fun printSpace() {
    println("")
}

fun printSuccess(message: String) {
    val successMessage =
        AttributedString(
            message,
            AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW),
        )
    terminal.writer().println("${PREFIX.toAnsi(terminal)} ${successMessage.toAnsi(terminal)}")
}

fun printError(message: String) {
    val errMessage =
        AttributedString(
            message,
            AttributedStyle.BOLD.foreground(AttributedStyle.RED),
        )
    terminal.writer().println("${PREFIX.toAnsi(terminal)} ${errMessage.toAnsi(terminal)}")
}

fun produceJLineUIConfig(terminal: Terminal): ConsolePrompt.UiConfig =
    when {
        terminal.type.equals(Terminal.TYPE_DUMB) || terminal.type.equals(Terminal.TYPE_DUMB_COLOR) -> {
            error(
                "Dumb terminal detected.\nConsoleUi requires real terminal to work!\nNote: On Windows Jansi or JNA library must be included in classpath.",
            )
        }
        OSUtils.IS_WINDOWS -> ConsolePrompt.UiConfig(">", "( ) ", "(x) ", "( ) ")
        else -> ConsolePrompt.UiConfig("\u276F", "\u25EF ", "\u25C9 ", "\u25EF ")
    }
