package com.github.reapermaga.kpcli

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle

private val PREFIX =
    AttributedString(
        "!",
        AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN),
    )

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
