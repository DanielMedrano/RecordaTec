package mx.edu.ittepic.judamedranoba.recordatec;

public class Lista_entrada {
    private String textoMateria;
    private String textoFechaEntrega;
    private String textoMaestro;

    public Lista_entrada(String textoMateria, String textoFechaEntrega, String textoMaestro) {
        this.textoMateria = textoMateria;
        this.textoFechaEntrega = textoFechaEntrega;
        this.textoMaestro = textoMaestro;
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
}
