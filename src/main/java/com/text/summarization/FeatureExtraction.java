package com.text.summarization;

import java.lang.*;

import java.io.*;
import java.util.*;

public class FeatureExtraction {

    private Set<String> stopWordList = new HashSet<String>();

    public FeatureExtraction() throws FileNotFoundException {
        LoadStopWords();
    }


    private void LoadStopWords() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("resources/stopwords.txt"));
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().toLowerCase().trim();

            if (line.length() > 0) {
                stopWordList.add(line);
            }
        }
        scanner.close();
    }

    String[] biGrams(String[] words) {
        int len = 2;
        String[] rst = new String[words.length - len + 1];
        for (int i = 0; i < words.length - len + 1; i++) {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < len; k++) {
                if (k > 0) sb.append('_');
                sb.append(words[i + k]);
            }
            rst[i] = sb.toString();
        }
        return rst;
    }

    String[] removeStopWords(String[] words) {
        List<String> arrayList = new ArrayList();
        for (String word :
                words) {

            if (!stopWordList.contains(word.toLowerCase().trim())) {

                arrayList.add(word);
            }

        }
        String[] removedStopWords = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            removedStopWords[i] = arrayList.get(i);

        }
        return removedStopWords;
    }

    double getUpperCaseWeight(String[] words) {
        int count = 0;
        for (String word :
                words) {
            if (word.toUpperCase().equals(word)) {
                count++;
            }

        }
        if (count > 0) {
            return 0.2;
        }

        return 0.0;
    }

    double getSentenceLengthWeight(String[] words) {
        if (words.length > 15) {
            return 0.4;
        } else if (words.length > 10) {
            return 0.3;
        }
        return 0.0;
    }

    double getSentencePositionWeight(int n, int position) {
        int val = n / 4;
        if (position <= val) {
            return 0.3;
        }
        return 0.0;

    }

    double getPartOfSpeechWeight(String[] pos) {
        int count = 0;
        for (String p :
                pos) {
            if (p.equalsIgnoreCase("vrb") || p.equalsIgnoreCase("nn")) {
                count++;
            }
        }
        if (count > 0) {
            return 0.3;
        }
        return 0.0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FeatureExtraction ob = new FeatureExtraction();
        String s = "this is my program";
        String[] tokens = s.split(" ");
        String[] biGrams = ob.biGrams(tokens);
        for (String biGram :
                biGrams) {
            System.out.println(biGram);

        }

        String[] stopWords = ob.removeStopWords(tokens);

        for (String stop :
                stopWords
                ) {
            System.out.println(stop);

            double UpperCaseWeight = ob.getUpperCaseWeight(tokens);

            System.out.println(UpperCaseWeight);

            double SentenceLengthWeight = ob.getSentenceLengthWeight(tokens);
            System.out.println(SentenceLengthWeight);


            double SentencePositionWeight = ob.getSentencePositionWeight(50, 5);
            System.out.println(SentenceLengthWeight);


        }
    }
}










