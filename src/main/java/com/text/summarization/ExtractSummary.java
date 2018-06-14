package com.text.summarization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExtractSummary {

    private FeatureExtraction featureExtraction;
    private WeightCalculator weightCalculator;
    private List<String> docVector;

    public ExtractSummary(OpenNlpTools openNlpTools) throws IOException {
        featureExtraction = new FeatureExtraction();
        weightCalculator = new WeightCalculator();
    }

    public void extractSummary(String[] sentences) {
        docVector = new ArrayList<>();
        HashMap<Integer, Double> weightMap = getVectorArray(sentences);

    }

    private HashMap<Integer, Double> getVectorArray(String[] sentences) {
        HashMap<Integer, Double> weightMap = new HashMap<>();
        List<List<String>> vectorArray = new ArrayList<>();
        int sentencePosition = 0;
        for (String sentence : sentences) {
            String[] tokens = featureExtraction.getTokens(sentence);
            List<String> biGrams = featureExtraction.getBiGrams(tokens);
            List<String> removeStopWords = featureExtraction.removeStopWords(tokens);
            String[] stems = featureExtraction.getStems(removeStopWords);
            List<String> vector = getVector(removeStopWords, stems, biGrams);
            weightMap.put(sentencePosition, getVectorWeight(tokens, sentences.length, sentencePosition + 1));
            sentencePosition++;
            vectorArray.add(vector);
        }

        //Calculate Term Frequency
        weightCalculator.calculateTermFrequency(docVector);

        //Add Tf-Idf weights
        sentencePosition = 0;
        for (List<String> vector : vectorArray) {
            double vectorWeight = getVectorWeight(vector);
            weightMap.put(sentencePosition, weightMap.get(sentencePosition) + vectorWeight);
            sentencePosition++;
        }
        return weightMap;
    }

    private double getVectorWeight(String[] tokens, int totalSentences, int sentencePosition) {
        String[] posTags = featureExtraction.getPosTags(tokens);
        double partOfSpeechWeight = featureExtraction.getPartOfSpeechWeight(posTags);
        double upperCaseWeight = featureExtraction.getUpperCaseWeight(tokens);
        double sentenceLengthWeight = featureExtraction.getSentenceLengthWeight(tokens);
        double sentencePositionWeight = featureExtraction.getSentencePositionWeight(totalSentences, sentencePosition);
        double finalWeight = partOfSpeechWeight + upperCaseWeight + sentenceLengthWeight + sentencePositionWeight;
        return finalWeight;
    }

    private List<String> getVector(List<String> removeStopWords, String[] stems, List<String> biGrams) {
        List<String> vector = new ArrayList<>();
        for (String s : removeStopWords) {
            vector.add(s.toLowerCase().trim());
            docVector.add(s.toLowerCase().trim());
        }
        for (String s : stems) {
            vector.add(s.toLowerCase().trim());
            docVector.add(s.toLowerCase().trim());
        }
        for (String s : biGrams) {
            vector.add(s.toLowerCase().trim());
            docVector.add(s.toLowerCase().trim());
        }
        return vector;
    }

    public double getVectorWeight(List<String> vector) {
        double vectorWeight = 0.0;
        for (String vec : vector) {
            vectorWeight = vectorWeight + weightCalculator.getTfIdf(vec);
        }
        return vectorWeight / (double) vector.size();
    }
}
