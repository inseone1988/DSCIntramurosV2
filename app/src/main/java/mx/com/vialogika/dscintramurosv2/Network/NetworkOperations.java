package mx.com.vialogika.dscintramurosv2.Network;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;

import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.vialogika.dscintramurosv2.GlobalAplication;
import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Incidencia;
import mx.com.vialogika.dscintramurosv2.Room.Person;
import mx.com.vialogika.dscintramurosv2.Room.Plantilla;
import mx.com.vialogika.dscintramurosv2.Room.SiteIncident;
import mx.com.vialogika.dscintramurosv2.Utils.FileUtils;
import mx.com.vialogika.dscintramurosv2.Utils.UserKeys;
import mx.com.vialogika.dscintramurosv2.Vetado;

public class NetworkOperations {

    public static final String MODE_UPDATE              = "mode_update";
    public static final String MODE_SYNC                = "mode_sync";
    public static final String MODE_SEND                = "mode_send";
    public static final int    SEND_PLANTILLA_TO_SERVER = 154879;
    //public static final String SERVER_URL_PREFIX        = "https://www.vialogika.com.mx/dscic/";
    //public static final String SERVER_URL_PREFIX        = "http://192.168.2.2/dscic/";
    public static final String SERVER_URL_PREFIX        = "http://192.168.0.3/dscic/";
    public static final String DEFAULT_HANDLER          = "raw.php";

    private static volatile NetworkOperations instance;
    private static          RequestQueue      rq;

    private static       Context mContext;
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

    private String guardProfilePhotoHandler() {
        return SERVER_URL_PREFIX + "requesthandler.php";
    }

    public void getApiKey(final SimpleNetworkCallback<JSONObject> cb) {
        ServerRequest request = new ServerRequest(Request.Method.POST, defaultURL(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        SharedPreferences        preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAplication.getAppContext());
                        SharedPreferences.Editor editor      = preferences.edit();
                        editor.putString(UserKeys.SP_API_KEY, response.getString("auth"));
                        editor.apply();
                        cb.onResponse(response);
                    } else {
                        cb.onResponse(response);
                    }
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        rq.add(request);
    }

