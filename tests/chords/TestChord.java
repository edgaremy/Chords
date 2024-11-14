import chords.Chord;
import java.util.ArrayList;
import java.util.Arrays;

public class TestChord {

    public static void main(String[] args) {
        // Test creation of a Chord object
        Chord chord1 = new Chord("A#", "m", new ArrayList<>(Arrays.asList("7","9")));
        System.out.printf("%-30s: %s%n", "chord1", chord1);

        // Test copying a Chord object
        Chord chord2 = chord1.copy();
        System.out.printf("%-30s: %s%n", "chord2 (copy of chord1)", chord2);

        // Test transposing a Chord object
        chord2.transpose(-3);
        System.out.printf("%-30s: %s%n", "chord2 transposed (-3)", chord2);

        // Test creation of a Chord object with additional parameters
        Chord chord3 = new Chord("Bb", "sus4", new ArrayList<>(Arrays.asList("7","9")), new ArrayList<>(Arrays.asList("b5","maj7")), "G");
        System.out.printf("%-30s: %s%n", "chord3", chord3);

        // Test standardizing chord notation
        String chord4 = "C#m7/9(maj7)\\G";
        String chord5 = "Asus4";
        System.out.printf("%-30s: %s%n", "chord4 standardized", Chord.standardize(chord4));
        System.out.printf("%-30s: %s%n", "chord5 standardized", Chord.standardize(chord5));

        // Test creation and transposition of a complex Chord object
        Chord chord6 = new Chord("C#m7/9(maj7/b5/#4)\\G");
        System.out.printf("%-30s: %s%n", "chord6", chord6);
        chord6.transpose(3);
        System.out.printf("%-30s: %s%n", "chord6 transposed (+3)", chord6);
    }

}
