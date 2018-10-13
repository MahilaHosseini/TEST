
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class VulCheck {

    private static HttpURLConnection con;
    // private static Scanner input = new Scanner(System.in);
    private static String url, url2, inpname;
    private static HashMap<String, String> param;
    private static String urlParameters;
    private static StringBuilder content;
    private static URL myUrl;
    private static Integer count = 0;
    private static HashSet<String> links;

    public static void main(String[] args) throws Exception {


        url = args[0];
        links = HTMLParser.listurl(url);
        //System.out.println("link number:  " + links.size());
        for (String link : links) {
            //System.out.println(count +"  "+ link);
            param = new HashMap<String, String>();
            param = GetInputName.sendGet(link);

            if (!param.isEmpty()) {
                url2 = param.get("action");
                if (!url2.contains("http"))
                    url2 = link.substring(0, link.indexOf("/", 7)) + url2 + "/";
                myUrl = new URL(url2);
                System.out.println("link is : " +link);
                System.out.println("url is : " +url2);
                for (int i = 1; i < param.size(); i++) {
                    inpname = "input" + i;
                    inpname = param.get(inpname);
                    urlParameters = inpname + "=<script>alert(\"XSS Attacked" + count.toString() + "\")</script>";
                    System.out.println("param is :  " + urlParameters);
                    byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                    urlParameters = urlParameters.split("=")[1];
                    checkVulnerable(postData, link);
                    count++;
                }
            }


        }
    }

    private static void checkVulnerable(byte[] postData, String link) throws IOException {

        con = (HttpURLConnection) myUrl.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");

        try {
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
                try (BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()))) {

                    String line;
                    content = new StringBuilder();
                    while ((line = in.readLine()) != null) {
                        content.append(line);
                        content.append(System.lineSeparator());
                    }

                } catch (FileNotFoundException e) {
                    //System.out.println("FileNotFoundException");
                } catch (IOException ei) {
                    //System.out.println("IOException");
                }


            } catch (UnknownHostException e) {
                // System.out.println("UnknownHostException on :  " + myUrl);

            }


        } finally {
            con.disconnect();
        }


        if (content.toString().contains(urlParameters)) {
            System.out.println("xss vulnerable");
            System.out.println("***" + link + "***");

        }
    }


}
