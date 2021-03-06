package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Room;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import android.util.Log;

import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.GlobalAplication;
import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Utils.CryptoHash;
import mx.com.vialogika.dscintramurosv2.Utils.TimeUtils;

public class DatabaseOperations {

    private static volatile DatabaseOperations dbo;
    private static          AppDatabase        db;
    public static Handler handler = new Handler(Looper.getMainLooper());

    private DatabaseOperations(Context context) {

    }

    public static DatabaseOperations getInstance() {
        if (db == null) {
            db = Room.databaseBuilder(GlobalAplication.getAppContext(), AppDatabase.class, "Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        if (dbo == null) {
            synchronized (DatabaseOperations.class) {
                if (dbo == null) dbo = new DatabaseOperations(GlobalAplication.getAppContext());
            }
        }
        return dbo;
    }

    public void SyncGuards(final List<Guard> guards,boolean downloadPictures) {
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

    public void SyncApostamientos(final List<Apostamiento> apostamientos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int updated = 0;
                int created = 0;
                for (int i = 0; i < apostamientos.size(); i++) {
                    Apostamiento current = apostamientos.get(i);
                    Apostamiento existent = db.apostamientoDao().getApostamientoById(current.getPlantillaPlaceId());
                    if (existent != null){
                        existent.update(current);
                        long saved = db.apostamientoDao().save(existent);
                        if (saved != 0){
                            updated += 1;
                        }
                    }else{
                        db.apostamientoDao().save(current);
                        created += 1;
                    }
                }
                String message = String.valueOf(updated)+": Apostamientos actualizados, "+String.valueOf(created)+": Apostamientos creados";
                Log.d("Room",message);
            }
        }).start();
    }

    public void getGroupData(final String grupo,final backgroundOperation cb){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int gReported = db.plantillaDao().getEdoGroupCount(from,to,grupo);
                int gRequired = db.apostamientoDao().getEdoRequired();
                int[] reported = new int[]{gReported,gRequired};
                cb.onOperationFinished(reported);
            }
        }).start();
    }

    public void getReportedGroups(final backgroundOperation cb,final UIThreadOperation uiop){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] reported = db.plantillaDao().getEdoFuerzaTurnosReported(from,to);
                cb.onOperationFinished(reported);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        uiop.onOperationFinished(reported);
                    }
                });
            }
        }).start();
    }

    public void getApostamientos(final String grupo,final backgroundOperation cb){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Apostamiento> aps = db.apostamientoDao().getAllApostamientos();
                final List<Plantilla> plantillas = db.plantillaDao().getSavedPlantillaPlaces(from,to,grupo);
                final List<Guard> guards = db.guardDao().getActiveElements();
                for (int i = 0; i < guards.size(); i++) {
                    Guard current = guards.get(i);
                    Person personadata = db.personDao().getPersonByid(current.getGuardPersonId());
                    if (personadata != null){
                        guards.get(i).setPaersonData(personadata);
                    }
                }
                Object[] result = new Object[]{aps,plantillas,guards};
                cb.onOperationFinished(result);
            }
        }).start();
    }

    public void getGuardByHash(final String gHash,final backgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Guard guard = db.guardDao().getGuardByHash(gHash);
                if (guard != null){
                    Person person = db.personDao().getPersonByid(guard.getGuardPersonId());
                    guard.setPaersonData(person);
                }
                cb.onOperationFinished(guard);
            }
        }).start();
    }

    public void saveIncidence(final Incidencia incidencia){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.incidenceDao().save(incidencia);
            }
        }).start();
    }

    public void savePlantillaPlace(final Plantilla plantilla){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.plantillaDao().save(plantilla);
            }
        }).start();
    }

    public void removeguardfromPlantilla(final int gid,final String turno){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.plantillaDao().deleteGuardFromPlantila(CryptoHash.sha1(String.valueOf(gid)),turno,from,to);
            }
        }).start();

    }

    public void getEdoAndIncidencesFromDay(final String grupo,final backgroundOperation cb){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Incidencia> incidences = db.incidenceDao().getIncidences(from,to);
                List<Plantilla> plantillas = db.plantillaDao().getSavedPlantillaPlaces(from,to,grupo);
                Object[] result = new Object[]{incidences,plantillas};
                cb.onOperationFinished(result);
            }
        }).start();
    }

    public void updateEdoData(final String grupo,final List<Plantilla> plantillas){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                c.setTime(TimeUtils.parse("yyyy-MM-dd",plantillas.get(0).getEdoFuerzaReported()));
                String from = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
                c.add(Calendar.DAY_OF_MONTH,1);
                String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
                db.plantillaDao().updateAfterServerSave(grupo,from,to,plantillas);
            }
        }).start();
    }
    
    public void setflagEdoUpdated(final String identifier,final String grupo){
        Calendar c = Calendar.getInstance();
        final String from = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(c.getTime());
        c.add(Calendar.DAY_OF_MONTH,1);
        final String to = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH).format(c.getTime());
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.plantillaDao().flagPlantillaSaved(identifier,from,to,grupo);
            }
        }).start();
    }

    public void getGuards(final backgroundOperation cb){
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Guard> guards = db.guardDao().getAllGuards();
                for (int i = 0; i < guards.size(); i++) {
                    Person person = db.personDao().getPersonByid(guards.get(i).getGuardPersonId());
                    if (person != null){
                        guards.get(i).setPaersonData(person);
                    }else {
                        //Thank you so much Intellij for telling me that
                        guards.remove(i);
                        //DELETE inexistent person we dont want duplicate
                        i = i-1;
                    }
                }
                cb.onOperationFinished(guards);
            }
        }).start();
    }

    public void saveNewGuard(final Guard guard){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.personDao().save(guard.getPaersonData());
                db.guardDao().save(guard);
            }
        }).start();
    }

    public void saveSiteIncidence(final SiteIncidence incidence){
        new Thread(new Runnable() {
            @Override
            public void run() {
                long saved = db.incodenceDao().save(incidence);
                if (saved != 0){
                    Gson gson = new Gson();
                    try{
                        JSONObject incident = new JSONObject(new String(gson.toJson(incidence,SiteIncidence.class)));

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void updateGuard(final Guard guard,@Nullable final UIThreadOperation cb){
        NetworkOperations.updateGuard(guard, new NetworkOperations.SimpleNetworkCallback<Boolean>() {
            @Override
            public void onResponse(Boolean response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int saved = db.guardDao().update(guard);
                        if (guard.getPaersonData() != null){
                            updatePerson(guard.getPaersonData());
                        }
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                if (cb != null){
                                    cb.onOperationFinished(saved);
                                }
                            }
                        });
                    }
                }).start();
            }

            @Override
            public void onVolleyError(Boolean response, VolleyError error) {

            }
        });
    }

    public  void updatePerson(final Person person){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final int saved = db.personDao().update(person);
            }
        }).start();
    }

    public static Handler getHandler() {
        return handler;
    }

    public interface backgroundOperation{
        void onOperationFinished(Object callbackResult);
    }

    public interface UIThreadOperation{
        void onOperationFinished(@Nullable Object callbackResult);
    }
}
