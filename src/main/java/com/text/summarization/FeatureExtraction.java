package com.text.summarization;

import opennlp.tools.formats.ad.ADSentenceStream;

import java.lang.*;

import java.io.*;
import java.util.*;

import static com.text.summarization.Utils.*;

public class FeatureExtraction {
    private Set<String> stopWordList = new HashSet<>();

    public FeatureExtraction() throws FileNotFoundException {
        LoadStopWords();
    }

    private void LoadStopWords() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(RESOURCE_DIRECTORY + "stopwords.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase().trim();
            if (line.length() > 0) {
                stopWordList.add(line);
            }
        }
        scanner.close();
    }

    private List<String> biGrams(String[] words) {
        List<String> biGrams = new ArrayList<>();
        for (int i = 0; i < words.length - 1; i++) {
            String biGram = words[i] + "_" + words[i + 1];
            biGrams.add(biGram);
        }
        return biGrams;
    }

    private List<String> removeStopWords(String[] words) {
        List<String> arrayList = new ArrayList();
        for (String word : words) {
            if (!stopWordList.contains(word.toLowerCase().trim())) {
                arrayList.add(word);
            }
        }
        return arrayList;
    }

    double getUpperCaseWeight(String[] words) {
        double count = 0;
        for (String word : words) {
            if (word.toUpperCase().equals(word)) {
                count++;
            }
        }
        if (count > 0) {
            return count / words.length;
        }
        return 0.0;
    }

    double getSentenceLengthWeight(String[] words) {
        if (words.length > 15) {
            return 0.4;
        } else if (words.length > 10) {
            return 0.2;
        }
        return 0.1;
    }

    double getSentencePositionWeight(int totalSentences, int position) {
        int val = totalSentences / 4;
        if (position <= val) {
            return 0.3;
        }
        return 0.0;

    }

    double getPartOfSpeechWeight(String[] pos) {
        double count = 0;
        for (String p :
                pos) {
            if (p.startsWith("VB") || p.startsWith("NN")) {
                count++;
            }
        }
        if (count > 0) {
            return count / pos.length;
        }
        return 0.0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FeatureExtraction ob = new FeatureExtraction();
        String s = "this is my PROGRAM";
        String[] tokens = s.split(" ");
        for (String biGram : ob.biGrams(tokens)) {
            System.out.println(biGram);
        }

        for (String stop : ob.removeStopWords(tokens)) {
            System.out.println(stop);
        }

        double upperCaseWeight = ob.getUpperCaseWeight(tokens);
        System.out.println(upperCaseWeight);

        double senLengthWeight = ob.getSentenceLengthWeight(tokens);
        System.out.println(senLengthWeight);


        double senositionWeight = ob.getSentencePositionWeight(50, 5);
        System.out.println(senositionWeight);
    }
}










