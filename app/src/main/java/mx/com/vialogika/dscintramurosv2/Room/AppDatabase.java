package mx.com.vialogika.dscintramurosv2.Room;



import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Person.class,Guard.class,Apostamiento.class,Incidencia.class,Plantilla.class},version = 2,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
    public abstract GuardDao guardDao();
    public abstract IncidenceDao incidenceDao();
    public abstract ApostamientoDao apostamientoDao();
    public abstract PlantillaDao plantillaDao();
}
