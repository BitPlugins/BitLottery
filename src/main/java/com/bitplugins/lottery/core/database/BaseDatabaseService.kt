package com.bitplugins.lottery.core.database

import com.bitplugins.lottery.core.util.Logger
import org.bukkit.Location
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

abstract class DatabaseService<T>(private val dbUrl: String) {

    init {
        val driver = when {
            dbUrl.startsWith("jdbc:sqlite") -> "org.sqlite.JDBC"
            dbUrl.startsWith("jdbc:mysql") -> "com.mysql.cj.jdbc.Driver"
            dbUrl.startsWith("jdbc:postgresql") -> "org.postgresql.Driver"
            else -> throw IllegalArgumentException("Unsupported database type in URL: $dbUrl")
        }

        Database.connect(dbUrl, driver = driver)
    }

    abstract fun createTable()

    abstract fun insert(entity: T)

    abstract fun getAll(): List<T>

    abstract fun getById(id: Int): T?

    abstract fun update(entity: T)

    abstract fun delete(id: Int)

    protected fun createSchema(schemaName: String) {
        transaction {
            exec("CREATE SCHEMA IF NOT EXISTS $schemaName")
        }
    }

    protected fun connectToSchema(schemaName: String) {
        transaction {
            exec("SET search_path TO $schemaName") // Para bancos como PostgreSQL ou similar
        }
    }
}

object UserTable : Table("users") {
    val uuid = varchar("uuid", 255)
    val username = varchar("username", 255)
    val email = varchar("email", 255).nullable()

    override val primaryKey = PrimaryKey(uuid, name = "PK_User_ID")
}

class UserDatabaseService(dbUrl: String) : DatabaseService<User>(dbUrl) {

    override fun createTable() {
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    override fun insert(entity: User) {
        transaction {
            val existingUser = getByUUID(entity.id)
            if (existingUser == null) {
                UserTable.insert {
                    it[uuid] = entity.id
                    it[username] = entity.username
                    it[email] = entity.email
                }
            } else {
                Logger.error("User with UUID ${entity.id} already exists.")
            }
        }
    }


    override fun getAll(): List<User> {
        return transaction {
            UserTable.selectAll().map {
                User(it[UserTable.uuid],it[UserTable.username], it[UserTable.email])
            }
        }
    }

    override fun getById(id: Int): User? {
        return null
    }

    fun getByUUID(uuid: String): User? {
        return transaction {
            UserTable.select { UserTable.uuid eq uuid }
                .map { User(it[UserTable.uuid], it[UserTable.username], it[UserTable.email]) }
                .singleOrNull()
        }
    }

    override fun update(entity: User) {
        transaction {
            UserTable.update({ UserTable.username eq entity.username }) {
                it[email] = entity.email
            }
        }
    }

    override fun delete(id: Int) {}

    fun deleteByUuid(uuid: String) {
        return transaction {
            UserTable.deleteWhere { UserTable.uuid eq uuid }
        }
    }
}

data class User(val id: String, val username: String, val email: String? = null)
