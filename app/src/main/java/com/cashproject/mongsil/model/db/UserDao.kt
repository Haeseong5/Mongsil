package com.cashproject.mongsil.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cashproject.mongsil.model.data.Saying
import com.google.firebase.firestore.auth.User

@Dao
interface LockerDao {
    @Query("SELECT * FROM Saying")
    fun getAll(): List<Saying>



    @Query(
        "SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1"
    )
    fun findByName(first: String, last: String): Saying

    @Insert
    fun insertAll(vararg users: Saying)

    @Delete
    fun delete(user: Saying)
}