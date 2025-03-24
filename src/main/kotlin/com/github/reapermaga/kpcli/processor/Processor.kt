package com.github.reapermaga.kpcli.processor

import com.github.reapermaga.kpcli.Template
import com.github.reapermaga.kpcli.wizard.WizardResult
import java.io.File
import kotlin.reflect.KClass

val processors: Array<Processor<out WizardResult>> =
    arrayOf(
        GradleProcessor(),
        BaseProcessor(),
    )

interface Processor<T : WizardResult> {
    val wizardResultType: KClass<T>

    fun process(
        template: Template,
        folder: File,
        wizardResult: T,
    )

    @Suppress("UNCHECKED_CAST")
    fun unsafeProcess(
        template: Template,
        folder: File,
        wizardResult: WizardResult,
    ) {
        process(template, folder, wizardResult as T)
    }
}
