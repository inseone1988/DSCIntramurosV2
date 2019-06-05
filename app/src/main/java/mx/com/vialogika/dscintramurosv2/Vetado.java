package mx.com.vialogika.dscintramurosv2;

public class Vetado {
    private int idpersons;
    private String person_fullname;
    private String provider_alias;
    private String restriction_type;
    private String due_date;
    private String restriction_obs;

    public Vetado() {
    }

    public int getIdpersons() {
        return idpersons;
    }

    public void setIdpersons(int idpersons) {
        this.idpersons = idpersons;
    }

    public String getPerson_fullname() {
        return person_fullname;
    }

    public void setPerson_fullname(String person_fullname) {
        this.person_fullname = person_fullname;
    }

    public String getProvider_alias() {
        return provider_alias;
    }

    public void setProvider_alias(String provider_alias) {
        this.provider_alias = provider_alias;
    }

    public String getRestriction_type() {
        return restriction_type;
    }

    public void setRestriction_type(String restriction_type) {
        this.restriction_type = restriction_type;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getRestriction_obs() {
        return restriction_obs;
    }

    public void setRestriction_obs(String restriction_obs) {
        this.restriction_obs = restriction_obs;
    }
}
