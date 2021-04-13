package us.codecraft.webmagic.utils;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.selector.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Tools for annotation converting. <br>
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.2.1
 */
public class ExtractorUtils {
	
	private ExtractorUtils() {}

    public static Selector getSelector(ExtractBy extractBy) {
        String value = extractBy.value();
        Selector selector;
        switch (extractBy.type()) {
            case CSS:
                selector = new CssSelector(value);
                break;
            case REGEX:
                selector = new RegexSelector(value);
                break;
            case XPATH:
                selector = new XpathSelector(value);
                break;
            case JSONPATH:
                selector = new JsonPathSelector(value);
                break;
            default:
                selector = new XpathSelector(value);
        }
        return selector;
    }

    public static List<Selector> getSelectors(ExtractBy[] extractBies) {
        List<Selector> selectors = new ArrayList<Selector>();
        if (extractBies == null) {
            return selectors;
        }
        for (ExtractBy extractBy : extractBies) {
            selectors.add(getSelector(extractBy));
        }
        return selectors;
    }
}
