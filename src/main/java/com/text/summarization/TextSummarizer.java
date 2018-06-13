package com.text.summarization;

import java.io.FileNotFoundException;

public class TextSummarizer {
    private FeatureExtraction featureExtraction;

    public TextSummarizer() throws FileNotFoundException {
        featureExtraction = new FeatureExtraction();
    }


    private String getSummary(String text) {
        return null;
    }

    public static void main(String[] arg) throws FileNotFoundException {
        TextSummarizer textSummarizer = new TextSummarizer();
        String text = "";
        String summary = textSummarizer.getSummary(text);
        System.out.println(summary);
    }

}
