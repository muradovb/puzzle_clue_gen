package aiproject;

/**
 * Created by bmmuradov on 19.12.2019.
 */
import  org.json.simple.JSONArray;
import  org.json.simple.JSONObject;
import  org.json.simple.parser.JSONParser;
import  javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import  java.io.InputStreamReader;
import  java.net.URL;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Oxford extends UnicastRemoteObject {


    public Oxford() throws RemoteException {
    }

    public String dictionary(String word) {
        ArrayList<String> defs=new ArrayList<>();
        //System.out.println("**inside dictionary**");
        JSONParser parser = new JSONParser();
        String result="";
        Object parse=null;
        try {
            result = getRequest(buildURL(word));
            parse = parser.parse(result);
            JSONObject jsonObject=(JSONObject) parse;
            JSONArray res=(JSONArray) jsonObject.get("results");
            for (Object e:res){
                JSONArray lexicalEntries= (JSONArray)(((JSONObject) e).get("lexicalEntries"));
                for(Object lex:lexicalEntries) {
                    JSONArray entries= (JSONArray)(((JSONObject) lex).get("entries"));
                    for(Object ent:entries) {
                        JSONArray senses= (JSONArray)(((JSONObject) ent).get("senses"));
                        for(Object sen:senses) {
                            JSONObject senJson= (JSONObject) sen;
                            result=senJson.get("definitions").toString();
                            result=result.replaceAll("\\[", "").replaceAll("\\]","");
                            result=result.replaceAll("\"", "");
                            defs.add(result);
                        }
                    }
                }
            }

        } catch (Exception e) {
            //e.printStackTrace();
            return "no result";
        }
        if(defs.size()==0) {
            //System.out.println("no result from OxfordDictionary");
            return "no result";
        }
        else {
            //randomize result
            int rnd = (int) Math.random() * defs.size();
            result = defs.get(rnd);
            return result;
        }
    }

    private String buildURL(final String word) {
        final String language = "en";
        final String word_id = word.toLowerCase();
        return "https://od-api.oxforddictionaries.com:443/api/v2/entries/" + language + "/" + word_id;
    }


    private String getRequest(String link) {
        final String app_id = "7c107869";
        final String app_key = "4b686e55580af94c795652547def1cda";
        try {
            URL url = new URL(link);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setRequestProperty("app_id", app_id);
            urlConnection.setRequestProperty("app_key", app_key);
            // read the output from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
