package us.codecraft.webmagic;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.processor.SimplePageProcessor;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author code4crafter@gmail.com
 */
public class SpiderTest {

    @Ignore("long time")
    @Test
    public void testStartAndStop() throws InterruptedException {
        Spider spider = Spider.create(new SimplePageProcessor( "http://www.oschina.net/*")).addPipeline(new Pipeline() {
            @Override
            public void process(ResultItems resultItems, Task task) {
                System.out.println(1);
            }
        }).thread(1).addUrl("http://www.oschina.net/");
        spider.start();
        Thread.sleep(10000);
        spider.stop();
        Thread.sleep(10000);
        spider.start();
        Thread.sleep(10000);
    }

    @Ignore("long time")
    @Test
    public void testWaitAndNotify() throws InterruptedException {
        for (int i = 0; i < 10000; i++) {
            System.out.println("round " + i);
            testRound();
        }
    }

    private void testRound() {
        Spider spider = Spider.create(new PageProcessor() {

            private AtomicInteger count = new AtomicInteger();

            @Override
            public void process(Page page) {
                page.setSkip(true);
            }

            @Override
            public Site getSite() {
                return Site.me().setSleepTime(0);
            }
        }).setDownloader(new Downloader() {
            @Override
            public Page download(Request request, Task task) {
                return new Page().setRawText("");
            }

            @Override
            public void setThread(int threadNum) {

            }
        }).setScheduler(new Scheduler() {

            private AtomicInteger count = new AtomicInteger();

            private Random random = new Random();

            @Override
            public void push(Request request, Task task) {

            }

            @Override
            public synchronized Request poll(Task task) {
                if (count.incrementAndGet() > 1000) {
                    return null;
                }
                if (random.nextInt(100)>90){
                    return null;
                }
                return new Request("test");
            }
        }).thread(10);
        spider.run();
    }
    
    @Test
    public void tSetStartsUrls() {
    	Spider spider = new Spider(new SimplePageProcessor(""));
    	List<String> urls =  new ArrayList<String>();
    	urls.add("http://url1.fr");
    	urls.add("http://url2.fr");
    	
    	spider.startUrls(urls);
    	
    	assertEquals(urls.get(0), spider.startRequests.get(0).getUrl());
    	assertEquals(urls.get(1), spider.startRequests.get(1).getUrl());
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldNotBeRunning() {
    	Spider spider = new Spider(new SimplePageProcessor("http://my.oschina.net/*blog/*"));
    	spider.stat.set(spider.STAT_RUNNING);
    	
    	List<String> urls =  new ArrayList<String>();
    	urls.add("http://url1.fr");
    	urls.add("http://url2.fr");
    	
    	spider.startUrls(urls);
    }
    
    @Test
    public void testSetScheduleWithNoOldScheduler() {
    	Spider spider = new Spider(new SimplePageProcessor(""));
    	spider.setScheduler(null);
    	
    	Scheduler oldScheduler = spider.getScheduler();
    	assertEquals(null, oldScheduler);
    	
    	Scheduler newScheduler = new QueueScheduler();
    	Spider spiderTest = spider.setScheduler(newScheduler);
    	assertSame("setSchduler don't return the same Scheduler when there isn't an old sheduler.", newScheduler, spiderTest.getScheduler());
    }
    
    @Test
    public void testSetScheduleWithAnOldScheduler() {
    	Spider spider = new Spider(new SimplePageProcessor("http://my.oschina.net/*blog/*"));
    	spider.getScheduler().push(new Request("http://url1.fr"), spider);
    	spider.getScheduler().push(new Request("http://url2.fr"), spider);
    	
    	QueueScheduler newScheduler = new QueueScheduler();
    	
    	spider.setScheduler(newScheduler);
    	
    	assertEquals(2, newScheduler.getTotalRequestsCount(spider));
    	assertEquals(new Request("http://url1.fr"), newScheduler.poll(spider));
    	assertEquals(new Request("http://url2.fr"), newScheduler.poll(spider));
    }
    
    @Test
    public void testSetScheduleWithANotEmptyNewScheduler() {
    	Spider spider = new Spider(new SimplePageProcessor("http://my.oschina.net/*blog/*"));
    	spider.getScheduler().push(new Request("http://url1.fr"), spider);
    	spider.getScheduler().push(new Request("http://url2.fr"), spider);
    	
    	QueueScheduler newScheduler = new QueueScheduler();
    	newScheduler.push(new Request("http://url3.fr"), spider);
    	
    	spider.setScheduler(newScheduler);
    	
    	assertEquals(3, newScheduler.getTotalRequestsCount(spider));
    	//The URL that is already in the new Scheduler still conserve it's position.
    	assertEquals(new Request("http://url3.fr"), newScheduler.poll(spider)); 
    	assertEquals(new Request("http://url1.fr"), newScheduler.poll(spider));
    	assertEquals(new Request("http://url2.fr"), newScheduler.poll(spider));
    }
    
    
}
