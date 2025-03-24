package com.github.reapermaga.kpcli

import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle

fun emptySpace() {
    println("")
}

fun success(message: String) {
    val greenMessage =
        AttributedString(
            message,
            AttributedStyle.BOLD.foreground(AttributedStyle.GREEN),
        )
    terminal.writer().println(greenMessage.toAnsi(terminal))
}
