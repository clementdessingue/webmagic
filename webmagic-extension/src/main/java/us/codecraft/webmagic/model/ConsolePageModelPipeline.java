package us.codecraft.webmagic.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

/**
 * Print page model in console.<br>
 * Usually used in test.<br>
 * @author code4crafter@gmail.com <br>
 * @since 0.2.0
 */
public class ConsolePageModelPipeline implements PageModelPipeline {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
    @Override
    public void process(Object o, Task task) {
    	String toLog = ToStringBuilder.reflectionToString(o);
        logger.info(toLog);
    }
}
