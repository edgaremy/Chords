package chords;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.Math;

// representation of chords (absolute position)
public class Chord implements Serializable {

	private static final long serialVersionUID = -1744179042860092145L;
	
	public static final String[] notesUp = {"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"};
    public static final String[] notesDown = {"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"};
    public static final String[] types = {"","m","aug","dim","sus2","sus4"};
    public static final String[] additions = {"2","4","5", "6","maj7","7","maj9","9","11","13"};
    public static final String[] modifications = {"#4","b5","b9","#11","maj7"};


    private static final String notePattern = "(A#|C#|D#|F#|G#|Ab|Bb|Db|Eb|Gb|A|B|C|D|E|F|G)";
    private static final String pretypePattern = "(m)";
    private static final String posttypePattern = "(aug|dim|sus2|sus4)";
    private static final String addPattern = "(2|4|5|6|maj6|maj7|7|maj9|9|11|13)";
    private static final String modPattern = "(b2|b3|#4|b5|#5|b6|#6|b9|#9|#11|b13|maj6|maj7)";
    private static final String addPatterns = "(" +addPattern+ "(\\/"+addPattern+")*)";
    private static final String modPatterns = "(\\(" +modPattern+ "(\\/"+modPattern+")*\\))";

    private static final String chordPattern = notePattern+ "(" +pretypePattern+"?" +addPatterns+"?|" +addPatterns+"?" +posttypePattern+ ")(" + modPatterns+"?)(\\\\" +notePattern+ ")?";

    private String name;
    private String key;
    private String type;
    private ArrayList<String> add; // added notes
    private ArrayList<String> mod; // modified notes (specified after the type)
    private String bass;

    //most precise constructor
    public Chord(String key, String type, ArrayList<String> add, ArrayList<String> mod, String bass) {
        this.key = new String(key);
        this.type = new String(type);
        this.add = new ArrayList<>(add);
        this.mod = new ArrayList<>(mod);
        this.bass = new String(bass);
        this.updateName();
    }

    public Chord(Chord copy) {
        this.key = new String(copy.key);
        this.type = new String(copy.type);
        this.add = new ArrayList<>(copy.add);
        this.mod = new ArrayList<>(copy.mod);
        this.bass = new String(copy.bass);
        this.updateName();
    }

    public Chord(String key, String type, ArrayList<String> add, ArrayList<String> mod) {
        this(key,type,add,mod,key);
    }

    public Chord(String key, String type, ArrayList<String> add) {
        this(key,type,add,new ArrayList<>(),key);
    }

    //simplest constructor
    public Chord(String key, String type) {
        this(key,type,new ArrayList<String>(),new ArrayList<>(),key);
    }

    // construct from chord name
    public Chord(String name) throws UnrecognizedChordException {
        this.name = standardize(name);
        this.add = new ArrayList<>();
        this.mod = new ArrayList<>();
        if (this.name == null) {
            throw new UnrecognizedChordException("** no chord matching **");
        }

        String buffer = new String(this.name);
        int nbCharRemoved = 0;

        // get key:
        this.key = "" + buffer.charAt(0);
        nbCharRemoved++;
        if (buffer.length()>1 && (buffer.charAt(1)=='#' || buffer.charAt(1)=='b')) {
            this.key = this.key + buffer.charAt(1);
            nbCharRemoved++;
        }
        buffer = buffer.substring(nbCharRemoved);
        nbCharRemoved = 0;
        // get bass note:
        int n = buffer.length();
        if (n>1 && buffer.charAt(n-2) == '\\'){
            this.bass = "" + buffer.charAt(n-1);
            nbCharRemoved = 2;
        } else if (n>2 && buffer.charAt(n-3) == '\\'){
            this.bass = "" + buffer.charAt(n-2) + buffer.charAt(n-1);
            nbCharRemoved = 3;
        } else { // no specified bass
            this.bass = this.key;
        }
        buffer = buffer.substring(0,n-nbCharRemoved);
        nbCharRemoved = 0;
        // get type:
        if (buffer.length()>0){
            // treats 'maj' first (if not 'm' could be detected instead)
            if (buffer.length()>2 && buffer.charAt(0)=='m' && buffer.charAt(1)=='a'){
                this.type = "";
            } else {
                Boolean found = false;
                nbCharRemoved = 1;
                while (!found && nbCharRemoved <= Math.min(4,buffer.length())) {
                    String word = buffer.substring(0,nbCharRemoved);
                    if (Arrays.asList(types).contains(word)){
                        this.type = word;
                        buffer = buffer.substring(nbCharRemoved);
                        found = true;
                    }
                    nbCharRemoved++;
                }
                if (!found) {
                    this.type = "";
                }
                nbCharRemoved = 0;
            }
        } else {
            this.type = "";
        }
        // get added notes & modifications:
        String[] splitted = buffer.split("\\(");
        if (splitted.length>1){ //case with modifications
            String bufferMod = splitted[1];
            bufferMod = bufferMod.substring(0,bufferMod.length()-1); //removes the ')' at the end
            String[] splitMod = bufferMod.split("/");
            for (int i=0; i<splitMod.length; i++){
                this.mod.add(splitMod[i]);
            }
        }
        String bufferAdd = splitted[0];
            String[] splitAdd = bufferAdd.split("/");
            for (int i=0; i<splitAdd.length; i++){
                this.add.add(splitAdd[i]);
            }
        this.updateName();
    }

