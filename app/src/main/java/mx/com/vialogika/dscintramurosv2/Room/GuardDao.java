package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface GuardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Guard guard);

    @Query("SELECT * FROM Guards WHERE guardId = :id")
    Guard getGuardById(int id);
}
