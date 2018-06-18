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

    public CosineSimilarity() {

    }

    public double getSimilarityScore(String s1, String s2) {
        return getSimilarityScore(featureExtraction.getTokens(s1), featureExtraction.getTokens(s2));
    }

    public double getSimilarityScore(String[] vector1, String[] vector2) {
        Map<String, Integer> leftWordCountMap = new HashMap<>();
        Map<String, Integer> rightWordCountMap = new HashMap<>();
        Set<String> uniqueSet = new HashSet<>();
        Integer temp = null;
        for (String leftWord : vector1) {
            temp = leftWordCountMap.get(leftWord);
            if (temp == null) {
                leftWordCountMap.put(leftWord, 1);
                uniqueSet.add(leftWord);
            } else {
                leftWordCountMap.put(leftWord, temp + 1);
            }
        }
        for (String rightWord : vector2) {
            temp = rightWordCountMap.get(rightWord);
            if (temp == null) {
                rightWordCountMap.put(rightWord, 1);
                uniqueSet.add(rightWord);
            } else {
                rightWordCountMap.put(rightWord, temp + 1);
            }
        }
        int[] leftVector = new int[uniqueSet.size()];
        int[] rightVector = new int[uniqueSet.size()];
        int index = 0;
        Integer tempCount = 0;
        for (String uniqueWord : uniqueSet) {
            tempCount = leftWordCountMap.get(uniqueWord);
            leftVector[index] = tempCount == null ? 0 : tempCount;
            tempCount = rightWordCountMap.get(uniqueWord);
            rightVector[index] = tempCount == null ? 0 : tempCount;
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