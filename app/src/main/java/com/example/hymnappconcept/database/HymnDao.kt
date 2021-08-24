package com.example.hymnappconcept.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface HymnDao {
    @Query("SELECT * FROM hymns_table")
    suspend fun getAllHymns(): List<HymnEntity>

    @Query("SELECT * FROM hymns_table WHERE :id = _id ")
    suspend fun getHymn(id: Int) : HymnEntity

    @Query(
        """SELECT hymns_table._id, hymns_table.title, hymns_table.author, hymns_table.lyrics 
                 FROM hymns_table
                 JOIN hymns_fts ON hymns_fts.rowid = hymns_table._id  
                 WHERE hymns_fts MATCH :query"""
    )
    suspend fun search(query: String): List<HymnEntity>
}
