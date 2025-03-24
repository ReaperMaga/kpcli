package com.github.reapermaga.kpcli.util

import org.jline.consoleui.prompt.ConsolePrompt
import org.jline.terminal.Terminal
import org.jline.utils.OSUtils

fun produceJLineUIConfig(terminal: Terminal): ConsolePrompt.UiConfig {
    return when {
        terminal.type.equals(Terminal.TYPE_DUMB) || terminal.type.equals(Terminal.TYPE_DUMB_COLOR) -> {
            error("Dumb terminal detected.\nConsoleUi requires real terminal to work!\nNote: On Windows Jansi or JNA library must be included in classpath.")
        }
        OSUtils.IS_WINDOWS -> ConsolePrompt.UiConfig(">", "( )", "(x)", "( )")
        else -> ConsolePrompt.UiConfig("\u276F", "\u25EF ", "\u25C9 ", "\u25EF ")
    }
}