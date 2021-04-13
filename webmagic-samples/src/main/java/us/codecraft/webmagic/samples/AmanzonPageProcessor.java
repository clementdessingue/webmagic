package us.codecraft.webmagic.samples;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author code4crafer@gmail.com
 */
public class AmanzonPageProcessor implements PageProcessor{

	private Logger logger = LoggerFactory.getLogger(getClass());

	public void process(Page page) {

		Html html = page.getHtml();
		List<String> questionList =  html.xpath("//table[@class='tgCustomerCommunityCenterColumn']//div[@class='content']//table[@class='dataGrid']//tr").all();

		if(questionList != null && questionList.size() > 1)
		{
			//i=0是列名称，所以i从1开始
			for( int i = 1 ; i < questionList.size(); i++)
			{
				logger.info(questionList.get(i));
				Html tempHtml =  Html.create("<table>"+questionList.get(i)+"</table>");
				String comment = tempHtml.xpath("//td[@class='title']//a/text()").toString();
				logger.info(comment);
				String answerNum =  tempHtml.xpath("//td[@class='num']/text()").toString();
				logger.info(answerNum);
				String createTime = tempHtml.xpath("//td[3]/text()").toString();
				logger.info(createTime);
			}
		}

	}

	@Override
	public Site getSite() {
		return Site.me();
	}

	public static void main(String[] args) {
		Spider.create(new AmanzonPageProcessor()).test("http://www.amazon.de/forum/Fx27CUFD8S7LJ5D");
	}
}
