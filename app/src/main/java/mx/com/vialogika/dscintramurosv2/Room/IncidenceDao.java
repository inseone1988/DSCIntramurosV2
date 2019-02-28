package mx.com.vialogika.dscintramurosv2.Room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.widget.AdapterView;

import java.util.List;

@Dao
public interface IncidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Incidencia incidencia);

    @Query("SELECT * FROM Incidences WHERE providerIncidencesDatetime BETWEEN :start AND :end")
    List<Incidencia> getIncidences(String start,String end);
}
