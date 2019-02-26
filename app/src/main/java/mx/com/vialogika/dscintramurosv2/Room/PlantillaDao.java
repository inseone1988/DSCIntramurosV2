package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlantillaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long save(Plantilla plantillaItem);

    @Query("SELECT COUNT(edoFuerzaGuardId) FROM Plantillas WHERE edoFuerzaDate BETWEEN :start AND :end AND edoFuerzaTurno = :turno")
    int getEdoGroupCount(String start,String end,String turno);

    @Query("SELECT DISTINCT(edoFuerzaTurno) FROM plantillas WHERE edoFuerzaDate BETWEEN :start AND :end")
    String[] getEdoFuerzaTurnosReported(String start,String end);

    @Query("SELECT * FROM Plantillas WHERE edoFuerzaDate BETWEEN :start AND :end AND edoFuerzaTurno = :grupo")
    List<Plantilla> getSavedPlantillaPlaces(String start,String end,String grupo);
}
