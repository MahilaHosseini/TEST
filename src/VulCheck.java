
import java.net.UnknownHostException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Scanner;

public class VulCheck {

    private static HttpURLConnection con;
   // private static Scanner input = new Scanner(System.in);
    private static String url, url2;
    private static String urlParameters;
    private static StringBuilder content;
    private static URL myUrl;
    private static Integer count = 0;
    //private static boolean cv = false;
    private static HashSet<String> links;
//    private static HashSet<String> Li

    public static void main(String[] args) throws Exception {


        url = args[0];
        links = HTMLParser.listurl(url);

        for (String link : links) {

            url2 = GetURLParam.sendGet(link);
            if (url2 != null) {
                url2 = link.substring(0, link.indexOf("/", 7)) + url2 + "/";

                // System.out.println(" url is :" + url2);
                urlParameters = "comment=<script>alert(\"XSS Attacked" + count.toString() + "\")</script>";
                byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
                urlParameters = urlParameters.split("=")[1];
                myUrl = new URL(url2);

                checkVulnerable(postData, link);
            }
            count++;
        }
    }

    private static void checkVulnerable(byte[] postData, String link) throws IOException {

        con = (HttpURLConnection) myUrl.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");

        try {
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            } catch (UnknownHostException e) {
                System.out.println("UnknownHostException on :  " + myUrl);

            }


            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }

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
