package mx.com.vialogika.dscintramurosv2.Network;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.widget.ListPopupWindow;

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

import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

public class NetworkOperations {

    public static final String SERVER_URL_PREFIX = "https:www.vialogika.com.mx/dscic/";
    public static final String DEFAULT_HANDLER   = "raw.php";

    private static volatile NetworkOperations instance;
    private static RequestQueue rq;

    private Context mContext;
    private static final Handler UIHandler = new Handler(Looper.getMainLooper());

    private NetworkOperations(Context context){
        mContext = context;
        if (instance != null){
            throw  new RuntimeException("You should use getInstance() method to get the an instance of this class.");
        }
    }

    public static NetworkOperations getInstance(Context context){
        if (rq == null){
            rq = Volley.newRequestQueue(context);
        }
        if (instance == null){
            synchronized (NetworkOperations.class){
                if (instance == null) instance = new NetworkOperations(context);
            }
        }
        return instance;
    }

    private String defaultURL(){
        return SERVER_URL_PREFIX + DEFAULT_HANDLER;
    }

    public void SyncGuards(int siteid){
        JSONObject params = new JSONObject();
        try{
            params.put("function","syncGuards");
            params.put("siteid",siteid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,defaultURL(),params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DatabaseOperations dbo = DatabaseOperations.getInstance(getContext());
                List<Guard> gResponse = new ArrayList<>();
                List<Person> pResponse = new ArrayList<>();
                try{
                    JSONArray gPayload = response.getJSONArray("payload");
                    JSONArray pPayload = response.getJSONArray("payloadPersons");
                    for (int i = 0; i < gPayload.length(); i++) {
                        gResponse.add(new Guard(gPayload.getJSONObject(i)));
                    }
                    for (int i = 0; i <pPayload.length(); i++) {
                        pResponse.add(new Person(pPayload.getJSONObject(i)));
                    }
                    dbo.SyncGuards(gResponse,false);
                    dbo.SyncPersons(pResponse);
                }catch(JSONException e){
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

    public interface NetworkRequestCallbacks{
        void onNetworkResponse(Object response);
        void onNetworkRequestError(VolleyError e);
    }

    public Context getContext(){
        return mContext;
    }

}
