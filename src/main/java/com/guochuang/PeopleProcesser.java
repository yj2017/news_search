package com.guochuang;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;

/**
 * 人民网
 * Created by yj on 2016/11/7.
 */
public class PeopleProcesser extends SearchProcessor {

    public PeopleProcesser(String... keywords) {
        super(Site.me()
                        .setDomain("www.people.com.cn")
                        .setRetrySleepTime(1)
                        .setCycleRetryTimes(3)
                , keywords);
    }

    @Override
    public void process(Page page) {
        Selectable box_con = page.getHtml().$(".box_con");
        if (box_con.match()) {
            String content = box_con.toString();
            String title = page.getHtml().$(".text_title h1", "text").toString();
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
                article.setPublishTime(page.getHtml().$(".text_title .fl", "text").toString().split(" ")[0]);
                article.setContent(content);
                article.setSite("人民网");
//                System.err.println(article);
                page.putField("article", article);
            } else {
                page.setSkip(true);
            }
        } else {
            page.setSkip(true);
        }
        List<String> links = page.getHtml().links().regex("(http://(" +
                "news|politics|world|finance|tw|military|opinion|leaders|renshi|theory|legal|society|edu|kpzg|sports" +
                "|culture|art|house|auto|travel|health|scitech|tv|fangtan|yuqing|hm|media|ent|book|gongyi|money|energy" +
                "|ccnews|env|it|tc|homea|shipin|lady|game|hongmu|caipiao|dangshi|fanfu|pic|t|jiaju|dengshi|capital|rmfp" +
                "|bj|tj|he|sx|nm|ln|jl|hlj|sh|js|zj|ah|fj|jx|sd|henan|hb|hn|gd|gx|hi|cq|sc|gz|yn|xz|sn|gs|qh|nx|xj|sz" +
                ")\\.people\\.com\\.cn/[/0-9A-Za-z\\.\\-_]*)").all();
        page.addTargetRequests(links);
    }

}