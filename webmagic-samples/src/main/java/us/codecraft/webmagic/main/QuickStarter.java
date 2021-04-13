package us.codecraft.webmagic.main;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.samples.IteyeBlog;
import us.codecraft.webmagic.model.samples.News163;
import us.codecraft.webmagic.model.samples.OschinaBlog;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.MultiPagePipeline;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author code4crafter@gmail.com <br>
 */
public class QuickStarter {
	
	private static Logger logger = LoggerFactory.getLogger(QuickStarter.class);

    private static Map<String, Class> clazzMap;

    private static Map<String, String> urlMap;

    private static void init(){
        clazzMap = new LinkedHashMap<>();
        clazzMap.put("1", OschinaBlog.class);
        clazzMap.put("2", IteyeBlog.class);
        clazzMap.put("3", News163.class);
        urlMap = new LinkedHashMap<>();
        urlMap.put("1", "http://my.oschina.net/flashsword/blog");
        urlMap.put("2", "http://flashsword20.iteye.com/");
        urlMap.put("3", "http://news.163.com/");
    }

    public static void main(String[] args) {
        init();
        String key = null;
        key = readKey(key);
        logger.info("The demo started and will last 20 seconds...");
        //Start spider
        OOSpider.create(Site.me(), clazzMap.get(key)).addUrl(urlMap.get(key)).addPipeline(new MultiPagePipeline()).addPipeline(new ConsolePipeline()).runAsync();

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("The demo stopped!");
        logger.info("To more usage, try to customize your own Spider!");
        System.exit(0);
    }

    private static String readKey(String key) {
        Scanner stdin = new Scanner(System.in);
        logger.info("Choose a Spider demo:");
        for (Map.Entry<String, Class> classEntry : clazzMap.entrySet()) {
            logger.info("{}\t{}\t{}", classEntry.getKey(), classEntry.getValue(), urlMap.get(classEntry.getKey()));
        }
        while (key == null) {
            key = stdin.nextLine();
            if (clazzMap.get(key) == null) {
            	logger.info("Invalid choice!");
                key = null;
            }
        }
        return key;
    }
}
