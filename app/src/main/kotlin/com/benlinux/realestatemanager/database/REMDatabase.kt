package com.benlinux.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.benlinux.realestatemanager.dao.PropertyDao
import com.benlinux.realestatemanager.ui.models.Picture
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.PropertyAddress
import com.benlinux.realestatemanager.ui.models.User
import com.benlinux.realestatemanager.utils.converters.Converters
import com.benlinux.realestatemanager.utils.subscribeOnBackground


@Database(entities = [Property::class, User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class REMDatabase : RoomDatabase() {

    abstract fun propertyDao(): PropertyDao

    companion object {
        private var instance: REMDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): REMDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext, REMDatabase::class.java,
                    "rem_database"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build()

            return instance!!

        }

        private val roomCallback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                populateDatabase(instance!!)
            }
        }

        // Create 3 property & 2 realtors in database
        private fun populateDatabase(db: REMDatabase) {
            val propertyDao = db.propertyDao()
            subscribeOnBackground {
                propertyDao.insertProperty(Property(1, "Flat", "Marvellous flat", "Paris", 1200000,
                    250, "Marvellous flat in Manhattan with tremendous options...",
                    mutableListOf(Picture(
                        "https://media.cntraveler.com/photos/60f88f63c28e94c67dd51fbd/master/pass/airbnb%2019839441.jpeg",
                        "Lounge")),
                    PropertyAddress("12", "rue de la Paix", "", "75000", "Paris","France"),
                    true, "28/11/2022", "", "",
                    User("1", "ben@test.com", "Ben", "Linux",""), 0,0,0  )
                )
                propertyDao.insertProperty(Property(2, "Duplex", "Fabulous duplex", "London", 2200000,
                    300, "Fabulous Duplex in London with tremendous options...", mutableListOf(Picture(
                        "https://images.unsplash.com/photo-1568605114967-8130f3a36994?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8M3x8fGVufDB8fHx8&w=1000&q=80",
                        "Exterior")),
                    PropertyAddress("10", "Downing Street", "", "SW1", "London","United Kingdom"),
                    true, "28/11/2022", "", "",
                    User("1", "ben@test.com", "Ben", "Linux",""),0,0,0)
                )
                propertyDao.insertProperty(Property(3, "Penthouse", "Exceptional penthouse", "Manhattan", 5200000,
                    300, "Exceptional penthouse in Manhattan with tremendous options...", mutableListOf(Picture(
                        "https://media.architecturaldigest.com/photos/5c0817ec1b58382d031ba321/2:1/w_4800,h_2400,c_limit/Eighty%20Seven%20Park%20Penthouse%20Family%20Room.jpg",
                        "Lounge")),
                    PropertyAddress("66", "Perry Street", "", "NY 10014", "New York", "United States"),
                    true, "28/11/2022", "", "",
                    User("2", "franck@test.com", "Franck", "Black",""),0,0,0 )
                )
                propertyDao.insertProperty(Property(4, "Penthouse", "Unbelievable penthouse", "GooglePlex", 4300000,
                    300, "Unbelievable penthouse near GooglePlex with tremendous options...",
                    mutableListOf(Picture(
                        "https://s.hdnux.com/photos/01/23/56/41/21948520/4/rawImage.jpg",
                        "Exterior")),
                    PropertyAddress("1024", "Alta avenue", "", "94043", "Mountain View","United States"),
                    true, "13/12/2022", "", "",
                    User("1", "ben@test.com", "Ben", "Linux",""), 0,0,0  )
                )
            }
        }
    }
}


