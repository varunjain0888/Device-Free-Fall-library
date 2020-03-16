package com.xbird.devicefreefalllib.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_count")
data class Count (@PrimaryKey(autoGenerate = true)
                  val id : Long=0,val freeFallCount : Long?)
