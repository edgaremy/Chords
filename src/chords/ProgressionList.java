package chords;

import java.io.Serializable;
import java.util.ArrayList;

public class ProgressionList implements Serializable {

    public ProgressionList(ArrayList<Progression> inProgs){
        progs = new ArrayList<Progression>(inProgs);
    }

    public ArrayList<Progression> progs;
}
