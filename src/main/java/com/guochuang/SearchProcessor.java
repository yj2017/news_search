package com.guochuang;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * Created by yj on 2016/11/8.
 */
public abstract class SearchProcessor implements PageProcessor {
    protected String[] keywords;
    protected Site site;

    public SearchProcessor(Site site, String... keywords) {
        this.site = site;
        this.keywords = keywords;
    }

    /**
     * get the site settings
     *
     * @return site
     * @see Site
     */
    @Override
    public Site getSite() {
        return site;
    }
}
