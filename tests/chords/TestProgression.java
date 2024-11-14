import chords.Chord;
import chords.Progression;

import java.util.ArrayList;
import java.util.Arrays;


public class TestProgression {

    public static void main(String[] args) {
        Chord chord1 = new Chord("D", "m");
        Chord chord2 = new Chord("G", "dom");
        Chord chord3 = new Chord("C", "m");
        
        Progression prog1 = new Progression(new ArrayList<>(Arrays.asList(chord1,chord2,chord3)), "C");
        System.out.println("First progression : " + prog1);
        System.out.println("its category " + prog1.categoryToString());

        Chord chord4 = new Chord("E", "m");
        Chord chord5 = new Chord("A", "");
        Chord chord6 = new Chord("D", "");

        Progression prog2 = new Progression(new ArrayList<>(Arrays.asList(chord4,chord5,chord6)), "D");
        System.out.println("Second progression : " + prog2);
        System.out.println("its category " + prog2.categoryToString());

        System.out.println("Category equality test of prog1&2: " + prog1.isSameCategory(prog2));

        Progression prog3 = new Progression("Cmaj7/9 Bbm Daug Gm", "C");
        System.out.println("Third progression : " + prog3);
        System.out.println("its category : " + prog3.categoryToString());

        prog3.transpose(3);
        System.out.println("Third progression transposed: " + prog3);
        System.out.println("its category : " + prog3.categoryToString());
    }

}
