package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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

}
