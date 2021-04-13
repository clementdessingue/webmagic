package us.codecraft.webmagic.model.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.model.AfterExtractor;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.TargetUrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yihua.huang@dianping.com <br>
 *         Date: 13-8-13 <br>
 *         Time: 上午10:13 <br>
 */
@TargetUrl("http://*.alpha.dp/*")
public class DianpingFtlDataScanner implements AfterExtractor {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExtractBy(value = "(DP\\.data\\(\\{.*\\}\\));", type = ExtractBy.Type.REGEX, notNull = true, multi = true)
	private List<String> data;

	public static void main(String[] args) {
		OOSpider.create(Site.me().setSleepTime(0), DianpingFtlDataScanner.class)
				.thread(5).run();
	}

	@Override
	public void afterProcess(Page page) {
		if (data.size() > 1) {
			String toLog = page.getUrl().toString();
			logger.warn(toLog);
		}
		if (! data.isEmpty() && data.get(0).length() > 100) {
			String toLog = page.getUrl().toString();
			logger.warn(toLog);
		}
	}
}
