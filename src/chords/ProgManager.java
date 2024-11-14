package chords;

// import android.content.Context;
// import android.util.Log;


import java.io.File;  // Import the File class
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;



public class ProgManager {
    
    private static void writeProgression(File file, Progression prog, Boolean noOverwrite){
        try {
            FileOutputStream fos = new FileOutputStream(file,noOverwrite);
            String newLine = "";
            if(noOverwrite)
                newLine = "\n";

            String text = newLine + prog.toString() + "," + prog.getKey();
            fos.write(text.getBytes());
            // Log.d("debug", "writing : "+prog.toString());

            fos.close();
          } catch (IOException e) {
            // Log.d("debug","PROGMANAGER: "+ e.getMessage());
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }
    public static void writeProgression(File file, Progression prog){
        writeProgression(file, prog, true);
    }

    public static void writeProgressions(File file, ArrayList<Progression> prog, Boolean noOverwrite){
        try {
            String text;
            FileOutputStream fos = new FileOutputStream(file,noOverwrite);
            for (int i=0; i<prog.size(); i++){
                text = prog.get(i).toString() + "," + prog.get(i).getKey();
                if(i==0 && !noOverwrite){
                    fos.write(text.getBytes());
                } else{
                    fos.write(("\n"+text).getBytes());
                }
                // Log.d("debug", "writing : "+prog.get(i).toString());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Progression> readProgressions(InputStream is){
        ArrayList<Progression> progs = new ArrayList<>();
        //try {
            //File myObj = new File(filename);
            //InputStream is = context.getAssets().open(filename); //TODO adapt this everywhere
            //InputStream is = context.getResources().openRawResource(R.raw.progs);
            Scanner myReader = new Scanner(is);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (data.equals("")){
                    continue;
                }
                // Log.d("debug", "reading : "+data);
                String[] prog = data.split(",");
                if (prog.length > 1){
                    progs.add(new Progression(prog[0], prog[1]));
                } else {
                    progs.add(new Progression(prog[0]));
                }
                
            }
            myReader.close();
        return progs;
    }

    // TODO update
    public static void saveProgressions(String filename, ArrayList<Progression> progs){
        try {
            FileOutputStream fileOut =
            new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(progs);
            out.close();
            fileOut.close();
            //System.out.printf("Serialized data is saved in " + filename);
         } catch (IOException i) {
            i.printStackTrace();
         }
    }

    //TODO update
    public static ArrayList<Progression> loadProgressions(String filename){
         ArrayList<Progression> progs = null;
      try {
         FileInputStream fileIn = new FileInputStream(filename);
         ObjectInputStream in = new ObjectInputStream(fileIn);
         progs = (ArrayList<Progression>) in.readObject();
         in.close();
         fileIn.close();
      } catch (IOException i) {
         i.printStackTrace();
      } catch (ClassNotFoundException c) {
         System.out.println("Class not found");
         c.printStackTrace();
      }
      return progs;
    }
}
