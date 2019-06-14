package mx.com.vialogika.dscintramurosv2.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Person person);

    @Query("SELECT * FROM Persons WHERE idpersons = :id")
    Person getPersonByid(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Person person);
}
