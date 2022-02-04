public class InvalidRateException extends Exception {

    private String numero;

    public InvalidRateException(String numero) {
        super("El número "+numero+" tiene una tarifa distinta de 100MB o 7GB.");
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

}
