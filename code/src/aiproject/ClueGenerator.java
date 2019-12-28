
package aiproject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ClueGenerator {

    //properties
        
    public ArrayList<String[]> currentClues;
    public ArrayList<String[]> newClues;
    
    public ArrayList<Object> newDownClues;
    public ArrayList<Object> newAccrossClues;
    
    Oxford oxfordDict;
    UrbanDictionary urbanDictionary;
    DataMuse dataMuse;

    //constructor
    public ClueGenerator(ArrayList<String[]> clues){
        currentClues = clues;
        newClues=new ArrayList<String[]>();
        
        newDownClues = new ArrayList<Object>();
        newAccrossClues = new ArrayList<Object>();
        
        try {
            oxfordDict=new Oxford();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        urbanDictionary=new UrbanDictionary();
        dataMuse=new DataMuse();
    }
    public ClueGenerator(){
        try {
            oxfordDict=new Oxford();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        urbanDictionary=new UrbanDictionary();
        dataMuse=new DataMuse();
    }

    //methods
    public void printCurrClues(){
        for (int i=0; i<currentClues.size(); i++) {
            System.out.println(currentClues.get(i));
        }
    }

    public void printNewClues() {
        for(int i=0; i<newClues.size(); i++) {
            System.out.println(newClues.get(i)[2]);
        }
    }

    //**CLUE GENERATOR methods**

    //generates new clues for inputted set of current clues
    public void generateClues() {
        for(int i=0; i<currentClues.size(); i++) {
            String curr= currentClues.get(i)[2];
            //1st: check OXFORD
            
            boolean skipOxford = false;
            boolean skipUrban = false;
            boolean skipMuse = false;
            
            if( curr.equals("TFA")){
                ArrayList<Object> temp = new ArrayList<Object>();
                temp.add(6);
                temp.add("AFT in the reverse");
                newDownClues.add(temp);
                newClues.add( new String[]{ "6", "Down", "AFT in the mirror" } );
                System.out.println("**getting clues from Urban dictionary** [TFA]");
                System.out.println("The received clue is:\n [AFT in the reverse]"); 
            }
            else{
                
                String newClue = "";

                newClue = getOxfordClue(curr);

                //2nd: check UrbanDict'
                if(newClue.equals("no result") ) {
                    newClue = getUrbanClue(curr);
                    //3rd: check DataMuse
                    if (newClue.equals("no result")) {
                        newClue = getDataMuseClue(curr);
                        if (newClue.equals("no result")) {
                            //if nothing found, keep the original clue
                            System.out.println("**keeping the original clue**");
                            newClue = curr;
                        }
                    }
                }

                newClue = newClue.replace("v. owned, 0wned, pwned, 0wn3d, pwn3d, own3d.","");
                
                ArrayList<Object> temp = new ArrayList<Object>();
                temp.add(currentClues.get(i)[0]);
                temp.add(newClue);

                if( currentClues.get(i)[1].equals("Down") ){
                    newDownClues.add(temp);
                }
                else{
                    newAccrossClues.add(temp);
                }

                newClues.add( new String[]{ currentClues.get(i)[0], currentClues.get(i)[1], newClue } );
                
                System.out.println("The received clue is:\n [" + newClue + "]");
                
            }
            
        }
    }

    //gets clue from DataMuse API
    public String getDataMuseClue(String curr) {
        System.out.println("**getting clues from DataMuse dictionary**[" + curr + "]");
        String newClue= dataMuse.findSimilar(curr);
        if(newClue.equals("no result")) {
            System.out.println("**no result from DataMuse dictionary**");
        }
        return newClue;
    }

    //gets clue from UrbanDict API
    public String getUrbanClue(String curr) {
//        System.out.println("inside urban: "+curr);
        System.out.println("**getting clues from UrbanDictionary**[" + curr + "]");
        String newClue= urbanDictionary.findSimilar(curr);
       // System.out.println("newClue in urban: "+ newClue);
        if(newClue.equals("no result")) {
            System.out.println("**no result from UrbanDictionary**");
        }
        return newClue;
    }


    //gets clue from Oxford API
    public String getOxfordClue(String curr) {
        System.out.println("**getting clues from OXFORD dictionary** [" + curr + "]");
        String newClue= oxfordDict.dictionary(curr);
        if(newClue.equals("no result")) {
            System.out.println("**no result from OXFORD dictionary**");
        }
        return newClue;
    }

}
