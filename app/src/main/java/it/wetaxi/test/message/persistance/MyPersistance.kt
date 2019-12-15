package it.wetaxi.test.message.persistance

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import it.wetaxi.test.message.models.Message

@Database(
    entities = [Message::class],
    version = 1
)
abstract class MyPersistance : RoomDatabase() {

    abstract fun messageDao(): MessageDao

    companion object {
        private const val DB_NAME = "persistance_db"
        private var INSTANCE: MyPersistance? = null
        fun getInstance(context: Context): MyPersistance? {
            if (INSTANCE == null) {
                synchronized(MyPersistance::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            MyPersistance::class.java, DB_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

