package com.text.summarization;

import java.io.IOException;
import java.util.*;

public class ExtractSummary {

    private FeatureExtraction featureExtraction;
    private WeightCalculator weightCalculator;
    private List<String> docVector;
    private CosineSimilarity similarity;

    public ExtractSummary(FeatureExtraction featureExtraction) throws IOException {
        this.featureExtraction = featureExtraction;
        weightCalculator = new WeightCalculator();
        similarity = new CosineSimilarity(featureExtraction);
    }

    public Map extractSummary(String[] sentences, double summaryPercentage) {
        docVector = new ArrayList<>();
        HashMap<Integer, Double> weightMap = getVectorArray(sentences);
        System.out.println("SentencePosition\tWeight");
        for (int position : weightMap.keySet()) {
            System.out.println(position + "\t" + weightMap.get(position));
        }

        //Use Cosine Similarity
        Map map = removeSimilarSentences(weightMap, sentences);

        //get Top n% sentences
        Map<Integer, Double> topN = getTopN(map, summaryPercentage);

        System.out.println("\nTop Sentences\nSentencePosition\tWeight");
        for (int position : topN.keySet()) {
            System.out.println(position + "\t" + weightMap.get(position));
        }
        return topN;
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

    private Map removeSimilarSentences(HashMap<Integer, Double> weightMap, String[] sentences) {
        Set<Integer> duplicates = new HashSet<>();
        for (int i = 0; i < sentences.length; i++) {
            for (int j = i + 1; j < sentences.length; j++) {
                Double weightSen1 = weightMap.get(i);
                Double weightSen2 = weightMap.get(j);
                String[] tokensSen1 = featureExtraction.getTokens(sentences[i]);
                String[] tokensSen2 = featureExtraction.getTokens(sentences[j]);
                double similarityScore = similarity.getSimilarityScore(tokensSen1, tokensSen2);
                if (similarityScore > 0.9) {
                    int p = weightSen1 > weightSen2 ? j : i;
                    duplicates.add(p);
                }
            }
        }
        for (Integer position : duplicates) {
            weightMap.remove(position);
        }
        return sortByValue(weightMap);
    }

    private Map sortByValue(HashMap<Integer, Double> weightMap) {
        List list = new LinkedList(weightMap.entrySet());

        // Defined Custom Comparator here
        Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue()));
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = (list).iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private Map sortByKey(Map<Integer, Double> weightMap) {
        List list = new LinkedList(weightMap.entrySet());

        // Defined Custom Comparator here
        Collections.sort(list, (Comparator) (o1, o2) -> ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey()));
        Map sortedHashMap = new LinkedHashMap();
        for (Iterator it = (list).iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    private Map getTopN(Map map, double percent) {
        int size = map.size();
        double v = Math.ceil((percent / 100) * size);
        Map topMap = new LinkedHashMap();
        Set<Integer> set = map.keySet();
        int i = 1;
        for (Integer pos : set) {
            if (i++ > v) {
                break;
            }
            topMap.put(pos, map.get(pos));
        }
        return sortByKey(topMap);
    }
}
