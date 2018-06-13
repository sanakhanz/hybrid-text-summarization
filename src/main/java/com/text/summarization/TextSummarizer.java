package com.text.summarization;

import java.io.IOException;

public class TextSummarizer {

    public TextSummarizer() throws IOException {

    }


    private String getSummary(String text) {
        return null;
    }

    public static void main(String[] arg) throws IOException {
        TextSummarizer textSummarizer = new TextSummarizer();
        String text = "";
        String summary = textSummarizer.getSummary(text);
        System.out.println(summary);
    }

}
