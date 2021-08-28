package rs.android.task4.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Cat(
    @PrimaryKey val id: UUID = UUID.randomUUID(),
    val name: String = "none",
    val birthday: Date = Date(),
    val breed: String = "None"
)