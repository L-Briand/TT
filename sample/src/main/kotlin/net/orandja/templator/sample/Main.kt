package net.orandja.templator.sample

import net.orandja.templator.TT
import net.orandja.templator.asGroup
import net.orandja.templator.asRoll
import net.orandja.templator.asTemplateRender
import net.orandja.templator.renderToString


class Main : Runnable {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            Main().run()
        }
    }

    data class UserInformation(val firstName: String, val lastName: String)

    override fun run() {
        val user = UserInformation("ABC", "DEF")
        val users = listOf(
            user,
            UserInformation("ABC", "DEF"),
            UserInformation("ABC", "BAR"),
        )

        val userTemplate = TT.template("Hello {{ firstName }}, is your lastname {{ lastName }} ?")

        val templates = with(TT) {
            val userInfo1 = group(
                mapOf(
                    "firstName" to value(user.firstName),
                    "lastName" to value(user.lastName),
                )
            )
            val userInfo2 = user.asGroup()

            val usersInfo1 = roll(users.map { it.asGroup() })
            val usersInfo2 = users.asRoll()

            group(
                "template" to template("Test : {{ users3 }}"),
                "user1" to onContext(userTemplate, userInfo1),
                "user2" to onContext(userTemplate, userInfo2),
                "user3" to user.asTemplateRender(userTemplate),
                "users1" to onContext(repeat(users.size, userTemplate), usersInfo1),
                "users2" to onContext(repeat(users.size, userTemplate), usersInfo2),
                "users3" to users.asTemplateRender(userTemplate),
            )
        }

        println(templates.get("user3"))
        println(templates.renderToString("user3"))
    }
}