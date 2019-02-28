package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@Dao
public abstract class PlantillaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long save(Plantilla plantillaItem);

    @Insert
    public abstract void saveEdoFuerza(List<Plantilla> edo);

    @Query("SELECT COUNT(edoFuerzaGuardId) FROM Plantillas WHERE edoFuerzaDate BETWEEN :start AND :end AND edoFuerzaTurno = :turno")
    public abstract int getEdoGroupCount(String start,String end,String turno);

    @Query("SELECT DISTINCT(edoFuerzaTurno) FROM plantillas WHERE edoFuerzaDate BETWEEN :start AND :end")
    public abstract String[] getEdoFuerzaTurnosReported(String start,String end);

    @Query("SELECT * FROM Plantillas WHERE edoFuerzaDate BETWEEN :start AND :end AND edoFuerzaTurno = :grupo")
    public abstract List<Plantilla> getSavedPlantillaPlaces(String start,String end,String grupo);

    @Query("UPDATE plantillas SET edoFuerzaStatus = 0 WHERE edoFuerzaGuardId = :gid AND edoFuerzaTurno = :turno AND edoFuerzaDate BETWEEN :from AND :to")
    public abstract void deleteGuardFromPlantila(String gid,String turno,String from,String to);

    @Query("UPDATE Plantillas SET edoFuerzaPlantillaId = :id WHERE edoFuerzaDate BETWEEN :start AND :end AND edoFuerzaTurno = :turno")
    public abstract void flagPlantillaSaved(String id ,String start,String end,String turno);

    @Query("DELETE FROM plantillas WHERE  edoFuerzaTurno = :turno AND edoFuerzaDate BETWEEN :from AND :to")
    public abstract void deleteSavedData(String turno,String from,String to);

    @Transaction
    public void updateAfterServerSave(String turno,String from,String to, List<Plantilla> plantilla){
        deleteSavedData(turno,from,to);
        saveEdoFuerza(plantilla);
    }

}
