package mx.edu.ittepic.judamedranoba.recordatec;

public class Lista_entrada {
    private String textoMateria;
    private String textoFechaEntrega;
    private String textoMaestro;
    private String textoId;

    public Lista_entrada(String textoMateria, String textoFechaEntrega, String textoMaestro, String textoId) {
        this.textoMateria = textoMateria;
        this.textoFechaEntrega = textoFechaEntrega;
        this.textoMaestro = textoMaestro;
        this.textoId = textoId;
    }

    public String getTextoMateria() {
        return textoMateria;
    }

    public String getTextoFechaEntrega() {
        return textoFechaEntrega;
    }

    public String getTextoMaestro() {
        return textoMaestro;
    }

    public String getTextoId() { return textoId; }
}
