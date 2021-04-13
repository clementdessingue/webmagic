package us.codecraft.webmagic.example;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.utils.Experimental;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author code4crafter@gmail.com
 * @since 0.4.1
 */
@Experimental
public class AppStore {
	

	private static Logger logger = LoggerFactory.getLogger(AppStore.class);

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$..trackName")
    private String trackName;

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$..description")
    private String description;

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$..userRatingCount")
    private int userRatingCount;

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$..screenshotUrls")
    private List<String> screenshotUrls;

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$..supportedDevices")
    private List<String> supportedDevices;

    public static void main(String[] args) {
        AppStore appStore = OOSpider.create(Site.me(), AppStore.class).<AppStore>get("http://itunes.apple.com/lookup?id=653350791&country=cn&entity=software");
        logger.info(appStore.trackName);
        logger.info(appStore.description);
        logger.info("{}", appStore.userRatingCount);
        logger.info("{}", appStore.screenshotUrls);
        logger.info("{}", appStore.supportedDevices);
    }
}
