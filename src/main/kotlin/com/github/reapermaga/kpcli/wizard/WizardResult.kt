package com.github.reapermaga.kpcli.wizard

interface WizardResult

interface GradleWizardResult : WizardResult {
    val dependencies: Set<String>
}