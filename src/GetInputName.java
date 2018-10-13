import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class GetInputName {
    private static int i, j, k;
    private static BufferedReader in;
    private static boolean done = false;
    private static String sub, sub2, sub3, res, inp;
    private static HashMap<String, String> param;
    private static int count = 0;

    public static HashMap sendGet(String url) throws Exception {
        boolean er = false;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        try {
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        } catch (IOException e) {
            er = true;
        }


        if (!er) {
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
                response.append(System.lineSeparator());
            }
            in.close();
            if (response.toString().contains("<form ")) {
                param = new HashMap<String, String>();
                done = false;
                i = response.toString().indexOf("<form ");
                if (response.toString().contains("</form>"))
                    j = response.toString().indexOf("</form>", i);
                else
                    j = response.toString().indexOf(">", i);
                //  System.out.println("i is: " + i);
                //  System.out.println("j is:" + j);

                sub = response.toString().substring(i, j);

                i = sub.indexOf(" action=") + 8;
                sub2 = sub.substring(i);
                if (sub2.contains(" ")) {
                    j = sub.indexOf(" ", i);
                    sub2 = sub.substring(i, j);

                }
                // System.out.println("url i s:  " + url);
                res = response.toString().substring(response.toString().indexOf("<form "));
                // System.out.println(res);
                if (res.contains("input")) {
                    while (!done) {
                        i = res.indexOf("<input ");
                        j = res.indexOf(">", i)+1;
                        sub3 = res.substring(i, j);
                        res = res.substring(j);
                        if (sub3.contains("name")) {
                            if (sub3.contains("\"")) {
                                i = sub3.indexOf("name=") + 6;
                                j = sub3.indexOf("\"", i);
                            } else if (sub3.contains("\'")) {
                                i = sub3.indexOf("name=") + 6;
                                j = sub3.indexOf("\'", i);
                            } else {
                                i = sub3.indexOf("name=") + 5;
                                k = sub3.indexOf(">", i);
                                j = sub3.indexOf(" ", i);
                                if (k < j || j == -1)
                                    j = k;
                            }
                           // System.out.println(sub3 + " url :  " + url + "\n j=" + j);
                            sub3 = sub3.substring(i, j);
                            // System.out.println(sub3);

                            if (!sub3.isEmpty()) {
                                count++;
                                inp = "input" + count;
                                param.put(inp, sub3);
                            }
                        }
                        if (!res.contains("<input"))
                            done = true;

                    }
                }

                if (sub2.contains("\""))
                    sub2 = sub2.split("\"")[0] + sub2.split("\"")[1];

                if (sub2.contains("\'"))
                    sub2 = sub2.split("\'")[0] + sub2.split("\'")[1];
                if (sub2.indexOf("/") != 0 && !sub2.contains("http"))
                    sub2 = "/" + sub2;
                param.put("action", sub2);
                System.out.println("urls:  " + url);


                return param;
            } else {
                param = new HashMap<String, String>();
                return param;
            }
        } else {
            param = new HashMap<String, String>();
            return param;
        }


        /// find input tag's name


    }

    public static void main(String[] args) throws Exception {
        sendGet("http://192.168.56.102/tikiwiki/tiki-pagehistory.php?page=HomePage&source=0");
    }
}