    public void downloadDeviceData(final SimpleNetworkCallback<JSONObject> cb) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", "getDeviceData");
        } catch (JSONException e) {
            Log.d("Volley", "Failed to set paramteres");
        }
        ServerRequest request = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAplication.getAppContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    JSONObject userdata = response.getJSONObject("user_data");
                    editor.putString(UserKeys.SP_USER_FULLNAME,userdata.getString("user_fullname"));
                    editor.putString(UserKeys.SP_PROVIDER,userdata.getString("user_provider"));
                    editor.putString(UserKeys.SP_ROLE,userdata.getString("user_role"));
                    editor.putString(UserKeys.SP_SITE,userdata.getString("user_site"));
                    editor.putInt(UserKeys.SP_SITE_ID,userdata.getInt("user_site_id"));
                    editor.putInt(UserKeys.SP_PROVIDER_ID,userdata.getInt("user_provider_id"));
                    editor.apply();
                }catch(JSONException e){
                    e.printStackTrace();
                }
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
        ServerRequest jor = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DatabaseOperations dbo       = DatabaseOperations.getInstance();
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
        ServerRequest jor = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DatabaseOperations dbo = DatabaseOperations.getInstance();
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
        final DatabaseOperations dbo = DatabaseOperations.getInstance();
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
                    ServerRequest jor = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
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

    public void saveGuard(Guard guard) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAplication.getAppContext());
            String apikey = preferences.getString(UserKeys.SP_API_KEY,"");
            String uploadId = new MultipartUploadRequest(GlobalAplication.getAppContext(), guardProfilePhotoHandler())
                    .addHeader("bearer",apikey)
                    .addFileToUpload(guard.getGuardPhotoPath(), "profile_photo")
                    .addParameter("function","saveProfilePhoto")
                    .addParameter("guard",guard.toJSONObject().toString())
                    .addParameter("person",guard.getPaersonData().toJSONObject().toString())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            System.out.println(new String(serverResponse.getBody()));
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .setMaxRetries(3)
                    .startUpload();
        } catch (Exception e) {

        }
    }

    public void updateGuard(final Guard guard,final SimpleNetworkCallback<Integer> cb){
        JSONObject params = new JSONObject();
        try{
            params.put("function","updateGuard");
            params.put("person",guard.getPaersonData().toJSONObject());
            ServerRequest sr = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        if (response.getBoolean("success")){
                            guard.save();
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            rq.add(sr);
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public void vetadoSearch(final String term, String searchType, final SimpleNetworkCallback<List<Vetado>> cb) {
        final JSONObject params = new JSONObject();
        JSONObject       data   = new JSONObject();
        try {
            params.put("function", "searchRestricted");
            data.put("string", term);
            data.put("searchtype", searchType);
            params.put("data", data);
            ServerRequest q = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.getBoolean("success")) {
                            List<Vetado> vetados = new ArrayList<>();
                            Gson         gson    = new Gson();
                            JSONArray    payload = response.getJSONArray("payload");
                            for (int i = 0; i < payload.length(); i++) {
                                vetados.add(gson.fromJson(payload.get(i).toString(), Vetado.class));
                            }
                            cb.onResponse(vetados);
                        }else{
                            cb.onResponse(new ArrayList<Vetado>());
                            Toast.makeText(GlobalAplication.getAppContext(), "No se han encontrado resultados", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            rq.add(q);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void saveIncidentToServer(String incident) {
        JSONObject params = new JSONObject();
        try {
            params.put("function", "");
            params.put("incident", incident);
            ServerRequest rq = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        } catch (JSONException e) {

        }
    }

    public void getImageFromServer(final Guard guard,@Nullable final SimpleNetworkCallback<Bitmap> cb){
        String url = SERVER_URL_PREFIX + "requesthandler.php";
        ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                try{
                    File mFile = FileUtils.createImageFile();
                    if (FileUtils.saveBitmap(response,mFile, Bitmap.CompressFormat.PNG)){
                        guard.setGuardPhotoPath(mFile.getAbsolutePath());
                        guard.save();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
                if (cb != null){
                    cb.onResponse(response);
                }
            }
        }, 0, 0, ImageView.ScaleType.FIT_CENTER, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){

            @Override
            public int getMethod() {
                return Method.POST;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GlobalAplication.getAppContext());
                Map<String,String> headers = new HashMap<>();
                headers.put("bearer",preferences.getString(UserKeys.SP_API_KEY,""));
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("function","profileImage");
                params.put("path",guard.getGuardPhotoPath());
                return params;
            }
        };
        rq.add(ir);
    }

    public void getIncidenceNames(final SimpleNetworkCallback<List<String>> cb){
        JSONObject params = new JSONObject();
        try{
            params.put("function","getIncidenceNames");
            ServerRequest request = new ServerRequest(Request.Method.POST, defaultURL(), params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        if (response.getBoolean("success")){
                            JSONArray incodencesnames = response.getJSONArray("incident_names");
                            List<String> inames = new ArrayList<>();
                            for (int i = 0; i < incodencesnames.length(); i++) {
                                JSONObject o = incodencesnames.getJSONObject(i);
                                inames.add(o.getString("incidence_name"));
                            }
                            cb.onResponse(inames);
                        }
                    }catch(JSONException e){

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    cb.onVolleyError(new ArrayList<String>(),error);
                }
            });
            rq.add(request);
        }catch(JSONException e ){
            e.printStackTrace();
        }
        }

        public static void saveIncidence(SiteIncident incident,final SimpleNetworkCallback<JSONObject> cb){
            try{
                Context context = GlobalAplication.getAppContext();
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                MultipartUploadRequest request = new MultipartUploadRequest(context,SERVER_URL_PREFIX + "/requesthandler.php")
                       .addHeader("bearer",preferences.getString(UserKeys.SP_API_KEY,""))
                        .addParameter("incident",incident.toJSONObject().toString())
                        .addParameter("function","saveIncident")
                        .setMaxRetries(3)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {

                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Toast.makeText(context, exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                                try{
                                    JSONObject response = new JSONObject(serverResponse.getBodyAsString());
                                    if (response.getBoolean("success")){
                                        cb.onResponse(response);
                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {
                                Toast.makeText(context, "La subida de la incidencia ha sido cancelada", Toast.LENGTH_SHORT).show();
                            }
                        });
                String[] evidencePaths = incident.getEventEvidence().split(",");
                String[] signaturePaths = incident.getEventSignature().split(",");
                for (int i = 0; i < evidencePaths.length; i++) {
                    String c = evidencePaths[i];
                    if (!c.equals("")){
                        request.addFileToUpload(c,"DSCEvidence_" + (System.currentTimeMillis()));
                    }
                }
                for (int i = 0; i < signaturePaths.length; i++) {
                    String s = signaturePaths[i];
                    if (!s.equals("")){
                        request.addFileToUpload(s,"DSCSignature_"+ (System.currentTimeMillis()));
                    }
                }
                request.startUpload();
            }catch(Exception e){

            }
        }


    public interface NetworkRequestCallbacks<T> {
        void onNetworkResponse(T response);

        void onNetworkRequestError(VolleyError e);
    }

    public interface SimpleNetworkCallback<T> {
        void onResponse(T response);

        void onVolleyError(T response, VolleyError error);
    }

    public Context getContext() {
        return mContext;
    }

}
