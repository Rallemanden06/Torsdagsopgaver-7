import java.time.LocalDate;
import java.util.*;

public class Kasseapparat {

    private final FileIO fileIO = new FileIO();
    private final Locale dk = new Locale("da", "DK");

    private final ArrayList<Vare> alleVarer;
    private final Map<Long, Vare> normalVarer = new HashMap<>();   // opslag på EAN
    private final Map<Long, Vare> tilbudsVarer = new HashMap<>();  // opslag på EAN

    public Kasseapparat(String varerSti, String tilbudSti) {
        alleVarer = loadData(varerSti);
        for (Vare v : alleVarer) {
            normalVarer.put(v.getId(), v);
        }
        for (Vare v : loadData(tilbudSti)) {
            tilbudsVarer.put(v.getId(), v);
        }
    }

    public ArrayList<Vare> getAlleVarer() {
        return alleVarer;
    }

    private ArrayList<Vare> loadData(String path) {
        ArrayList<Vare> resultat = new ArrayList<>();
        ArrayList<String> data = fileIO.readData(path);

        for (int i = 1; i < data.size(); i++) {
            if (data.get(i).isBlank()) continue;
            String[] v = data.get(i).split(";");
            resultat.add(new Vare(
                    Long.parseLong(v[0]),
                    v[1],
                    LocalDate.parse(v[2]),
                    Double.parseDouble(v[3]),
                    Boolean.parseBoolean(v[4]),
                    Double.parseDouble(v[5]),
                    v[6],
                    Boolean.parseBoolean(v[7]),
                    Boolean.parseBoolean(v[8]),
                    Boolean.parseBoolean(v[9])
            ));
        }
        return resultat;
    }

    public void udskrivKassebon(List<Vare> kurv) {
        Map<Long, Integer> antal = new LinkedHashMap<>();
        for (Vare v : kurv) {
            antal.merge(v.getId(), 1, Integer::sum);
        }

        double total = 0;
        StringBuilder bon = new StringBuilder();
        bon.append("------ KASSEBON ------\n");

        for (Map.Entry<Long, Integer> e : antal.entrySet()) {
            Vare normal = normalVarer.get(e.getKey());
            Vare tilbud = tilbudsVarer.get(e.getKey());
            int n = e.getValue();

            double normalPris = normal.getPrice();
            double aktuelPris = (tilbud != null) ? tilbud.getPrice() : normalPris;

            bon.append(normal.getName()).append("\n");
            bon.append(String.format(dk, "%d × %.2f kr. = %.2f kr.%n",
                    n, normalPris, n * normalPris));

            if (tilbud != null) {
                double rabat = (aktuelPris - normalPris) * n;   // negativt tal
                bon.append(String.format(dk, "Tilbud: %.2f kr.%n", rabat));
            }

            total += n * aktuelPris;
        }
        double moms = total * 0.2;

        bon.append("----------------------\n");
        bon.append(String.format(dk, "TOTAL: %.2f kr.%n", total));
        bon.append(String.format(dk, "Heraf moms: %.2f kr.%n", moms));

        System.out.print(bon);
    }
}