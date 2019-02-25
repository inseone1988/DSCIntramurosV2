package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ApostamientoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Apostamiento apostamiento);

    @Query("SELECT * FROM Apostamientos WHERE plantillaPlaceId = :id")
    Apostamiento getApostamientoById(int id);

    @Query("SELECT SUM(plantillaPlaceGuardsRequired) FROM apostamientos")
    int getEdoRequired();

    @Query("SELECT * FROM Apostamientos")
    List<Apostamiento> getAllApostamientos();
}
