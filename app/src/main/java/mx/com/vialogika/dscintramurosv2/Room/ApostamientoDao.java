package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

@Dao
public interface ApostamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Apostamiento apostamiento);

    @Query("SELECT * FROM Apostamientos WHERE plantillaPlaceId = :id")
    Apostamiento getApostamientoById(int id);
}
