import chords.ProgManager;
import chords.Progression;

import java.util.ArrayList;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;

public class TestProgManager {
    
    public static void main(String[] args) {

        Progression prog1 = new Progression("Cmaj7/9 Bbm Daug Gm", "C");
        Progression prog2 = new Progression("Dmaj7/9 Gm Dsus2 Fmaj7", "G");

        ProgManager.writeProgression(new File("./data/progsTest.txt"), prog1);
        ProgManager.writeProgression(new File("./data/progsTest.txt"), prog2);

        ProgManager.writeProgressions(new File("./data/progsTest.txt"), new ArrayList<>(Arrays.asList(prog1,prog2)), false);

        try {
            ArrayList<Progression> progs = ProgManager.readProgressions(new FileInputStream("./data/progsTest.txt"));
            ProgManager.saveProgressions("./data/dataTest.progs", progs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Progression> progsBis = ProgManager.loadProgressions("./data/dataTest.progs");
        ProgManager.writeProgressions(new File("./data/progsTest2.txt"), progsBis, false);
    }

}
