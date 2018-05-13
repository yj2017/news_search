package com.guochuang;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by yj on 2016/11/8.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("关键词：");
        for (String arg : args) {
            System.out.print(arg + "\t");
        }
        System.out.println();
        Configuration configuration = new Configuration()
                .setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver")
                .setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/news_search?characterEncoding=utf8&useSSL=true&serverTimezone=GMT%2b8")
                .setProperty("hibernate.connection.username", "root")
                .setProperty("hibernate.connection.password", "273841")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .addAnnotatedClass(Article.class);
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session = sessionFactory.openSession();
        session.setHibernateFlushMode(FlushMode.COMMIT);
        Pipeline articlePipeLine = new ArticlePipeLine(session);

        Spider peopleSpider = Spider.create(new PeopleProcesser(args)).addUrl("http://www.people.com.cn/")
//                .addPipeline(new ConsolePipeline())
//                .addPipeline(new JsonFilePipeline("D:/webmagic/people"))
                .addPipeline(articlePipeLine)
                .thread(500);
        SpiderMonitor.instance().register(peopleSpider);
        peopleSpider.start();

        Spider sinaSpider = Spider.create(new SinaProcessor(args)).addUrl("http://www.sina.com.cn")
                .addPipeline(articlePipeLine)
                .thread(500);
        SpiderMonitor.instance().register(sinaSpider);
        sinaSpider.start();

        /**
         * 等待其它线程结束
         */
        Thread.currentThread().join();
        System.err.println("closed...");
        session.close();
        sessionFactory.close();
    }
}
