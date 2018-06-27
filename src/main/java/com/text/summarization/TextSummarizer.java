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
    private HashMap<Integer, String[]> sentenceTokenMap;
    private double summaryPercentage = 33;
    private AbstractSummary abstractSummary;

    public TextSummarizer() throws IOException {
        featureExtraction = new FeatureExtraction();
        extractSummary = new ExtractSummary(featureExtraction);
        abstractSummary = new AbstractSummary(featureExtraction);
    }


    public String getSummary(String text) {
        String[] sentences = featureExtraction.getSentences(text);
        loadSentenceMap(sentences);
        loadSentenceTokenMap(sentences);
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

        Set<String> abstractSummary = this.abstractSummary.getAbstractSummary(extractSummaryMap, sentenceTokenMap, sentenceMap);
        System.out.println("\n\nAbstract Summary");
        for (String s : abstractSummary) {
            System.out.println(s);
        }
        return toString(abstractSummary);
    }

    private String toString(Set<String> abstractSummary) {
        StringBuilder sb = new StringBuilder();
        for (String s : abstractSummary) {
            sb.append(s).append(". ");
        }
        return sb.toString().trim();
    }

    private void loadSentenceMap(String[] sentences) {
        sentenceMap = new LinkedHashMap<>();
        for (int i = 0; i < sentences.length; i++) {
            sentenceMap.put(i, sentences[i]);
        }
    }

    private void loadSentenceTokenMap(String[] sentences) {
        sentenceTokenMap = new LinkedHashMap<>();
        for (int i = 0; i < sentences.length; i++) {
            sentenceTokenMap.put(i, featureExtraction.getTokens(sentences[i]));
        }
    }

    public static void main(String[] arg) throws IOException {
        TextSummarizer textSummarizer = new TextSummarizer();
        File f = new File("/home/shabir/Documents/project/sana/hybrid-text-summarization/resources/test.txt");
        String text = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
        textSummarizer.getSummary(text);
    }

}
