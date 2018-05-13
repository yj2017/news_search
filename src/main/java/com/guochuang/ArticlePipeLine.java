package com.guochuang;

import org.hibernate.Session;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by yj on 2016/11/9.
 */
public class ArticlePipeLine implements Pipeline, Closeable {
    private Session session;

    public ArticlePipeLine(Session session) {
        this.session = session;
    }

    /**
     * Process extracted results.
     *
     * @param resultItems resultItems
     * @param task        task
     */
    @Override
    public void process(ResultItems resultItems, Task task) {

        Article article = resultItems.get("article");

        synchronized (session) {

            session.getTransaction().begin();
            session.save(article);
            session.getTransaction().commit();

            System.out.println();
            System.out.println(article);
            System.out.println();
        }

    }


    @Override
    public void close() throws IOException {
        session.close();
    }
}
