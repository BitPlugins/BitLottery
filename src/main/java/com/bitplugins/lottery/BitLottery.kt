package com.bitplugins.lottery

import com.bitplugins.lottery.core.BitPlugin
import com.bitplugins.lottery.core.database.User
import com.bitplugins.lottery.core.database.UserDatabaseService
import com.bitplugins.lottery.core.util.Logger
import com.github.shynixn.mccoroutine.bukkit.launch
import kotlinx.coroutines.Dispatchers

class BitLottery : BitPlugin() {

    override fun onEnable() {
        super.onEnable()

        testDb()
    }

    private fun testDb() = Companion.context.plugin.launch(Dispatchers.IO) {
        val path = "database"
        Logger.info("Checando de diretorio db existe")
        val file = context.fileUtils.createDirectory(path)

        val dbUrl = "jdbc:sqlite:${file?.absolutePath}/database.db"

        Logger.info("Acessando DB")
        val userDb = UserDatabaseService(dbUrl)

        Logger.info("Criando tabela")
        userDb.createTable()

        val user = User(id = "50c345df-a257-4a94-b040-490829e1011c", username = "choicedev", email = "choicedev@example.com")
        Logger.info("Inserindo usuário")
        userDb.insert(user)

        val users = userDb.getAll()
        Logger.info("All users: $users")

        val userId = user.id
        val fetchedUser = userDb.getByUUID(userId)
        Logger.info("User fetched by ID: $fetchedUser")

        val updatedUser = user.copy(
            email = "mudeio@emai.com",
        )
        Logger.info("Atualizando usuario")
        userDb.update(updatedUser)

    }

}