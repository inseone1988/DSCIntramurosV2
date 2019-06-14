package mx.com.vialogika.dscintramurosv2.Room;



import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Person.class,Guard.class,Apostamiento.class,Incidencia.class,Plantilla.class,SiteIncidence.class},version = 6,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PersonDao personDao();
    public abstract GuardDao guardDao();
    public abstract IncidenceDao incidenceDao();
    public abstract ApostamientoDao apostamientoDao();
    public abstract PlantillaDao plantillaDao();
    public abstract IncodenceDao incodenceDao();
}
