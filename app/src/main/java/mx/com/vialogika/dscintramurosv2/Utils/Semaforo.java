package mx.com.vialogika.dscintramurosv2.Utils;

public enum Semaforo {
    GRIS("gray"),
    VERDE("green"),
    AMARILLO("yellow"),
    ROJO("red");
    private String value;

    Semaforo(String value) {
        this.value = value;
    }

    public String toSTring(){
        return this.value;
    }
}
