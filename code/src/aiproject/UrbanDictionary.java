package aiproject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import  org.json.simple.parser.JSONParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by bmmuradov on 20.12.2019.
 */
public class UrbanDictionary {

    //properties

    //constructors
    public UrbanDictionary(){
    }


    public String findSimilar(String word) {
        String s = word.replaceAll(" ", "+");
        //System.out.println("inside findSim: "+s);
        return getWord(getJSON("http://api.urbandictionary.com/v0/define?term="+s));
    }

    private String getJSON(String url) {
        URL datamuse;
        URLConnection dc;
        StringBuilder s = null;
        try {
            datamuse = new URL(url);
            dc = datamuse.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(dc.getInputStream(), "UTF-8"));
            String inputLine;
            s = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
                s.append(inputLine);
            in.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s != null ? s.toString() : null;
    }

    //parses the json String to get word data
    private String getWord(String jsonStr) {
        ArrayList<String> defs= new ArrayList<>();
        String result=null;
        JSONParser parser= new JSONParser();
        Object parse=null;
        try{
            parse=parser.parse(jsonStr);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject=(JSONObject) parse;
        //System.out.println(jsonObject.toString());
        JSONArray res=(JSONArray) jsonObject.get("list");

        for(Object e: res) {
            JSONObject senJson= (JSONObject) e;
            result=senJson.get("definition").toString();
            result = result.replaceAll("\\[", "").replaceAll("\\]","");
            defs.add(result);
        }
        if(defs.size()==0) {
            //System.out.println("no result from UrbanDictionary");
            return "no result";
        }
        else {
            //randomize result
            int rnd = (int) Math.random() * defs.size();
            result = defs.get(rnd);
            return result;
        }
    }

}
