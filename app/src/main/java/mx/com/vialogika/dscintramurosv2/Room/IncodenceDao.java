package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

@Dao
public interface IncodenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(SiteIncidence incidence);
}
