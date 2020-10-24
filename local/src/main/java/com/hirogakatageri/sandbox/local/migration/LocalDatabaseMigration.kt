package com.hirogakatageri.sandbox.local.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object LocalDatabaseMigration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            renameTableColumns(
                database,
                "users",
                listOf(
                    Triple("rowid", "INTEGER", "PRIMARY KEY NOT NULL"),
                    Triple("username", "TEXT", "NOT NULL"),
                    Triple("followers", "INTEGER", "NOT NULL"),
                    Triple("followings", "INTEGER", "NOT NULL"),
                    Triple("profile_image_url", "TEXT", "NULL"),
                    Triple("name", "TEXT", "NULL"),
                    Triple("company_name", "TEXT", "NULL"),
                    Triple("blogUrl", "TEXT", "NULL"),
                    Triple("notes", "TEXT", "NULL"),
                    Triple("htmlUrl", "TEXT", "NULL")
                ),
                listOf(
                    Triple("rowid", "INTEGER", "PRIMARY KEY NOT NULL"),
                    Triple("username", "TEXT", "NOT NULL"),
                    Triple("followers", "INTEGER", "NOT NULL"),
                    Triple("followings", "INTEGER", "NOT NULL"),
                    Triple("profile_image_url", "TEXT", "NULL"),
                    Triple("name", "TEXT", "NULL"),
                    Triple("company_name", "TEXT", "NULL"),
                    Triple("blog_url", "TEXT", "NULL"),
                    Triple("notes", "TEXT", "NULL"),
                    Triple("html_url", "TEXT", "NULL")
                )
            )

            dropTable(database, "users_fts")
            createVirtualTable(
                database,
                "users_fts",
                "users",
                listOf(
                    "username",
                    "html_url",
                    "profile_image_url",
                    "notes"
                )
            )
        }
    }

    /**
     * Renames Table columns.
     * @param tableName Table Name to alter.
     * @param oldColumns Old Column Pairings <name, dataType, constraint>.
     * @param newColumns New Column Pairings <name, dataType, constraint>.
     * */
    private fun renameTableColumns(
        database: SupportSQLiteDatabase,
        tableName: String,
        oldColumns: List<Triple<String, String, String>>,
        newColumns: List<Triple<String, String, String>>
    ) {
        val sqlBuilder = StringBuilder()

        //Create temporary table to store values.
        val createString = "CREATE TABLE ${tableName}_temp "
        sqlBuilder.append(createString)
        sqlBuilder.append("(")
        newColumns.forEachIndexed { index, (name, dataType, constraint) ->
            sqlBuilder.append(createColumnString(name, dataType, constraint))
            if ((index + 1) < newColumns.size) sqlBuilder.append(",")
        }
        sqlBuilder.append(")")

        database.execSQL(sqlBuilder.toString())
        sqlBuilder.clear()

        //Start copying files into temp table.
        val copyString = "INSERT INTO ${tableName}_temp "
        sqlBuilder.append(copyString)
        sqlBuilder.append("(")
        newColumns.forEachIndexed { index, (name, _, _) ->
            sqlBuilder.append(name)
            if (index + 1 < newColumns.size) sqlBuilder.append(",")
        }
        sqlBuilder.append(") ")
        sqlBuilder.append("SELECT ")
        oldColumns.forEachIndexed { index, (name, _, _) ->
            sqlBuilder.append(name)
            if (index + 1 < newColumns.size) sqlBuilder.append(",")
        }
        sqlBuilder.append(" FROM $tableName")

        database.execSQL(sqlBuilder.toString())
        sqlBuilder.clear()

        //Remove old table.
        dropTable(
            database,
            tableName
        )

        //Rename temp table.
        renameTable(
            database,
            "${tableName}_temp",
            tableName
        )
    }

    private fun dropTable(database: SupportSQLiteDatabase, tableName: String) {
        database.execSQL("DROP TABLE $tableName")
    }

    private fun renameTable(
        database: SupportSQLiteDatabase,
        oldTableName: String,
        newTableName: String
    ) {
        database.execSQL("ALTER TABLE $oldTableName RENAME TO $newTableName")
    }

    private fun createVirtualTable(
        database: SupportSQLiteDatabase,
        tableName: String,
        contentTableName: String,
        columns: List<String>
    ) {
        val sqlBuilder = StringBuilder()

        sqlBuilder.append("CREATE VIRTUAL TABLE $tableName USING fts4")
        sqlBuilder.append("(content=`$contentTableName`,")
        columns.forEachIndexed { index, name ->
            sqlBuilder.append(name)
            if (index + 1 < columns.size) sqlBuilder.append(",")
        }
        sqlBuilder.append(")")
        database.execSQL(sqlBuilder.toString())
    }

    private fun createColumnString(name: String, dataType: String, constraint: String) =
        StringBuilder().append("$name $dataType $constraint")


}