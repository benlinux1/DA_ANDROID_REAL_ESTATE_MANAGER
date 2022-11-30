package com.benlinux.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.benlinux.realestatemanager.ui.models.Property
import com.benlinux.realestatemanager.ui.models.Realtor


/**
 * Sets DAO Pattern Interface to group access to persistent data
 */
@Dao
interface PropertyDao {

    // Insert Property in the table
    @Insert
    fun insertProperty(property: Property)

    // Update Property in the table
    @Update
    fun updateProperty(property: Property)

    // Delete Property in the table
    @Delete
    fun deleteProperty(property: Property)

    // Delete all properties in the table
    @Query("delete from property_table")
    fun deleteAllProperties()

    // Deletes a given property in the table
    @Query("DELETE FROM property_table WHERE property_id = :id")
    fun deletePropertyById(id: Int): Int

    // Get all properties from the table
    @Query("select * from property_table order by property_id asc")
    fun getAllProperties(): LiveData<MutableList<Property?>>

    // Gets a given property by id
    @Query("SELECT * FROM property_table WHERE property_id = :id")
    fun getPropertyById(id: Int): LiveData<Property>

}