    // simplify to the extreme
    public Chord simplify() {
        Chord simpler = new Chord(this);
        if (this.type.equals("m")) {
            simpler.type = "m";
        } else {
            simpler.type = "";
        }
        simpler.mod.clear();
        simpler.add.clear();
        simpler.updateName();
        return simpler;
    }

    public Chord noExtensions() {
        Chord simpler = new Chord(this);
        simpler.mod.clear();
        simpler.add.clear();
        simpler.updateName();
        return simpler;
    }

    public Chord noVariations() {
        Chord simpler = new Chord(this);
        if (this.type.equals("m")) {
            simpler.type = "m";
        } else {
            simpler.type = "";
        }
        simpler.updateName();
        return simpler;
    }

    public Chord onlySeventh() {
        Chord simpler = new Chord(this);
        simpler.mod.clear();
        simpler.add.clear();
        if (this.add.contains("7"))
            simpler.add.add("7");
        if (this.add.contains("maj7"))
            simpler.add.add("maj7");
        simpler.updateName();
        return simpler;
    }

    public String getKey(){
        return new String(this.bass);
    }

    public String getType(){
        return new String(this.type);
    }

    public ArrayList<String> getMod(){
        return new ArrayList<>(this.mod);
    }

    // returns null if unrecognized pattern
    public static String standardize(String name) {
        Pattern pattern = Pattern.compile(chordPattern);
        Matcher matcher = pattern.matcher(name);
        boolean matchFound = matcher.matches();
        if (!matchFound) return null;
        return name;
    }

    // updates the name of the chord
    private void updateName() {
        String name_temp;
        Boolean typeAjoute = false;
        name_temp = this.key;
        if (this.type.equals("") || this.type.equals("m")){
            name_temp += type;
            typeAjoute = true;
        }
        if (add.size() > 0){
            name_temp += add.get(0);
            for (int i=1;i<add.size();i++) {
                name_temp += "/" + add.get(i);
            }
        }
        if (!typeAjoute){
            name_temp += type;
        }
        if (mod.size() > 0){
            name_temp += "(" + mod.get(0);
            for (int i=1;i<mod.size();i++) {
                name_temp += "/" + mod.get(i);
            }
            name_temp += ")";
        }

        if (!key.equals(bass)){
            name_temp += "\\" + bass;
        }

        this.name = name_temp;
    }


    public Chord copy(){
        Chord copy = new Chord(this.key,this.type,this.add,this.mod,this.bass);
        return copy;
    }


    public static String transposeKey(String key,int halfsteps){
        int index;
        String newKey;
        // if the key is flat:
        if (key.endsWith("b")) {
            index = Arrays.asList(notesDown).indexOf(key);
        } else { // if the key is natural/sharp:
            index = Arrays.asList(notesUp).indexOf(key);
        }
        index = ((index + halfsteps)+12) % 12;
        if (halfsteps < 0){ // going down
            newKey = new String(notesDown[index]);
        } else { // going up
            newKey = new String(notesUp[index]);
        }
        return newKey;
    }

    public void transpose(int halfsteps){
        // transpose the key:
        this.key = transposeKey(this.key, halfsteps);
        // transpose the bass:
        this.bass = transposeKey(this.bass, halfsteps);
        // updates the name of the chord:
        this.updateName();

    }

    // determines the interval in halfsteps between key1 and key2 (order matters!)
    public static int getInterval(String key1,String key2){
        int index1,index2;
        // converts both keys to flat format
        if (key1.length()>1 && key1.charAt(1) == 'b'){
            index1 = Arrays.asList(notesDown).indexOf(key1);
        } else {
            index1 = Arrays.asList(notesUp).indexOf(key1);
        }
        if (key2.length()>1 && key2.charAt(1) == 'b'){
            index2 = Arrays.asList(notesDown).indexOf(key2);
        } else {
            index2 = Arrays.asList(notesUp).indexOf(key2);
        }
        
        return (12-index1+index2)%12;
    }

    @Override
    public String toString(){
        return this.name;
    }

    public String toStringFR(){
        return null; //TODO
    }
}