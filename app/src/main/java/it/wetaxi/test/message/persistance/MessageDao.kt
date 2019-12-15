package it.wetaxi.test.message.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import it.wetaxi.test.message.models.Message

@Dao
interface MessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: Message?)

    @Query("SELECT * FROM messages")
    fun getMessages() : List<Message>

}