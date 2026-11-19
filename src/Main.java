import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Kasseapparat kasseapparat = new Kasseapparat("data/varer.csv", "data/tilbud.csv");
        Robot robot = new Robot();

        ArrayList<Vare> kurv = robot.fyldIKurv(kasseapparat.getAlleVarer());
        kasseapparat.udskrivKassebon(kurv);
    }
}