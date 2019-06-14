package mx.com.vialogika.dscintramurosv2.Room;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface IncidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Incidencia incidencia);

    @Query("SELECT * FROM Incidences WHERE providerIncidencesDatetime BETWEEN :start AND :end")
    List<Incidencia> getIncidences(String start,String end);
}
