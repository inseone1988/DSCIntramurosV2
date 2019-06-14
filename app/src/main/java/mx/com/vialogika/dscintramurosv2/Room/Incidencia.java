package mx.com.vialogika.dscintramurosv2.Room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Incidences")
public class Incidencia {
    @PrimaryKey
    private int    localId;
    private int    providerIncidencesId;
    private String providerIncidencesDatetime;
    private String providerIncidencesName;
    private String providerIncidencesType;
    private String providerIncidencesRequestedby;
    private String providerIncidencesObs;
    private String providerIncidencesUuid;

    public Incidencia() {
    }

    public Incidencia(JSONObject incident) {
        try {
            this.providerIncidencesId = incident.getInt("provider_incidences_id");
            this.providerIncidencesDatetime = incident.getString("provider_incidences_datetime");
            this.providerIncidencesName = incident.getString("provider_incidences_name");
            this.providerIncidencesType = incident.getString("provider_incidences_type");
            this.providerIncidencesRequestedby = incident.getString("provider_incidences_requestedby");
            this.providerIncidencesObs = incident.getString("provider_incidences_obs");
            this.providerIncidencesUuid = incident.getString("provider_incidences_uuid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject incident = new JSONObject();
        try {
            incident.put("provider_incidences_id", this.providerIncidencesId);
            incident.put("provider_incidences_datetime", this.providerIncidencesDatetime);
            incident.put("provider_incidences_name", this.providerIncidencesName);
            incident.put("provider_incidences_type", this.providerIncidencesType);
            incident.put("provider_incidences_requestedby", this.providerIncidencesRequestedby);
            incident.put("provider_incidences_obs", this.providerIncidencesObs);
            incident.put("provider_incidences_uuid", this.providerIncidencesUuid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return incident;
    }



    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getProviderIncidencesId() {
        return providerIncidencesId;
    }

    public void setProviderIncidencesId(int providerIncidencesId) {
        this.providerIncidencesId = providerIncidencesId;
    }

    public String getProviderIncidencesDatetime() {
        return providerIncidencesDatetime;
    }

    public void setProviderIncidencesDatetime(String providerIncidencesDatetime) {
        this.providerIncidencesDatetime = providerIncidencesDatetime;
    }

    public String getProviderIncidencesName() {
        return providerIncidencesName;
    }

    public void setProviderIncidencesName(String providerIncidencesName) {
        this.providerIncidencesName = providerIncidencesName;
    }

    public String getProviderIncidencesType() {
        return providerIncidencesType;
    }

    public void setProviderIncidencesType(String providerIncidencesType) {
        this.providerIncidencesType = providerIncidencesType;
    }

    public String getProviderIncidencesRequestedby() {
        return providerIncidencesRequestedby;
    }

    public void setProviderIncidencesRequestedby(String providerIncidencesRequestedby) {
        this.providerIncidencesRequestedby = providerIncidencesRequestedby;
    }

    public String getProviderIncidencesObs() {
        return providerIncidencesObs;
    }

    public void setProviderIncidencesObs(String providerIncidencesObs) {
        this.providerIncidencesObs = providerIncidencesObs;
    }

    public String getProviderIncidencesUuid() {
        return providerIncidencesUuid;
    }

    public void setProviderIncidencesUuid(String providerIncidencesUuid) {
        this.providerIncidencesUuid = providerIncidencesUuid;
    }

    public boolean isValidIncidence(){
        return this.getProviderIncidencesUuid() != null && !this.providerIncidencesUuid.equals("");
    }
}
