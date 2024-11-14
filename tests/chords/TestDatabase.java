import chords.ProgManager;
import chords.Progression;

import java.util.ArrayList;
// import java.util.Arrays;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TestDatabase {
    public static void main(String[] args) {
        try (InputStream inputStream = new FileInputStream("./data/progs.txt")) {

            ArrayList<Progression> progs = ProgManager.readProgressions(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
