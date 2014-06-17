package com.linq.meta;

import org.jsoup.nodes.Element;

import java.util.List;

/**
 * 知乎问题
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class Question {
    private String title;
    private List<Element> answers;
    private List<String> imgs;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Element> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Element> answers) {
        this.answers = answers;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
