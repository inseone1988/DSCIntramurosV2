package mx.com.vialogika.dscintramurosv2.Room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "Persons")
public class Person {
    @PrimaryKey(autoGenerate = true)
    private int    LocalId;
    private int    idpersons;
    private String personCreated;
    private int    personProviderid;
    private String personType;
    private String personPosition;
    private String personName;
    private String personFname;
    private String personLname;
    private String personIdcardNumber;
    private String personLicenseType;
    private String personLicenseExp;
    private String personCertType;
    private int    personSiteId;
    private String personProfilePhotoPath;
    private String personGender;

    public Person() {

    }

    public Person(JSONObject person) {
        try {
            this.idpersons = person.getInt("idpersons");
            this.personCreated = person.getString("person_created");
            this.personProviderid = person.getInt("person_providerid");
            this.personType = person.getString("person_type");
            this.personPosition = person.getString("person_position");
            this.personName = person.getString("person_name");
            this.personFname = person.getString("person_fname");
            this.personLname = person.getString("person_lname");
            this.personIdcardNumber = person.getString("person_idcard_number");
            this.personLicenseType = person.getString("person_license_type");
            this.personLicenseExp = person.getString("person_license_exp");
            this.personCertType = person.getString("person_cert_type");
            this.personSiteId = person.getInt("person_site_id");
            this.personProfilePhotoPath = person.getString("person_profile_photo_path");
            this.personGender = person.getString("person_gender");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        JSONObject person = new JSONObject();
        try {
            person.put("idpersons", this.idpersons);
            person.put("person_created", this.personCreated);
            person.put("person_providerid", this.personProviderid);
            person.put("person_type", this.personType);
            person.put("person_position", this.personPosition);
            person.put("person_name", this.personName);
            person.put("person_fname", this.personFname);
            person.put("person_lname", this.personLname);
            person.put("person_idcard_number", this.personIdcardNumber);
            person.put("person_license_type", this.personLicenseType);
            person.put("person_license_exp", this.personLicenseExp);
            person.put("person_cert_type", this.personCertType);
            person.put("person_site_id", this.personSiteId);
            person.put("person_profile_photo_path", this.personProfilePhotoPath);
            person.put("person_gender", this.personGender);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return person;
    }

    public void update(Person person){
        this.idpersons = person.getIdpersons();
        this.personCreated = person.getPersonCreated();
        this.personProviderid = person.getPersonProviderid();
        this.personType = person.getPersonType();
        this.personPosition = person.getPersonPosition();
        this.personName = person.getPersonName();
        this.personFname = person.getPersonFname();
        this.personLname = person.getPersonLname();
        this.personIdcardNumber = person.getPersonIdcardNumber();
        this.personLicenseType = person.getPersonLicenseType();
        this.personLicenseExp = person.getPersonLicenseExp();
        this.personCertType = person.getPersonCertType();
        this.personSiteId = person.getPersonSiteId();
        this.personProfilePhotoPath = person.getPersonProfilePhotoPath();
        this.personGender = person.getPersonGender();
    }

    public int getLocalId() {
        return LocalId;
    }

    public void setLocalId(int localId) {
        LocalId = localId;
    }

    public int getIdpersons() {
        return idpersons;
    }

    public void setIdpersons(int idpersons) {
        this.idpersons = idpersons;
    }

    public String getPersonCreated() {
        return personCreated;
    }

    public void setPersonCreated(String personCreated) {
        this.personCreated = personCreated;
    }

    public int getPersonProviderid() {
        return personProviderid;
    }

    public void setPersonProviderid(int personProviderid) {
        this.personProviderid = personProviderid;
    }

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }

    public String getPersonPosition() {
        return personPosition;
    }

    public void setPersonPosition(String personPosition) {
        this.personPosition = personPosition;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonFname() {
        return personFname;
    }

    public void setPersonFname(String personFname) {
        this.personFname = personFname;
    }

    public String getPersonLname() {
        return personLname;
    }

    public void setPersonLname(String personLname) {
        this.personLname = personLname;
    }

    public String getPersonIdcardNumber() {
        return personIdcardNumber;
    }

    public void setPersonIdcardNumber(String personIdcardNumber) {
        this.personIdcardNumber = personIdcardNumber;
    }

    public String getPersonLicenseType() {
        return personLicenseType;
    }

    public void setPersonLicenseType(String personLicenseType) {
        this.personLicenseType = personLicenseType;
    }

    public String getPersonLicenseExp() {
        return personLicenseExp;
    }

    public void setPersonLicenseExp(String personLicenseExp) {
        this.personLicenseExp = personLicenseExp;
    }

    public String getPersonCertType() {
        return personCertType;
    }

    public void setPersonCertType(String personCertType) {
        this.personCertType = personCertType;
    }

    public int getPersonSiteId() {
        return personSiteId;
    }

    public void setPersonSiteId(int personSiteId) {
        this.personSiteId = personSiteId;
    }

    public String getPersonProfilePhotoPath() {
        return personProfilePhotoPath;
    }

    public void setPersonProfilePhotoPath(String personProfilePhotoPath) {
        this.personProfilePhotoPath = personProfilePhotoPath;
    }

    public String getPersonGender() {
        return personGender;
    }

    public void setPersonGender(String personGender) {
        this.personGender = personGender;
    }

    public String getPersonFullName(){
        return this.personName +" "+this.personFname+" "+this.personLname;
    }
}
