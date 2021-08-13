package com.example.hymnappconcept.repository

import com.example.hymnappconcept.database.HymnDao

class HymnRepository(private val hymnDao: HymnDao) {
    suspend fun allHymns() = hymnDao.getAllHymns()
    suspend fun getHymn(id: Int) = hymnDao.getHymn(id)
    suspend fun search(query: String) = hymnDao.search(query)
}
