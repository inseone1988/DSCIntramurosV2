package mx.com.vialogika.dscintramurosv2.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.GlobalAplication;
import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Incidencia;
import mx.com.vialogika.dscintramurosv2.Room.Person;
import mx.com.vialogika.dscintramurosv2.Room.Plantilla;
import mx.com.vialogika.dscintramurosv2.Utils.UserKeys;

public class NetworkOperations {

    public static final String MODE_UPDATE              = "mode_update";
    public static final String MODE_SYNC                = "mode_sync";
    public static final String MODE_SEND                = "mode_send";
    public static final int    SEND_PLANTILLA_TO_SERVER = 154879;
    //public static final String SERVER_URL_PREFIX        = "https://www.vialogika.com.mx/dscic/";
    public static final String SERVER_URL_PREFIX        = "http://192.168.2.2/dscic/";
    public static final String DEFAULT_HANDLER          = "raw.php";

    private static volatile NetworkOperations instance;
    private static          RequestQueue      rq;

    private static          Context mContext;
    private static final Handler UIHandler = new Handler(Looper.getMainLooper());

    private NetworkOperations() {
        mContext = GlobalAplication.getAppContext();
        if (instance != null) {
            throw new RuntimeException("You should use getInstance() method to get the an instance of this class.");
        }
    }

    public static NetworkOperations getInstance() {
        if (rq == null) {
            rq = Volley.newRequestQueue(GlobalAplication.getAppContext());
        }
        if (instance == null) {
            synchronized (NetworkOperations.class) {
                if (instance == null) instance = new NetworkOperations();
            }
        }
        return instance;
    }

    private String defaultURL() {
        return SERVER_URL_PREFIX + DEFAULT_HANDLER;
    }

    public void getApiKey(final SimpleNetworkCallback<JSONObject> cb){
        ServerRequest request = new ServerRequest(Request.Method.POST, defaultURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if (response.getBoolean("success")){
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAplication.getAppContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(UserKeys.SP_API_KEY,response.getString("auth"));
                        editor.apply();
                        cb.onResponse(response);
                    }else{
                        cb.onResponse(response);
                    }
                }catch(JSONException e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(request);
    }

    public void downloadDeviceData(final SimpleNetworkCallback<JSONObject> cb){
        JSONObject params = new JSONObject();
        try{
            params.put("function","getDeviceData");
        }catch(JSONException e){
            Log.d("Volley","Failed to set paramteres");
        }
        ServerRequest request = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                cb.onResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(request);
    }

    public void SyncGuards(int siteid) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", "syncGuards");
            params.put("siteid", siteid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DatabaseOperations dbo       = DatabaseOperations.getInstance(getContext());
                List<Guard>        gResponse = new ArrayList<>();
                List<Person>       pResponse = new ArrayList<>();
                try {
                    JSONArray gPayload = response.getJSONArray("payload");
                    JSONArray pPayload = response.getJSONArray("payloadPersons");
                    for (int i = 0; i < gPayload.length(); i++) {
                        gResponse.add(new Guard(gPayload.getJSONObject(i)));
                    }
                    for (int i = 0; i < pPayload.length(); i++) {
                        pResponse.add(new Person(pPayload.getJSONObject(i)));
                    }
                    dbo.SyncGuards(gResponse, false);
                    dbo.SyncPersons(pResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq.add(jor);
    }

    public void SyncApostamientos(int siteid) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", "getApostamientos");
            params.put("siteid", siteid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DatabaseOperations dbo = DatabaseOperations.getInstance(getContext());
                List<Apostamiento> aps = new ArrayList<>();
                try {
                    JSONArray apPayload = response.getJSONArray("payload");
                    for (int i = 0; i < apPayload.length(); i++) {
                        aps.add(new Apostamiento(apPayload.getJSONObject(i)));
                    }
                    dbo.SyncApostamientos(aps);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        rq.add(jor);
    }

    public void sendPlantilla(final String grupo) {
        final DatabaseOperations dbo = DatabaseOperations.getInstance(getContext());
        dbo.getEdoAndIncidencesFromDay(grupo, new DatabaseOperations.backgroundOperation() {
            @Override
            public void onOperationFinished(Object callbackResult) {
                Object[]         r          = (Object[]) callbackResult;
                List<Plantilla>  edo        = (List<Plantilla>) r[1];
                List<Incidencia> incidences = (List<Incidencia>) r[0];
                if (edo.size() > 0) {
                    String     mode   = edo.get(0).getEdoFuerzaPlantillaId() == null ? MODE_SEND : MODE_UPDATE;
                    JSONObject params = new JSONObject();
                    JSONArray  data   = new JSONArray();
                    JSONArray  inc    = new JSONArray();
                    for (int i = 0; i < edo.size(); i++) {
                        try {
                            data.put(i, edo.get(i).toJSONObject());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if (incidences.size() > 0) {
                        for (int i = 0; i < incidences.size(); i++) {
                            try {
                                inc.put(i, incidences.get(i).toJSONObject());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        params.put("function", "savePlantilla");
                        params.put("edo", data);
                        params.put("incidences", inc);
                        params.put("mode", mode);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getBoolean("success")) {
                                    JSONArray       savedPlantillas = response.getJSONArray("plantilla");
                                    List<Plantilla> updated         = new ArrayList<>();
                                    for (int i = 0; i < savedPlantillas.length(); i++) {
                                        updated.add(new Plantilla(savedPlantillas.getJSONObject(i)));
                                    }
                                    dbo.updateEdoData(grupo, updated);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    });
                    rq.add(jor);
                } else {
                    UIHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "No hay nada por enviar", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

    public interface NetworkRequestCallbacks {
        void onNetworkResponse(Object response);

        void onNetworkRequestError(VolleyError e);
    }

    public interface SimpleNetworkCallback<T>{
        void onResponse(T response);
        void onVolleyError(T response,VolleyError error);
    }

    public Context getContext() {
        return mContext;
    }

}
