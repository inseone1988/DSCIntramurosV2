package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class DatabaseOperations {

    private static volatile DatabaseOperations dbo;
    private static          AppDatabase        db;

    private DatabaseOperations(Context context) {

    }

    public static DatabaseOperations getInstance(Context context) {
        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "Database").build();
        }
        if (dbo == null) {
            synchronized (DatabaseOperations.class) {
                if (dbo == null) dbo = new DatabaseOperations(context);
            }
        }
        return dbo;
    }

    public void SyncGuards(final List<Guard> guards) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int updated = 0;
                int created = 0;
                for (int i = 0; i < guards.size(); i++) {
                    Guard current  = guards.get(i);
                    Guard existent = db.guardDao().getGuardById(current.getGuardId());
                    if (existent != null) {
                        existent.update(current);
                        long saved = db.guardDao().save(existent);
                        if (saved != 0) {
                            updated += 1;
                        }
                    } else {
                        db.guardDao().save(current);
                        created +=1;
                    }
                }
                String message = String.valueOf(updated)+": Guardias actualizados, "+String.valueOf(created)+": Guardias creados";
                Log.d("Room",message);
            }
        }).start();
    }

    public void SyncPersons(final List<Person> personList){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int updated = 0;
                int created = 0;
                for (int i = 0; i < personList.size(); i++) {
                    Person current = personList.get(i);
                    Person exists = db.personDao().getPersonByid(current.getIdpersons());
                    if (exists != null){
                        exists.update(current);
                        long saved = db.personDao().save(exists);
                        if (saved != 0){
                            updated += 1;
                        }
                    }else{
                        db.personDao().save(current);
                        created += 1;
                    }
                }
                String message = String.valueOf(updated)+": Personas actualizados, "+String.valueOf(created)+": Personas creados";
                Log.d("Room",message);
            }
        }).start();
    }
}
