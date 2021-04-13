package us.codecraft.webmagic.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.*;
import us.codecraft.webmagic.handler.CompositePageProcessor;
import us.codecraft.webmagic.handler.CompositePipeline;
import us.codecraft.webmagic.handler.PatternProcessor;
import us.codecraft.webmagic.handler.RequestMatcher;

/**
 * Created with IntelliJ IDEA.
 * User: Sebastian MA
 * Date: April 04, 2014
 * Time: 21:23
 */
public class PatternProcessorExample {

    private static Logger logger = LoggerFactory.getLogger(PatternProcessorExample.class);

    public static void main(String... args) {

        // define a patternProcessor which handles only "http://item.jd.com/.*"
        PatternProcessor githubRepoProcessor = new PatternProcessor("https://github\\.com/[\\w\\-]+/[\\w\\-]+") {

            @Override
            public RequestMatcher.MatchOther processPage(Page page) {
                page.putField("reponame", page.getHtml().xpath("//h1[@class='entry-title public']/strong/a/text()").toString());
                return RequestMatcher.MatchOther.YES;
            }

            @Override
            public RequestMatcher.MatchOther processResult(ResultItems resultItems, Task task) {
                logger.info("Extracting from repo {}", resultItems.getRequest());
                ResultItems toLog = resultItems.get("reponame");
                logger.info("Repo name: {}", toLog);
                return RequestMatcher.MatchOther.YES;
            }
        };

        PatternProcessor githubUserProcessor = new PatternProcessor("https://github\\.com/[\\w\\-]+") {

            @Override
            public RequestMatcher.MatchOther processPage(Page page) {
                logger.info("Extracting from {}", page.getUrl());
                page.addTargetRequests(page.getHtml().links().regex("https://github\\.com/[\\w\\-]+/[\\w\\-]+").all());
                page.addTargetRequests(page.getHtml().links().regex("https://github\\.com/[\\w\\-]+").all());
                page.putField("username", page.getHtml().xpath("//span[@class='vcard-fullname']/text()").toString());
                return RequestMatcher.MatchOther.YES;
            }

            @Override
            public RequestMatcher.MatchOther processResult(ResultItems resultItems, Task task) {
            	ResultItems toLog = resultItems.get("username");
                logger.info("User name: {}", toLog);
                return RequestMatcher.MatchOther.YES;
            }
        };

        CompositePageProcessor pageProcessor = new CompositePageProcessor(Site.me().setDomain("github.com").setRetryTimes(3));
        CompositePipeline pipeline = new CompositePipeline();

        pageProcessor.setSubPageProcessors(githubRepoProcessor, githubUserProcessor);
        pipeline.setSubPipeline(githubRepoProcessor, githubUserProcessor);

        Spider.create(pageProcessor).addUrl("https://github.com/code4craft").thread(5).addPipeline(pipeline).runAsync();
    }

}
