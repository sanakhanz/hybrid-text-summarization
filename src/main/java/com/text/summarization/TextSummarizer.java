package com.text.summarization;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class TextSummarizer {

    private ExtractSummary extractSummary;
    private FeatureExtraction featureExtraction;
    private HashMap<Integer, String> sentenceMap;
    private double summaryPercentage = 33;

    public TextSummarizer() throws IOException {
        featureExtraction = new FeatureExtraction();
        extractSummary = new ExtractSummary(featureExtraction);
    }


    private String getSummary(String text) {
        String[] sentences = featureExtraction.getSentences(text);
        loadSentenceMap(sentences);
        Map<Integer, Double> extractSummaryMap = extractSummary.extractSummary(sentences, summaryPercentage);
        Set<Integer> positions = extractSummaryMap.keySet();

        System.out.println("\ninput Text");
        for (String sen : sentences) {
            System.out.println(sen);
        }

        System.out.println("\n\nExtract Summary");
        for (Integer position : positions) {
            System.out.println(sentences[position]);
        }
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
        //String text = "";
        File f = new File("resources/test.txt");
        String text = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
        String summary = textSummarizer.getSummary(text);
        System.out.println(summary);
    }

}
