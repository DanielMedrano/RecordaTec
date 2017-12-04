package mx.edu.ittepic.judamedranoba.recordatec;

/**
 * Created by twarrios on 21/11/17.
 */

public class Lista_entrada2 {
        private String textoMateria;
        private String textoFechaEntrega;
        private String textoMaestro;

        public Lista_entrada2(String textoMateria, String textoFechaEntrega, String textoMaestro) {
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
