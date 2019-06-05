package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

@Dao
public interface IncodenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(SiteIncidence incidence);
}
