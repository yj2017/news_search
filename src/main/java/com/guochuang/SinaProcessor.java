package com.guochuang;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * Created by yj on 2016/11/8.
 */
public class SinaProcessor extends SearchProcessor {

    public SinaProcessor(String... keywords) {
        super(Site.me()
                        .setDomain("www.sina.com.cn")
                        .setRetrySleepTime(1)
                        .setCycleRetryTimes(3)
                , keywords);
    }

    /**
     * process the page, extract urls to fetch, extract the data and store
     *
     * @param page page
     */
    @Override
    public void process(Page page) {
        Selectable artibody = page.getHtml().$("#artibody");
        Selectable artibodyTitle = page.getHtml().$("#artibodyTitle", "text");
        Selectable pub_date = page.getHtml().$("#pub_date", "text");

        if (artibody.match() && artibodyTitle.match() && pub_date.match()) {
            String content = artibody.toString();
            String title = artibodyTitle.toString();
            String publishTime = pub_date.toString().trim();

            String all = title + content;

            boolean contains = false;
            for (String keyword : keywords) {
                if (all.contains(keyword)) {
                    contains = true;
                    break;
                }
            }
            if (contains) {
                Article article = new Article();
                article.setUrl(page.getRequest().getUrl());
                article.setTitle(title);
                article.setPublishTime(publishTime);
                article.setContent(content);
                article.setSite("新浪网");
//                System.err.println(article);
                page.putField("article", article);

            } else {
                page.setSkip(true);
            }
        } else {
            page.setSkip(true);
        }
        List<String> links = page.getHtml().links().regex("(http://(" +
                "news|mil\\.news|finance|tech|mobile|zhongce|sports|ent|astro|auto|dealer\\.auto" +
                "|data\\.auto|zhuanlan|video|vr|house|jiaju|collection|fashion|eladies|health" +
                "|baby|edu|gongyi|fo|photo|book|sifa|city|travel|sky\\.news|lottery|golf|games" +
                "|show|gov|chexian" +
                ")\\.sina\\.com\\.cn/[/0-9A-Za-z\\.\\-_]*)").all();
        page.addTargetRequests(links);

    }


}
