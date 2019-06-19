package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

@Entity(tableName = "Guards")
public class Guard {
    @PrimaryKey(autoGenerate = true)
    private int    localId;
    private int    guardId;
    private int    guardProviderId;
    private int    guardPersonId;
    private String guardSite;
    private String guardRange;
    private String guardHash;
    private String guardPhotoPath;
    private String guardGroup;
    private String guardTurno;
    private int    guardStatus;
    private String guardBajaTimestamp;
    @Ignore
    private boolean asigned = false;
    @Ignore
    private Person paersonData;

    public Guard() {

    }

    public Guard(JSONObject guard) {
        try {
            this.guardId = guard.getInt("guard_id");
            this.guardProviderId = guard.getInt("guard_provider_id");
            this.guardPersonId = guard.getInt("guard_person_id");
            this.guardSite = guard.getString("guard_site");
            this.guardRange = guard.getString("guard_range");
            //this.guardHash = guard.getString("guard_hash");
            this.guardPhotoPath = guard.getString("guard_photo_path");
            this.guardGroup = guard.getString("guard_group");
            this.guardTurno = guard.getString("guard_turno");
            this.guardStatus = guard.getInt("guard_status");
            this.guardBajaTimestamp = guard.getString("guard_baja_timestamp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject guard = new JSONObject();
        try {
            guard.put("guard_id", this.guardId);
            guard.put("guard_provider_id", this.guardProviderId);
            guard.put("guard_person_id", this.guardPersonId);
            guard.put("guard_site", this.guardSite);
            guard.put("guard_range", this.guardRange);
            guard.put("guard_hash", this.guardHash);
            guard.put("guard_photo_path", this.guardPhotoPath);
            guard.put("guard_group", this.guardGroup);
            guard.put("guard_turno", this.guardTurno);
            guard.put("guard_status", this.guardStatus);
            guard.put("guard_baja_timestamp", this.guardBajaTimestamp);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guard;
    }

    public void update(Guard guard){
        this.guardId = guard.getGuardId();
        this.guardProviderId = guard.getGuardProviderId();
        this.guardPersonId = guard.getGuardPersonId();
        this.guardSite = guard.getGuardSite();
        this.guardRange = guard.getGuardRange();
        this.guardHash = guard.getGuardHash();
        if (!this.hasLocalProfilePhoto()) this.guardPhotoPath = guard.getGuardPhotoPath();
        this.guardGroup = guard.getGuardGroup();
        this.guardTurno = guard.getGuardTurno();
        this.guardStatus = guard.getGuardStatus();
        this.guardBajaTimestamp = guard.getGuardBajaTimestamp();
    }

    public void save(){
        DatabaseOperations op = DatabaseOperations.getInstance();
        op.updateGuard(this,null);
    }

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public int getGuardProviderId() {
        return guardProviderId;
    }

    public void setGuardProviderId(int guardProviderId) {
        this.guardProviderId = guardProviderId;
    }

    public int getGuardPersonId() {
        return guardPersonId;
    }

    public void setGuardPersonId(int guardPersonId) {
        this.guardPersonId = guardPersonId;
    }

    public String getGuardSite() {
        return guardSite;
    }

    public void setGuardSite(String guardSite) {
        this.guardSite = guardSite;
    }

    public String getGuardRange() {
        return guardRange;
    }

    public void setGuardRange(String guardRange) {
        this.guardRange = guardRange;
    }

    public String getGuardHash() {
        return guardHash;
    }

    public void setGuardHash(String guardHash) {
        this.guardHash = guardHash;
    }

    public String getGuardPhotoPath() {
        return guardPhotoPath;
    }

    public void setGuardPhotoPath(String guardPhotoPath) {
        this.guardPhotoPath = guardPhotoPath;
        if (getPaersonData() != null){
            getPaersonData().setPersonProfilePhotoPath(guardPhotoPath);
        }
    }

    public String getGuardGroup() {
        return guardGroup;
    }

    public void setGuardGroup(String guardGroup) {
        this.guardGroup = guardGroup;
    }

    public String getGuardTurno() {
        return guardTurno;
    }

    public void setGuardTurno(String guardTurno) {
        this.guardTurno = guardTurno;
    }

    public int getGuardStatus() {
        return guardStatus;
    }

    public void setGuardStatus(int guardStatus) {
        this.guardStatus = guardStatus;
    }

    public String getGuardBajaTimestamp() {
        return guardBajaTimestamp;
    }

    public void setGuardBajaTimestamp(String guardBajaTimestamp) {
        this.guardBajaTimestamp = guardBajaTimestamp;
    }

    public Person getPaersonData() {
        return paersonData;
    }

    public void setPaersonData(Person paersonData) {
        this.paersonData = paersonData;
    }

    public boolean isAsigned() {
        return asigned;
    }

    public void setAsigned(boolean asigned) {
        this.asigned = asigned;
    }

    public boolean isActive(){
        return this.guardStatus != 0;
    }

    public boolean networkSaved(){
        return this.guardId != 0;
    }

    public boolean hasLocalProfilePhoto(){
        if (this.guardPhotoPath != null && !this.guardPhotoPath.equals("null")&& !this.guardPhotoPath.equals("")&& !this.guardPhotoPath.contains("/dscic")){
            return hasLocalPhoto();
        }
        return false;
    }

    private boolean hasLocalPhoto(){
        File file = new File(this.guardPhotoPath);
        return file.exists();
    }
}
