package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GuardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Guard guard);

    @Query("SELECT * FROM Guards WHERE guardId = :id")
    Guard getGuardById(int id);

    @Query("SELECT * FROM Guards WHERE guardStatus != 0")
    List<Guard> getActiveElements();

    @Query("SELECT * FROM Guards WHERE guardHash= :gHash")
    Guard getGuardByHash(String gHash);

    @Query("SELECT * FROM Guards ORDER BY guardId ASC")
    List<Guard> getAllGuards();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(Guard guard);

}
