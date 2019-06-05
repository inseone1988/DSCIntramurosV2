package mx.com.vialogika.dscintramurosv2.Room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.view.View;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Person person);

    @Query("SELECT * FROM Persons WHERE idpersons = :id")
    Person getPersonByid(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Person person);
}
