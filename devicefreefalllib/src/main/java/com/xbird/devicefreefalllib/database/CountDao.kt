package com.xbird.devicefreefalllib.database

import androidx.room.*
import com.xbird.devicefreefalllib.database.Count


@Dao
interface CountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data : Count)
    @Query("Select SUM(freeFallCount) from tb_count")
    fun getSum():Long
    @Query("Delete from tb_count")
    fun deleteAll()
}