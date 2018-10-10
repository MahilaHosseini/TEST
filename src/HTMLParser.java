
import java.util.HashSet;
import java.util.ArrayList;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.InetAddress;
import java.net.UnknownHostException;


public class HTMLParser {
    static InetAddress[] inetAddresses;
    static String site2, site;
    static boolean ip = false;

    public final static HashSet<String> listurl(String args) throws Exception {

        site2 = args.substring(args.indexOf(":") + 3);
        System.out.println("site2 " + site2);

        try {
            inetAddresses = InetAddress.getAllByName(site2);
        } catch (UnknownHostException e) {
            System.out.println("no ip address");
            ip = true;

        }

        if (!ip) {
            for (InetAddress ina : inetAddresses)
                System.out.println(ina);
            System.out.println("done");
        }
        //System.out.println(ip);
        HashSet<String> links = new HashSet<String>();
        links.addAll(extractLinks(args));
        links.add(args);
        ArrayList<String> links3 = new ArrayList<String>();
        ArrayList<String> links2 = new ArrayList<String>();
        links3.add(args);
        links2.addAll(links);
        while (!links3.isEmpty()) {
            links3.removeAll(links3);
            for (String link : links2)
                if (!link.contains(".jpg"))
                    links3.addAll(extractLinks(link));
            links.addAll(links3);
            links2 = links3;

        }
        for (String link : links) {
            System.out.println(link);
        }
        System.out.println("*****************************************************************************************");
        return links;

    }

    public static ArrayList<String> extractLinks(String url) throws Exception {
        final ArrayList<String> result = new ArrayList<String>();
        Document doc = null;
        boolean NotAccessible = false;
        try {
            doc = Jsoup.connect(url).get();
        } catch (HttpStatusException e) {
            System.out.println("Not Accessible :  " + url.toString());
            NotAccessible =true;
        }
        if (!NotAccessible) {
            Elements links = doc.select("a[href]");

            /*if provided url is not ip address then all the associated ip addresses will be checked to see if the
             * link is for the site itself or is redirect to another site*/

            if (ip) {
                String l;
                for (Element link : links) {
                    l = link.attr("abs:href");
                    if (l.contains(site2))
                        result.add(l);

                }
            }

            /*if the provided url is an ip itself then it will only check for it */
            else {
                for (Element link : links) {
                    for (InetAddress ina : inetAddresses) {
                        if (link.toString().contains(ina.toString()) || link.toString().contains(site2)) {
                            result.add(link.attr("abs:href"));
                            break;
                        }
                    }
                }


            }

            return result;
        } else {
            return result;
        }
    }


}

