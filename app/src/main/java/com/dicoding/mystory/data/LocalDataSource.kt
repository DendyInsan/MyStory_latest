package com.dicoding.mystory.data

import com.dicoding.mystory.database.StoryDao

class LocalDataSource (private val dao: StoryDao){
    fun getStories() = dao.getAllStoryMap()

    companion object {

        fun getMap(
           dao: StoryDao

        ): LocalDataSource {
            return LocalDataSource(dao)
        }
    }
}