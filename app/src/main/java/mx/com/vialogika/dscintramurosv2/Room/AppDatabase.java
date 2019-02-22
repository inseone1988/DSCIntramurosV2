package mx.com.vialogika.dscintramurosv2.Room;



import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Person.class,Guard.class,Apostamiento.class,Incidencia.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
    public abstract GuardDao guardDao();
    public abstract IncidenceDao incidenceDao();
    public abstract ApostamientoDao apostamientoDao();
}
