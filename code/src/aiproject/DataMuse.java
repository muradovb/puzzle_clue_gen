package aiproject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by bmmuradov on 19.12.2019.
 */
public class DataMuse {

    //properties

    //constructor
    public DataMuse() {
    }

    public String findSimilar(String word) {
        String s = word.replaceAll(" ", "+");
        return getWord(getJSON("http://api.datamuse.com/words?rd="+s));
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
        JSONArray jsonArray=(JSONArray) parse;
        for (Object e:jsonArray) {
            JSONObject senJson= (JSONObject) e;
            result=senJson.get("word").toString();
            defs.add(result);
        }
        if(defs.size()==0) {
            //System.out.println("no result from DataMuse");
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
