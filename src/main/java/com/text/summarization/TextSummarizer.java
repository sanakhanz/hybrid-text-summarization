package com.text.summarization;

public class TextSummarizer {
    private FeatureExtraction featureExtraction;

    public TextSummarizer() {
        featureExtraction = new FeatureExtraction();
    }


    private String getSummary(String text) {
        return null;
    }

    public static void main(String[] arg)  {
        TextSummarizer textSummarizer = new TextSummarizer();
        String text = "";
        String summary = textSummarizer.getSummary(text);
        System.out.println(summary);
    }

}
