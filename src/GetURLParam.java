import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
//import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;


public class GetURLParam {
    private static int i, j;
    private static BufferedReader in;

    private static String sub, sub2;

    public static String sendGet(String url) throws Exception {
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
            }
            in.close();
            if (response.toString().contains("<form ")) {
                i = response.toString().indexOf("<form ");
                j = response.toString().indexOf(">", i);
                // System.out.println("i is: "+i);
                // System.out.println("j is:" +j);

                sub = response.toString().substring(i, j);

                i = sub.indexOf(" action=") + 8;
                sub2 = sub.substring(i);
                if (sub2.contains(" ")) {
                    j = sub.indexOf(" ", i);
                    sub2 = sub.substring(i, j);

                }
                if (sub2.contains("\""))
                    sub2 = sub2.split("\"")[0] + sub2.split("\"")[1];

                if (sub2.contains("\'"))
                    sub2 = sub2.split("\'")[0] + sub2.split("\'")[1];
                if (sub2.indexOf("/")!=0 && !sub2.contains("http"))
                    sub2="/"+sub2;
                // System.out.println("sub2:  " + sub2);
                return sub2;
            } else
                return null;
        } else
            return null;


        /// find input tag's name




    }

    //    public static void main(String[] args) throws Exception {
//        sendGet("http://192.168.56.102/peruggia/index.php?action=comment&pic_id=1");
//    }
}
