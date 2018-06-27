package com.text.summarization;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CosineSimilarity {
    private FeatureExtraction featureExtraction;

    public CosineSimilarity(FeatureExtraction openNlpTools) {
        this.featureExtraction = openNlpTools;
    }

    public CosineSimilarity() {}

    public double getSimilarityScore(String s1, String s2) {
        return getSimilarityScore(featureExtraction.getTokens(s1), featureExtraction.getTokens(s2));
    }

    public double getSimilarityScore(String[] vector1, String[] vector2) {
        Map<String, Integer> leftWordCountMap = new HashMap<>();
        Map<String, Integer> rightWordCountMap = new HashMap<>();
        Set<String> uniqueSet = new HashSet<>();
        for (String leftWord : vector1) {
            leftWord = leftWord.toLowerCase().trim();
            leftWordCountMap.put(leftWord, leftWordCountMap.getOrDefault(leftWord, 0) + 1);
            uniqueSet.add(leftWord);
        }
        for (String rightWord : vector2) {
            rightWord = rightWord.toLowerCase().trim();
            rightWordCountMap.put(rightWord, rightWordCountMap.getOrDefault(rightWord, 0) + 1);
            uniqueSet.add(rightWord);
        }
        int[] leftVector = new int[uniqueSet.size()];
        int[] rightVector = new int[uniqueSet.size()];
        int index = 0;
        for (String uniqueWord : uniqueSet) {
            leftVector[index] = leftWordCountMap.getOrDefault(uniqueWord, 0);
            rightVector[index] = rightWordCountMap.getOrDefault(uniqueWord, 0);
            index++;
        }

        return cosineVectorSimilarity(leftVector, rightVector);
    }

    private double cosineVectorSimilarity(int[] leftVector, int[] rightVector) {
        if (leftVector.length != rightVector.length) {
            return 1;
        }
        double dotProduct = 0;
        double leftNorm = 0;
        double rightNorm = 0;
        for (int i = 0; i < leftVector.length; i++) {
            dotProduct += leftVector[i] * rightVector[i];
            leftNorm += leftVector[i] * leftVector[i];
            rightNorm += rightVector[i] * rightVector[i];
        }
        double result = dotProduct
                / (Math.sqrt(leftNorm) * Math.sqrt(rightNorm));

        return result;
    }
}