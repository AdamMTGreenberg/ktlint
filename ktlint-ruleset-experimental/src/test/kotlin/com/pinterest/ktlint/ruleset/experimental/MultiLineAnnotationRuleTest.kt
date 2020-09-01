package com.pinterest.ktlint.ruleset.experimental

import com.pinterest.ktlint.core.LintError
import com.pinterest.ktlint.ruleset.experimental.MultiLineAnnotationRule.Companion.fileAnnotationsLineBreaks
import com.pinterest.ktlint.test.format
import com.pinterest.ktlint.test.lint
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class MultiLineAnnotationRuleTest {

    @Test
    fun `lint no empty lines between an annotation and object`() {
        assertThat(
            AnnotationRule().lint(
                """
                @JvmField
                fun foo() {}

                """.trimIndent()
            )
        ).isEmpty()
    }

    @Test
    fun `lint there should not be empty lines between an annotation and object`() {
        assertThat(
            AnnotationRule().lint(
                """
                @JvmField

                fun foo() {}

                """.trimIndent()
            )
        ).isEqualTo(
            listOf(
                LintError(1, 9, "annotation", fileAnnotationsLineBreaks)
            )
        )
    }

    @Test
    fun `lint there should not be empty lines between an annotation and object autocorrected`() {
        val code =
            """
            @JvmField

            fun foo() {}

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            fun foo() {}

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between an annotation and object autocorrected with control`() {
        val code =
            """
            @JvmField

            fun foo() {
              @JvmStatic
              val r = A()
            }

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            fun foo() {
              @JvmStatic
              val r = A()
            }

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between an annotation and object autocorrected multiple lines`() {
        val code =
            """
            @JvmField



            fun foo() {}

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            fun foo() {}

            """.trimIndent()
        )
    }

    @Test
    fun `lint annotation on the same line remains there`() {
        val code =
            """
            @JvmField fun foo() {}

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField fun foo() {}

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between multiple annotations`() {
        val code =
            """
            @JvmField @JvmStatic

            fun foo() = Unit

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField @JvmStatic
            fun foo() = Unit

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between multiple annotations on multiple lines`() {
        val code =
            """
            @JvmField
            @JvmStatic

            fun foo() = Unit

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            @JvmStatic
            fun foo() = Unit

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between multiple annotations with inline annotation`() {
        val code =
            """
            @JvmField

            @JvmName

            @JvmStatic fun foo() = Unit

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            @JvmName
            @JvmStatic
            fun foo() = Unit

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between two or more annotations`() {
        val code =
            """
            @JvmField

            @JvmName


            @JvmStatic


            fun foo() = Unit

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            @JvmName
            @JvmStatic
            fun foo() = Unit

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be empty lines between an annotation and object autocorrected multiple annotations`() {
        val code =
            """
            @JvmField



            fun foo() {
              @JvmStatic

              val foo = Foo()
            }

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            @JvmField
            fun foo() {
              @JvmStatic
              val foo = Foo()
            }

            """.trimIndent()
        )
    }

    @Test
    fun `lint there should not be an error on multiple lines assertion while additional formatting ongoing`() {
        val code =
            """
            package a.b.c

            class Test {
                fun bloop() {
                    asdfadsf(asdfadsf, asdfasdf, asdfasdfasdfads,
                    asdfasdf, asdfasdf, asdfasdf)
                }

                @Blah
                val test: Int
            }

            """.trimIndent()

        assertThat(
            AnnotationRule().format(code)
        ).isEqualTo(
            """
            package a.b.c

            class Test {
                fun bloop() {
                    asdfadsf(asdfadsf, asdfasdf, asdfasdfasdfads,
                    asdfasdf, asdfasdf, asdfasdf)
                }

                @Blah
                val test: Int
            }

            """.trimIndent()
        )
    }
}
