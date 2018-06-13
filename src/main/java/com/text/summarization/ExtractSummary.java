package com.text.summarization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExtractSummary {
    private OpenNlpTools openNlpTools;
    private FeatureExtraction featureExtraction;
    private WeightCalculator weightCalculator;
    private List<String> docVector = new ArrayList<>();

    public ExtractSummary(OpenNlpTools openNlpTools) throws IOException {
        this.openNlpTools = openNlpTools;
        featureExtraction = new FeatureExtraction();
        weightCalculator = new WeightCalculator();
    }

    public void extractSummary(String[] sentences) {
        List<List<String>> vectorArray = getVectorArray(sentences);
        weightCalculator.calculateTermFrequency(docVector);
        for (List<String> sentence : vectorArray) {

        }
    }

    List<List<String>> getVectorArray(String[] sentences) {
        List<List<String>> vectorArray = new ArrayList<>();
        for (String sentence : sentences) {
            String[] tokens = openNlpTools.getTokens(sentence);
            List<String> biGrams = featureExtraction.biGrams(tokens);
            List<String> removeStopWords = featureExtraction.removeStopWords(tokens);
            String[] stems = openNlpTools.getStems(removeStopWords);
            List<String> vector = getVector(removeStopWords, stems, biGrams);
            vectorArray.add(vector);
        }
        return vectorArray;
    }

    private List<String> getVector(List<String> removeStopWords, String[] stems, List<String> biGrams) {
        List<String> vector = new ArrayList<>();
        for (String s : removeStopWords) {
            vector.add(s);
            docVector.add(s);
        }
        for (String s : stems) {
            vector.add(s);
            docVector.add(s);
        }
        for (String s : biGrams) {
            vector.add(s);
            docVector.add(s);
        }
        return vector;
    }
}
