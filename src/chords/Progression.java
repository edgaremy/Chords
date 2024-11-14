package chords;

import java.io.Serializable;
import java.util.ArrayList;

public class Progression implements Serializable {
    
	private static final long serialVersionUID = 2295223239592819082L;
	
	private int nbChords; // number of chords in the progression
    private String key; // key of the relative I
    private ArrayList<String> category; // progr in numbers relative to the I-key
    private ArrayList<Chord> chords; // progr with absolute chords

    // create absolute progression based on chordsArray
    public Progression(ArrayList<Chord> prog, String key) {
        this.chords = new ArrayList<>(prog);
        this.nbChords = prog.size();
        this.key = key;
        this.updateCategory();
    }

    // create progression based on string with all chords (space in between, comma before key)
    public Progression(String prog, String key) {
        this.chords = new ArrayList<>();
        String[] chds = prog.split(" ");
        // converts String array to Chord array:
        for (int i=0; i<chds.length;i++){
            this.chords.add(new Chord(chds[i]));
        }
        this.nbChords = chords.size();
        this.key = key;
        this.updateCategory();
    }

    // create a progression from string with no key (key defaulted to C)
    public Progression(String prog) {
        this.chords = new ArrayList<>();
        String[] chds = prog.split(" ");
        // converts String array to Chord array:
        for (int i=0; i<chds.length;i++){
            this.chords.add(new Chord(chds[i]));
        }
        this.nbChords = chords.size();
        this.key = "C";
        this.updateCategory();
    }

    public Progression(Progression copy) {
        this.chords = new ArrayList<>(copy.chords);
        this.nbChords = copy.nbChords;
        this.key = copy.key;
        this.updateCategory();
    }

    public Progression simplify() {
        Progression simpler = new Progression(this);
        for (int i = 0; i < simpler.nbChords; i++) {
            simpler.chords.set(i, simpler.chords.get(i).simplify());
        }
        return simpler;
    }

    public Progression noExtensions() {
        Progression simpler = new Progression(this);
        for (int i = 0; i < simpler.nbChords; i++) {
            simpler.chords.set(i, simpler.chords.get(i).noExtensions());
        }
        return simpler;
    }

    public Progression noVariations() {
        Progression simpler = new Progression(this);
        for (int i = 0; i < simpler.nbChords; i++) {
            simpler.chords.set(i, simpler.chords.get(i).noVariations());
        }
        return simpler;
    }

    public Progression onlySeventh() {
        Progression simpler = new Progression(this);
        for (int i = 0; i < simpler.nbChords; i++) {
            simpler.chords.set(i, simpler.chords.get(i).onlySeventh());
        }
        return simpler;
    }

    // gives the number of chords (same chords are both counted) contained in the progression
    public int getNbChords(){
        return this.nbChords;
    }

    public String getKey(){
        return new String(this.key);
    }

    public Chord getChordFromIndex(int index) {
        return new Chord(this.chords.get(index));
    }

    // gives number associated to interval
    private String getNumberOfChord(String root, Chord chord){
        switch(Chord.getInterval(root, chord.getKey())){
            case 0:
                return "I";
            case 1:
                return "bII";
            case 2:
                return "II";
            case 3:
                return "III"; // minor third
            case 4:
                return "III"; // major third
            case 5:
                return "IV";
            case 6:
                return "#IV";
            case 7:
                return "V";
            case 8:
                return "VI"; // minor sixth
            case 9:
                return "VI"; // major sixth
            case 10:
                return "VII"; // minor seventh
            case 11:
                return "VII"; // major seventh
            default:
                throw new UnrecognizedChordException("** could not find the number of the chord **");
        }
    }

    // returns null if invalid category roman number
    private String getKeyOfNumber(String root, String number){
        switch(number){
            case "i": case "I": case "1":
                return new String(root);
            case "#i": case "#I": case "#1": case "bii": case "bII": case "b2":
                return Chord.transposeKey(root, 1);
            case "ii": case "II": case "2":
                return Chord.transposeKey(root, 2);
            case "iii": case "III": case "3":
                return Chord.transposeKey(root, 4);
            case "iv": case "IV": case "4":
                return Chord.transposeKey(root, 5);
            case "#iv": case "#IV": case "#4": case "bv": case "bV": case "b5":
                return Chord.transposeKey(root, 6);
            case "v": case "V": case "5":
                return Chord.transposeKey(root, 7);
            case "vi": case "VI": case "6":
                return Chord.transposeKey(root, 9);
            case "vii": case "VII": case "7":
                return Chord.transposeKey(root, 11);
            default:
                return null;
        }
    }

    // not very useful
    // update chords prog based on the category (only for full Major)
    private void updateChords(){
        this.chords = new ArrayList<>();
        for (int i=0; i<this.nbChords; i++){
            String chordKey = getKeyOfNumber(this.key, this.category.get(i));
            if (chordKey.equals(null)) throw new UnrecognizedProgressionException("** unrecognized number in progression **");
            chords.add(new Chord(chordKey, ""));
        }
    }

    private void updateCategory(){
        this.category = new ArrayList<>();
        for (int i=0; i<this.nbChords; i++){
            this.category.add(getNumberOfChord(this.key, this.chords.get(i)));
        }
    }

    public void transpose(int halfsteps){
        this.key = Chord.transposeKey(this.key, halfsteps);
        for (int i=0; i<this.chords.size(); i++){
            Chord newChord = this.chords.get(i);
            newChord.transpose(halfsteps);
            this.chords.set(i,newChord);
        }
        this.updateCategory();
    }

    public Boolean isSameCategory(Progression prog){
        /*if (this.nbChords != prog.nbChords){
            return false;
        }*/
        return this.category.equals(prog.category);
    }

    public String categoryToString(){
        String cat = this.category.get(0).toString();
        for (int i=1; i<this.nbChords; i++){
            cat += "-" + this.category.get(i).toString();
        }
        return cat;
    }

    @Override
    public String toString(){
        String prog = this.chords.get(0).toString();
        for (int i=1; i<this.nbChords; i++){
            prog += " " + this.chords.get(i).toString();
        }
        return prog;
    }
}