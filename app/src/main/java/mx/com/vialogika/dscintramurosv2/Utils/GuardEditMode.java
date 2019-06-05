package mx.com.vialogika.dscintramurosv2.Utils;

public enum GuardEditMode {
    NEW_GUARD("nuevo"),
    EXISTENT_GUARD("existente");

    private String mode;

    GuardEditMode(String mode) {
        this.mode = mode;
    }

    public String toString(){
        return this.mode;
    }
}
