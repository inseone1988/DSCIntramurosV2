package mx.com.vialogika.dscintramurosv2.Room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Incdiences")
public class SiteIncidence {
    //Mappers for table : site_events
    @PrimaryKey(autoGenerate = true)
    private long localId;

    private int eventId;
    private String eventCaptureTimestamp;
    private String eventDate;
    private String eventTime;
    private String eventName;
    private String eventRiskLevel;
    private String eventResponsable;
    private String eventEvidence;
    private String eventWhat;
    private String eventHow;
    private String eventWhen;
    private String eventWhere;
    private String eventFacts;
    private String eventFiles;
    private String eventUser;
    private String eventUserSite;
    private boolean eventEditStatus;
    private String eventSignatureNames;
    private String eventSignature;
    private String eventSignatureRoles;
    private String eventType;
    private String eventUUID;
    private String eventUserPosition;
    private String eventSiteClient;
    private String eventSubject;
    private boolean eventStatus;
    private String eventFlags;
    private String eventFinalObs;

    public SiteIncidence() {
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
        this.eventEvidence = eventEvidence;
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

    public boolean isEventEditStatus() {
        return eventEditStatus;
    }

    public void setEventEditStatus(boolean eventEditStatus) {
        this.eventEditStatus = eventEditStatus;
    }

    public String getEventSignatureNames() {
        return eventSignatureNames;
    }

    public void setEventSignatureNames(String eventSignatureNames) {
        this.eventSignatureNames = eventSignatureNames;
    }

    public String getEventSignature() {
        return eventSignature;
    }

    public void setEventSignature(String eventSignature) {
        this.eventSignature = eventSignature;
    }

    public String getEventSignatureRoles() {
        return eventSignatureRoles;
    }

    public void setEventSignatureRoles(String eventSignatureRoles) {
        this.eventSignatureRoles = eventSignatureRoles;
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

    public boolean isEventStatus() {
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
