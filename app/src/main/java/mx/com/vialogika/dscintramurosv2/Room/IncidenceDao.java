package mx.com.vialogika.dscintramurosv2.Room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.widget.AdapterView;

@Dao
public interface IncidenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Incidencia incidencia);
}
