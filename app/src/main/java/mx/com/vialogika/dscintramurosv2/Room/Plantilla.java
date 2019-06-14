package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Plantillas")
public class Plantilla {
    @PrimaryKey
    private int     id;
    private String  edoFuerzaPlantillaId;
    private String  edoFuerzaProviderId;
    private String  edoFuerzaSiteId;
    private String  edoFuerzaPlaceId;
    private String  edoFuerzaGuardId;
    private String  edoFuerzaTiempo;
    private String  edoFuerzaIncidenceId;
    private String  edoFuerzaCoveredGuardId;
    private String  edoFuerzaGuardJob;
    private String  edoFuerzaDate;
    private String  edoFuerzaReported;
    private String  edoFuerzaTurno;
    private String  edoFuerzaUpdated;
    private Boolean edoFuerzaStatus;

    public Plantilla() {
    }

    public Plantilla(JSONObject plantilla) {
        try {
            this.id = plantilla.getInt("id");
            this.edoFuerzaPlantillaId = plantilla.getString("edo_fuerza_plantilla_id");
            this.edoFuerzaProviderId = plantilla.getString("edo_fuerza_provider_id");
            this.edoFuerzaSiteId = plantilla.getString("edo_fuerza_site_id");
            this.edoFuerzaPlaceId = plantilla.getString("edo_fuerza_place_id");
            this.edoFuerzaGuardId = plantilla.getString("edo_fuerza_guard_id");
            this.edoFuerzaTiempo = plantilla.getString("edo_fuerza_tiempo");
            this.edoFuerzaIncidenceId = plantilla.getString("edo_fuerza_incidence_id");
            this.edoFuerzaCoveredGuardId = plantilla.getString("edo_fuerza_covered_guard_id");
            this.edoFuerzaGuardJob = plantilla.getString("edo_fuerza_guard_job");
            this.edoFuerzaDate = plantilla.getString("edo_fuerza_date");
            this.edoFuerzaReported = plantilla.getString("edo_fuerza_reported");
            this.edoFuerzaTurno = plantilla.getString("edo_fuerza_turno");
            this.edoFuerzaUpdated = plantilla.getString("edo_fuerza_updated");
            this.edoFuerzaStatus = plantilla.getInt("edo_fuerza_status") == 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject plantilla = new JSONObject();
        try {
            plantilla.put("id", this.id);
            plantilla.put("edo_fuerza_plantilla_id", this.edoFuerzaPlantillaId);
            plantilla.put("edo_fuerza_provider_id", this.edoFuerzaProviderId);
            plantilla.put("edo_fuerza_site_id", this.edoFuerzaSiteId);
            plantilla.put("edo_fuerza_place_id", this.edoFuerzaPlaceId);
            plantilla.put("edo_fuerza_guard_id", this.edoFuerzaGuardId);
            plantilla.put("edo_fuerza_tiempo", this.edoFuerzaTiempo);
            plantilla.put("edo_fuerza_incidence_id", this.edoFuerzaIncidenceId);
            plantilla.put("edo_fuerza_covered_guard_id", this.edoFuerzaCoveredGuardId);
            plantilla.put("edo_fuerza_guard_job", this.edoFuerzaGuardJob);
            plantilla.put("edo_fuerza_date", this.edoFuerzaDate);
            plantilla.put("edo_fuerza_reported", this.edoFuerzaReported);
            plantilla.put("edo_fuerza_turno", this.edoFuerzaTurno);
            plantilla.put("edo_fuerza_updated", this.edoFuerzaUpdated);
            plantilla.put("edo_fuerza_status", this.edoFuerzaStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return plantilla;
    }

    public void update(Plantilla plantilla) {
        this.id = plantilla.getId();
        this.edoFuerzaPlantillaId = plantilla.getEdoFuerzaPlantillaId();
        this.edoFuerzaProviderId = plantilla.getEdoFuerzaProviderId();
        this.edoFuerzaSiteId = plantilla.getEdoFuerzaSiteId();
        this.edoFuerzaPlaceId = plantilla.getEdoFuerzaPlaceId();
        this.edoFuerzaGuardId = plantilla.getEdoFuerzaGuardId();
        this.edoFuerzaTiempo = plantilla.getEdoFuerzaTiempo();
        this.edoFuerzaIncidenceId = plantilla.getEdoFuerzaIncidenceId();
        this.edoFuerzaCoveredGuardId = plantilla.getEdoFuerzaCoveredGuardId();
        this.edoFuerzaGuardJob = plantilla.getEdoFuerzaGuardJob();
        this.edoFuerzaDate = plantilla.getEdoFuerzaDate();
        this.edoFuerzaReported = plantilla.getEdoFuerzaReported();
        this.edoFuerzaTurno = plantilla.getEdoFuerzaTurno();
        this.edoFuerzaUpdated = plantilla.getEdoFuerzaUpdated();
        this.edoFuerzaStatus = plantilla.getEdoFuerzaStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEdoFuerzaPlantillaId() {
        return edoFuerzaPlantillaId;
    }

    public void setEdoFuerzaPlantillaId(String edoFuerzaPlantillaId) {
        this.edoFuerzaPlantillaId = edoFuerzaPlantillaId;
    }

    public String getEdoFuerzaProviderId() {
        return edoFuerzaProviderId;
    }

    public void setEdoFuerzaProviderId(String edoFuerzaProviderId) {
        this.edoFuerzaProviderId = edoFuerzaProviderId;
    }

    public String getEdoFuerzaSiteId() {
        return edoFuerzaSiteId;
    }

    public void setEdoFuerzaSiteId(String edoFuerzaSiteId) {
        this.edoFuerzaSiteId = edoFuerzaSiteId;
    }

    public String getEdoFuerzaPlaceId() {
        return edoFuerzaPlaceId;
    }

    public void setEdoFuerzaPlaceId(String edoFuerzaPlaceId) {
        this.edoFuerzaPlaceId = edoFuerzaPlaceId;
    }

    public String getEdoFuerzaGuardId() {
        return edoFuerzaGuardId;
    }

    public void setEdoFuerzaGuardId(String edoFuerzaGuardId) {
        this.edoFuerzaGuardId = edoFuerzaGuardId;
    }

    public String getEdoFuerzaTiempo() {
        return edoFuerzaTiempo;
    }

    public void setEdoFuerzaTiempo(String edoFuerzaTiempo) {
        this.edoFuerzaTiempo = edoFuerzaTiempo;
    }

    public String getEdoFuerzaIncidenceId() {
        return edoFuerzaIncidenceId;
    }

    public void setEdoFuerzaIncidenceId(String edoFuerzaIncidenceId) {
        this.edoFuerzaIncidenceId = edoFuerzaIncidenceId;
    }

    public String getEdoFuerzaCoveredGuardId() {
        return edoFuerzaCoveredGuardId;
    }

    public void setEdoFuerzaCoveredGuardId(String edoFuerzaCoveredGuardId) {
        this.edoFuerzaCoveredGuardId = edoFuerzaCoveredGuardId;
    }

    public String getEdoFuerzaGuardJob() {
        return edoFuerzaGuardJob;
    }

    public void setEdoFuerzaGuardJob(String edoFuerzaGuardJob) {
        this.edoFuerzaGuardJob = edoFuerzaGuardJob;
    }

    public String getEdoFuerzaDate() {
        return edoFuerzaDate;
    }

    public void setEdoFuerzaDate(String edoFuerzaDate) {
        this.edoFuerzaDate = edoFuerzaDate;
    }

    public String getEdoFuerzaReported() {
        return edoFuerzaReported;
    }

    public void setEdoFuerzaReported(String edoFuerzaReported) {
        this.edoFuerzaReported = edoFuerzaReported;
    }

    public String getEdoFuerzaTurno() {
        return edoFuerzaTurno;
    }

    public void setEdoFuerzaTurno(String edoFuerzaTurno) {
        this.edoFuerzaTurno = edoFuerzaTurno;
    }

    public Boolean getEdoFuerzaStatus() {
        return edoFuerzaStatus;
    }

    public void setEdoFuerzaStatus(Boolean edoFuerzaStatus) {
        this.edoFuerzaStatus = edoFuerzaStatus;
    }

    public String getEdoFuerzaUpdated() {
        return edoFuerzaUpdated;
    }

    public void setEdoFuerzaUpdated(String edoFuerzaUpdated) {
        this.edoFuerzaUpdated = edoFuerzaUpdated;
    }
}
