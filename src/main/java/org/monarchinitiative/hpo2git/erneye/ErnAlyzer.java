package org.monarchinitiative.hpo2git.erneye;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class ErnAlyzer {



    private List<ErnEntry> entries=new ArrayList<>();

    private int MINLEN=3;

    private String password;





    public void output() {
        for (ErnEntry ern: entries) {
            System.out.println(ern.toString());
            try {
                postIt(ern);
            } catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    }



    public ErnAlyzer(String path, String pword) {
        this.password = pword;
        Set<String> actions=new HashSet<>();
        Term previousTerm=null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line=null;
            while ((line=br.readLine())!=null) {

                if (line.contains("HP:0000478")) {
                    //System.out.println(line);
                    break; // this is the first content line
                }
            }
            while ((line=br.readLine())!=null) {
                String A[] = line.split("\t");
                int level=0;
                if (A.length<MINLEN) {
                    System.err.println("[WARNING SHORT LINE: " + line);
                    continue;
                }
                String action = A[3].toLowerCase().replaceAll("!","");
                actions.add(action);
                Term term=null;
                //System.out.println(A[3]);
                for (int i=4;i<A.length&& i<=14;++i) {
                    if (A[i]!=null && !A[i].isEmpty()) {
                        //System.out.println(String.format("%d-%s",i,A[i] )); level=i;
                        term = new Term(A[i],i);
                    }
                }
                String message = Arrays.stream(A).filter(s ->  s != null ).filter(s -> s.length()>0).collect(Collectors.joining("//"));
                //System.out.println(String.format("%s  %d  %s ",action, level,message));
                ErnEntry entry = new ErnEntry(action,term,previousTerm,message);
                previousTerm=term;
                entries.add(entry);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (String a:actions) {
            System.out.println(a);
        }
    }







    public void postIt(ErnEntry ern) throws Exception {
        URL url = new URL("https://github.com/obophenotype/pnrobinson/human-phenotype-ontology/issues/");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST"); // PUT is another valid option
        http.setDoOutput(true);

        Map<String,String> arguments = new HashMap<>();

        String myTitle=String.format("ERN-EYE NTR: %s",ern.term.name);
        arguments.put("username", "pnrobinson");
       // arguments.put("password", this.password); // pass from command line
        arguments.put("title",myTitle);
        arguments.put("body",ern.toString());
        arguments.put("labels", "ophthalmology");

        StringJoiner sj = new StringJoiner("&");
        for(Map.Entry<String,String> entry : arguments.entrySet())
            sj.add(URLEncoder.encode(entry.getKey(), "UTF-8") + "="
                    + URLEncoder.encode(entry.getValue(), "UTF-8"));
        byte[] out = sj.toString().getBytes(StandardCharsets.UTF_8);
        String test="{"+
                "\"title\": \"Found a bug\", "+
                "\"body\": \"I'm having a problem with this.\","+
                "}";
       // out = test.toString().getBytes(StandardCharsets.UTF_8);
        int length = out.length;


        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        System.out.println(http.toString());
        http.connect();
        String str = new String(out);
        System.out.println(str);
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
            os.close();
        }
       System.out.println("Response:"+http.getResponseCode());
    }


}
