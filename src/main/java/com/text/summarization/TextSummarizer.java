package com.text.summarization;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TextSummarizer {

    private ExtractSummary extractSummary;
    private FeatureExtraction featureExtraction;
    private HashMap<Integer, String> sentenceMap;

    public TextSummarizer() throws IOException {
        featureExtraction = new FeatureExtraction();
        extractSummary = new ExtractSummary(featureExtraction);
    }


    private String getSummary(String text) {
        String[] sentences = featureExtraction.getSentences(text);
        loadSentenceMap(sentences);
        Map<Integer, Double> extractSummaryMap = extractSummary.extractSummary(sentences);
        return "";
    }

    private void loadSentenceMap(String[] sentences) {
        sentenceMap = new LinkedHashMap<>();
        for (int i = 0; i < sentences.length; i++) {
            sentenceMap.put(i, sentences[i]);
        }
    }

    public static void main(String[] arg) throws IOException {
        TextSummarizer textSummarizer = new TextSummarizer();
        String text = "";
        String summary = textSummarizer.getSummary(text);
        System.out.println(summary);
    }

}
