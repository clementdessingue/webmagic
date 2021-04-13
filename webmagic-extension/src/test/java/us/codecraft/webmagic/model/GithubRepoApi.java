package us.codecraft.webmagic.model;

import us.codecraft.webmagic.model.annotation.ExtractBy;

/**
 * @author code4crafter@gmail.com
 *         Date: 2017/6/3
 *         Time: 下午9:07
 */
public class GithubRepoApi {

    @ExtractBy(type = ExtractBy.Type.JSONPATH, value = "$.name",source = ExtractBy.Source.RAWTEXT)
    private String name;

    public String getName() {
        return name;
    }
}
