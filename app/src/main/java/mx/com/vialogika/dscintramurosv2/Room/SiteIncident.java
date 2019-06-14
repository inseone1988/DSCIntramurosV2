package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "SiteIncidents")
public class SiteIncident {

    @PrimaryKey(autoGenerate = true)
    private long localId;

    private int     eventId;
    private String  eventCaptureTimestamp;
    private String  eventDate;
    private String  eventTime;
    private String  eventName;
    private String  eventRiskLevel;
    private String  eventResponsable;
    private String  eventEvidence = "";
    private String  eventWhat;
    private String  eventHow;
    private String  eventWhen;
    private String  eventWhere;
    private String  eventFacts;
    private String  eventFiles;
    private String  eventUser;
    private String  eventUserSite;
    private boolean eventEditStatus;
    private String  eventSignatureNames = "";
    private String  eventSignature = "";
    private String  eventSignatureRoles = "";
    private String  eventType;
    private String  eventUUID;
    private String  eventUserPosition;
    private String  eventSiteClient;
    private String  eventSubject;
    private boolean eventStatus;
    private String  eventFlags;
    private String  eventFinalObs;

    public SiteIncident() {
    }

    public SiteIncident(JSONObject siteEvent) {
        try {
            this.eventId = siteEvent.getInt("event_id");
            this.eventCaptureTimestamp = siteEvent.getString("event_capture_timestamp");
            this.eventDate = siteEvent.getString("event_date");
            this.eventTime = siteEvent.getString("event_time");
            this.eventName = siteEvent.getString("event_name");
            this.eventRiskLevel = siteEvent.getString("event_risk_level");
            this.eventResponsable = siteEvent.getString("event_responsable");
            this.eventEvidence = siteEvent.getString("event_evidence");
            this.eventWhat = siteEvent.getString("event_what");
            this.eventHow = siteEvent.getString("event_how");
            this.eventWhen = siteEvent.getString("event_when");
            this.eventWhere = siteEvent.getString("event_where");
            this.eventFacts = siteEvent.getString("event_facts");
            this.eventFiles = siteEvent.getString("event_files");
            this.eventUser = siteEvent.getString("event_user");
            this.eventUserSite = siteEvent.getString("event_user_site");
            this.eventEditStatus = siteEvent.getInt("event_edit_status") != 0;
            this.eventSignatureNames = siteEvent.getString("event_signature_names");
            this.eventSignature = siteEvent.getString("event_signature");
            this.eventSignatureRoles = siteEvent.getString("event_signature_roles");
            this.eventType = siteEvent.getString("event_type");
            this.eventUUID = siteEvent.getString("event_UUID");
            this.eventUserPosition = siteEvent.getString("event_user_position");
            this.eventSiteClient = siteEvent.getString("event_site_client");
            this.eventSubject = siteEvent.getString("event_subject");
            this.eventStatus = siteEvent.getInt("event_status") != 0;
            this.eventFlags = siteEvent.getString("event_flags");
            this.eventFinalObs = siteEvent.getString("event_final_obs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject siteEvent = new JSONObject();
        try {
            siteEvent.put("event_id", this.eventId);
            siteEvent.put("event_capture_timestamp", this.eventCaptureTimestamp);
            siteEvent.put("event_date", this.eventDate);
            siteEvent.put("event_time", this.eventTime);
            siteEvent.put("event_name", this.eventName);
            siteEvent.put("event_risk_level", this.eventRiskLevel);
            siteEvent.put("event_responsable", this.eventResponsable);
            siteEvent.put("event_evidence", this.eventEvidence);
            siteEvent.put("event_what", this.eventWhat);
            siteEvent.put("event_how", this.eventHow);
            siteEvent.put("event_when", this.eventWhen);
            siteEvent.put("event_where", this.eventWhere);
            siteEvent.put("event_facts", this.eventFacts);
            siteEvent.put("event_files", this.eventFiles);
            siteEvent.put("event_user", this.eventUser);
            siteEvent.put("event_user_site", this.eventUserSite);
            siteEvent.put("event_edit_status", this.eventEditStatus);
            siteEvent.put("event_signature_names", this.eventSignatureNames);
            siteEvent.put("event_signature", this.eventSignature);
            siteEvent.put("event_signature_roles", this.eventSignatureRoles);
            siteEvent.put("event_type", this.eventType);
            siteEvent.put("event_UUID", this.eventUUID);
            siteEvent.put("event_user_position", this.eventUserPosition);
            siteEvent.put("event_site_client", this.eventSiteClient);
            siteEvent.put("event_subject", this.eventSubject);
            siteEvent.put("event_status", this.eventStatus);
            siteEvent.put("event_flags", this.eventFlags);
            siteEvent.put("event_final_obs", this.eventFinalObs);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return siteEvent;
    }

    private void update(SiteIncident siteEvent){
        this.eventId = siteEvent.getEventId();
        this.eventCaptureTimestamp = siteEvent.getEventCaptureTimestamp();
        this.eventDate = siteEvent.getEventDate();
        this.eventTime = siteEvent.getEventTime();
        this.eventName = siteEvent.getEventName();
        this.eventRiskLevel = siteEvent.getEventRiskLevel();
        this.eventResponsable = siteEvent.getEventResponsable();
        this.eventEvidence = siteEvent.getEventEvidence();
        this.eventWhat = siteEvent.getEventWhat();
        this.eventHow = siteEvent.getEventHow();
        this.eventWhen = siteEvent.getEventWhen();
        this.eventWhere = siteEvent.getEventWhere();
        this.eventFacts = siteEvent.getEventFacts();
        this.eventFiles = siteEvent.getEventFiles();
        this.eventUser = siteEvent.getEventUser();
        this.eventUserSite = siteEvent.getEventUserSite();
        this.eventEditStatus = siteEvent.getEventEditStatus();
        this.eventSignatureNames = siteEvent.getEventSignatureNames();
        this.eventSignature = siteEvent.getEventSignature();
        this.eventSignatureRoles = siteEvent.getEventSignatureRoles();
        this.eventType = siteEvent.getEventType();
        this.eventUUID = siteEvent.getEventUUID();
        this.eventUserPosition = siteEvent.getEventUserPosition();
        this.eventSiteClient = siteEvent.getEventSiteClient();
        this.eventSubject = siteEvent.getEventSubject();
        this.eventStatus = siteEvent.getEventStatus();
        this.eventFlags = siteEvent.getEventFlags();
        this.eventFinalObs = siteEvent.getEventFinalObs();
    }

    public long getLocalId() {
        return localId;
    }

    public void setLocalId(long localId) {
        this.localId = localId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getEventCaptureTimestamp() {
        return eventCaptureTimestamp;
    }

    public void setEventCaptureTimestamp(String eventCaptureTimestamp) {
        this.eventCaptureTimestamp = eventCaptureTimestamp;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventRiskLevel() {
        return eventRiskLevel;
    }

    public void setEventRiskLevel(String eventRiskLevel) {
        this.eventRiskLevel = eventRiskLevel;
    }

    public String getEventResponsable() {
        return eventResponsable;
    }

    public void setEventResponsable(String eventResponsable) {
        this.eventResponsable = eventResponsable;
    }

    public String getEventEvidence() {
        return eventEvidence;
    }

    public void setEventEvidence(String eventEvidence) {
        String[] evidence = this.eventEvidence.split(",");
        if (evidence[0].equals("")){
            this.eventEvidence = eventEvidence;
        }else{
            this.eventEvidence = this.eventEvidence + "," + eventEvidence;
        }
    }

    public String getEventWhat() {
        return eventWhat;
    }

    public void setEventWhat(String eventWhat) {
        this.eventWhat = eventWhat;
    }

    public String getEventHow() {
        return eventHow;
    }

    public void setEventHow(String eventHow) {
        this.eventHow = eventHow;
    }

    public String getEventWhen() {
        return eventWhen;
    }

    public void setEventWhen(String eventWhen) {
        this.eventWhen = eventWhen;
    }

    public String getEventWhere() {
        return eventWhere;
    }

    public void setEventWhere(String eventWhere) {
        this.eventWhere = eventWhere;
    }

    public String getEventFacts() {
        return eventFacts;
    }

    public void setEventFacts(String eventFacts) {
        this.eventFacts = eventFacts;
    }

    public String getEventFiles() {
        return eventFiles;
    }

    public void setEventFiles(String eventFiles) {
        this.eventFiles = eventFiles;
    }

    public String getEventUser() {
        return eventUser;
    }

    public void setEventUser(String eventUser) {
        this.eventUser = eventUser;
    }

    public String getEventUserSite() {
        return eventUserSite;
    }

    public void setEventUserSite(String eventUserSite) {
        this.eventUserSite = eventUserSite;
    }

    public boolean getEventEditStatus() {
        return eventEditStatus;
    }

    public void setEventEditStatus(boolean eventEditStatus) {
        this.eventEditStatus = eventEditStatus;
    }

    public String getEventSignatureNames() {
        return eventSignatureNames;
    }

    public void setEventSignatureNames(String eventSignatureNames) {
        String[] snames = this.eventSignatureNames.split(",");
        if (!snames[0].equals("")){
            this.eventSignatureNames = this.eventSignature + ","+eventSignatureNames;
        }else{
            this.eventSignatureNames = eventSignatureNames;
        }
    }

    public String getEventSignature() {
        return eventSignature;
    }

    public void setEventSignature(String eventSignature) {
        String[] signatures = this.eventSignature.split(",");
        if (!signatures[0].equals("")){
            this.eventSignature =  this.eventSignature  +","+ eventSignature;
        }else {
            this.eventSignature = eventSignature;
        }
    }

    public String getEventSignatureRoles() {
        return eventSignatureRoles;
    }

    public void setEventSignatureRoles(String eventSignatureRoles) {
        String[] snames = this.eventSignatureRoles.split(",");
        if (!snames[0].equals("")){
            this.eventSignatureRoles = this.eventSignatureRoles + ","+eventSignatureRoles;
        }else{
            this.eventSignatureRoles = eventSignatureRoles;
        }
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventUUID() {
        return eventUUID;
    }

    public void setEventUUID(String eventUUID) {
        this.eventUUID = eventUUID;
    }

    public String getEventUserPosition() {
        return eventUserPosition;
    }

    public void setEventUserPosition(String eventUserPosition) {
        this.eventUserPosition = eventUserPosition;
    }

    public String getEventSiteClient() {
        return eventSiteClient;
    }

    public void setEventSiteClient(String eventSiteClient) {
        this.eventSiteClient = eventSiteClient;
    }

    public String getEventSubject() {
        return eventSubject;
    }

    public void setEventSubject(String eventSubject) {
        this.eventSubject = eventSubject;
    }

    public boolean getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(boolean eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventFlags() {
        return eventFlags;
    }

    public void setEventFlags(String eventFlags) {
        this.eventFlags = eventFlags;
    }

    public String getEventFinalObs() {
        return eventFinalObs;
    }

    public void setEventFinalObs(String eventFinalObs) {
        this.eventFinalObs = eventFinalObs;
    }
}