import java.util.*;

public class Robot {

    FileIO fileIO;

    private int min = 5;
    private int max = 25;
    private int sandsynlighedForKun1Vare = 70;
    private int maxStyk = 5;

    public Robot() {
        fileIO = new FileIO();
    }

    public ArrayList<Vare> fyldIKurv(ArrayList<Vare> alleVarer) {
        Random random = new Random();

        ArrayList<Vare> kurv = new ArrayList<>();

        int antalVarer = random.nextInt(min, max);
        for (int i = 0; i < antalVarer; i++) {
            int styk = (random.nextInt(100) > sandsynlighedForKun1Vare) ? random.nextInt(2, maxStyk) : 1;

            Vare vare = alleVarer.stream().skip(random.nextInt(alleVarer.size())).findFirst().get();
            while (styk > 0) {
                kurv.add(vare);
                styk--;
            }
        }

        Collections.shuffle(kurv);
        return kurv;
    }
}