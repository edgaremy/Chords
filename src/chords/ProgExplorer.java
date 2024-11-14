package chords;

// import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class ProgExplorer {

    public static Progression getRandomProg(ArrayList<Progression> list){
        if (list.size()==0){
            return null;
        }
        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    public static Progression getRandomProg(ArrayList<Progression> list, int maxNbChords){
        ArrayList<Progression> subList = new ArrayList<>();
        for (int i=0; i<list.size(); i++){
            Progression prog = list.get(i);
            if (prog.getNbChords() <= maxNbChords){
                subList.add(prog);
            }
        }
        if (subList.size()==0){
            return null;
        }
        int index = new Random().nextInt(subList.size());
        return subList.get(index);
    }

    public static  String addRandomExtensions() {
        StringBuilder ext = new StringBuilder();
        double dice;
        boolean hasExtension = false;
        if (new Random().nextDouble() > 0.5) {
            ext.append("m");

            dice = new Random().nextDouble();
            if (dice < 0.33) {
                ext.append("7");
                hasExtension = true;
            }

        } else {
            dice = new Random().nextDouble();
            if (dice < 0.33) {
                ext.append("7");
                hasExtension = true;
            } else if (dice < 0.66) {
                ext.append("maj7");
                hasExtension = true;
            }
        }

        if (new Random().nextDouble() < 0.2) {
            if (hasExtension) {
                ext.append("/9");
            } else {
                ext.append("9");
            }
        }

        return ext.toString();
    }

    public static  Progression getTrulyRandomProg(int maxNbChords){
        try {
            int nbChords;
            if (maxNbChords < 9) {
                nbChords = maxNbChords;
            } else {
                nbChords = new Random().nextInt(8) + 1;
            }
            StringBuilder prog = new StringBuilder();
            int index = new Random().nextInt(Chord.notesUp.length);
            prog.append(Chord.notesUp[index]);
            prog.append(addRandomExtensions());
            for (int i=1; i<nbChords; i++){
                index = new Random().nextInt(Chord.notesUp.length);
                prog.append(" ").append(Chord.notesUp[index]);
                prog.append(addRandomExtensions());
            }
            //Log.d("debug", prog.toString());
            return new Progression(prog.toString());
        } catch (UnrecognizedProgressionException | UnrecognizedChordException e) {
            return null;
        }

    }
}
