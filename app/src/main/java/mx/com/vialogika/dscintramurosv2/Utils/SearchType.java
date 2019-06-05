package mx.com.vialogika.dscintramurosv2.Utils;

public enum SearchType {
    SEARCH_TYPE_PERSON("Nombre"),
    SEARCH_TYPE_PROVIDER("Proveedor");

    private String value;

    SearchType(String value) {
        this.value = value;
    }

    public String toString(){
        return this.value;
    }
}
