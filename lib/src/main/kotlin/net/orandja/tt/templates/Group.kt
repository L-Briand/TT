package net.orandja.tt.templates

import net.orandja.tt.TemplateProvider
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.asTemplateProvider

class Group(
    val provider: TemplateProvider
) : TemplateRenderer() {

    constructor(map: Map<String, TemplateRenderer>) : this(map.asTemplateProvider())

    override fun toString(): String =
        "group { ${
        provider.keys().joinToString(prefix = "\n", separator = ",\n", postfix = "\n") { "$it:${provider[it]}" }
        } }"

    override suspend fun render(
        key: String?,
        contexts: Array<TemplateRenderer>,
        onNew: (CharSequence) -> Unit
    ): Boolean = key?.let(provider::get)?.render(null, mergeContexts(contexts), onNew) ?: false

    override fun duplicate(): TemplateRenderer = Group(provider.duplicate())

    override suspend fun validateTag(key: String): Boolean = provider.keys().contains(key)
    override fun getExternalTemplate(key: String): TemplateRenderer? = provider[key] ?: context?.get(key)
}
