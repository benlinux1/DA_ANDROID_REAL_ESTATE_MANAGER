package com.benlinux.realestatemanager.ui.models

import androidx.room.*
import com.benlinux.realestatemanager.utils.converters.Converters


@Entity(tableName = "property_table")
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
data class Property (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "property_id")
    var id: Int = 0,
    var type: String = "",
    var name: String = "",
    var area: String = "",
    var price: Int = 0,
    var surface: Int = 0,
    var description: String = "",
    @TypeConverters(Converters::class)
    var pictures: MutableList<Picture?> = mutableListOf(),
    @TypeConverters(Converters::class)
    var address: PropertyAddress = PropertyAddress("", "", "", "", "",""),
    var isAvailable: Boolean = true,
    var creationDate: String = "29/11/2022",
    var soldDate: String = "",
    var updateDate: String = "",
    @TypeConverters(Converters::class)
    var realtor: User = User("0", "", "", "", ""),
    var numberOfRooms: Int = 0,
    var numberOfBathrooms: Int = 0,
    var numberOfBedrooms: Int = 0

)

