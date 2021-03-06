package net.orandja.tt.sample

import net.orandja.tt.TT
import net.orandja.tt.TemplateRenderer
import net.orandja.tt.assertEqual
import net.orandja.tt.renderToString

fun block2() {

    // Given:
    val block = TT.block(
        """
            i'm a TT.value template
            --{ template
                I'm a TT.template named {{ name }}
                --{ case1 Dory}--
                --{ case2 Nemo}--
            }--
        """
    )

    // The obvious way to create templates from block are to simply take
    // the content of a block and transform it into a template
    val template1 = TT.template(block["template"])

    // But creating all templates inside a block can be tedious.
    // The method asTemplateMap is here to help.
    // It creates a map of templates where all templates are accessible by path.
    //
    // Both delimiters and path separator can be set.
    // By default, it's like this : separator = ".", delimiters = "{{" - "}}"
    val templates: Map<String, TemplateRenderer> = block.asTemplateMap()

    // The key of the root template is an empty string
    assertEqual("i'm a TT.value template", templates[""]!!.renderToString())
    // An inner template is accessible with its path.
    // It requires a dot at start since it's inside the root.
    assertEqual("Dory", templates[".template.case1"]!!.renderToString())

    // Having a map of templates is not really useful for linking two templates together
    // You still have to bind one another manually
    // This is handled in block3
    val blockInsideTemplate = templates[".template"]!! bindTo TT.templates(
        "name" to templates[".template.case2"]!!
    )
    assertEqual("I'm a TT.template named Nemo", blockInsideTemplate.renderToString())
}
