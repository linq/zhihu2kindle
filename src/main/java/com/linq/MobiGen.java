package com.linq;

import com.linq.meta.Question;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * mobi生成
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class MobiGen {
    private static final Logger logger = LoggerFactory.getLogger(MobiGen.class);

    public String generate(String url, AppConfig appConfig) throws IOException {
        Document document = download(url, appConfig);
        Question question = parse(document);
        downloadImage(question.getImgs(), appConfig);
        rearrange(question);
        String html = genHtml(question);
        return toMobi(html, question.getTitle(), appConfig);
    }

    private void rearrange(Question question) {
        List<Element> answers = question.getAnswers();
        for (Element answer : answers) {
            Elements imgs = answer.select("img");
            for (Element img : imgs) {
                String src = img.attr("data-actualsrc");
                src = FilenameUtils.getName(src);
                img.attr("src", String.format("img/%s", src));
            }
        }
    }

    private void downloadImage(List<String> imgs, AppConfig appConfig) {
        int threads = imgs.size() > 10 ? 10 : 5;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        for (String img : imgs) {
            executor.submit(new ImageTask(img, appConfig.getLocalPath() + "/img"));
        }
        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.error("", e);
        }
    }

    private String toMobi(String html, String title, AppConfig appConfig) throws IOException {
        String name = String.format("%s/%s.html", appConfig.getLocalPath(), title);
        FileUtils.writeStringToFile(new File(name), html, "gbk");
        String kindlegen = appConfig.getKindlegen();
        String cmd = String.format("\"%s\" \"%s\" -o \"%s\".mobi", kindlegen, name, title);
        logger.info("execute shell: {}", cmd);
        Process process = Runtime.getRuntime().exec(cmd);
        IOUtils.copy(process.getInputStream(), System.out);
        return String.format("%s/%s.mobi", appConfig.getLocalPath(), title);
    }

    private String genHtml(Question question) {
        StringBuilder html = new StringBuilder(1024);
        html.append(String.format("<h1>%s</h1>", question.getTitle()));
        int size = question.getAnswers().size();
        for (int i = 0; i < size; i++) {
            Element element = question.getAnswers().get(i);
            html.append(element.html());
            if (i < (size - 1)) {
                html.append("<p></p><p></p>");
                html.append("<hr>");
                html.append("<p></p><p></p>");
            }
        }
        Whitelist whitelist = Whitelist.basic()
                .addTags("img")
                .addAttributes("img", "align", "alt", "height", "src", "title", "width");
        whitelist.addTags("h1", "hr");
        String clean = Jsoup.clean(html.toString(), whitelist);
        return String.format("<body>%s</body>", clean);
    }

    private Document download(String url, AppConfig appConfig) throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:16.0) Gecko/20100101 Firefox/16.0");
        httpParams.setParameter("Host", "www.zhihu.com");
        client.setParams(httpParams);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", appConfig.getCookie());
        HttpResponse httpResponse = client.execute(httpGet);
        String html = EntityUtils.toString(httpResponse.getEntity());
        return Jsoup.parse(html);
    }

    private Question parse(Document document) {
        document.select("noscript").remove();
        Question question = new Question();
        Element title = document.select("title").first();
        question.setTitle(StringUtils.trimToEmpty(title.text()));
        Elements elements = document.select("div[data-action=/answer/content]");
        question.setAnswers(elements);
        ArrayList<String> imgs = new ArrayList<String>();
        for (Element element : elements) {
            Elements imgElements = element.select("img");
            for (Element img : imgElements) {
                String src = img.attr("data-actualsrc");
                imgs.add(src);
            }
        }
        question.setImgs(imgs);
        return question;
    }
}
