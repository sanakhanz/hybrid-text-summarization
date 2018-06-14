package com.text.summarization;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.text.summarization.Utils.*;

public class WeightCalculator {
    private HashMap<String, Integer> documentFrequency = new HashMap<>();
    private HashMap<String, Integer> termFrequency = new HashMap<>();

    public WeightCalculator() throws IOException {
        loadDfFile(RESOURCE_DIRECTORY + "dict.txt");
    }

    private void loadDfFile(String file) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(file));
        while (scanner.hasNextLine()) {
            String s = scanner.nextLine();
            String[] split = s.split("\t");
            if (split.length == 2) {
                documentFrequency.put(split[0].trim(), Integer.valueOf(split[1].trim()));
            } else {
                System.out.println(s);
            }
        }
        scanner.close();
    }

    //term freq
    public void calculateTermFrequency(List<String> vector) {
        for (String vec : vector) {
            vec = vec.toLowerCase().trim();
            termFrequency.put(vec, termFrequency.getOrDefault(vec, 0) + 1);
        }
    }

    //inverse doc freq
    public double idf(String term) {
        double docFreq = documentFrequency.getOrDefault(term, 1);
        return Math.log(TOTAL_NO_OF_CORPUS / docFreq);
    }

    //term freq-inverse doc freq
    public double getTfIdf(String term) {
        term = term.toLowerCase().trim();
        return termFrequency.get(term) * idf(term);
    }

    public static void main(String[] args) throws IOException {
        FeatureExtraction featureExtraction = new FeatureExtraction();
        HashMap<String, Integer> documentFreq = calculateDocFrequency(featureExtraction);
        write(documentFreq);
        WeightCalculator calculator = new WeightCalculator();
        List<String> tokens = Arrays.asList("Calculates Df using background corpus".split(" "));
        calculator.calculateTermFrequency(tokens);
        System.out.println(calculator.getTfIdf("Df"));
    }

    /**
     * Calculates Df using background corpus.
     *
     * @throws IOException
     */
    public static HashMap<String, Integer> calculateDocFrequency(FeatureExtraction featureExtraction) throws IOException {
        int totalNoOfCorpusDocuments = 0;
        HashMap<String, Integer> documentFrequency = new HashMap<>();
        File dir = new File(RESOURCE_DIRECTORY + "Documents/");
        File[] allfiles = dir.listFiles();
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                totalNoOfCorpusDocuments++;
                String s = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                String document = s.toLowerCase();
                String[] tokenArray = featureExtraction.getTokens(document);
                String[] stems = featureExtraction.getStems(tokenArray);
                List<String> biGrams = featureExtraction.getBiGrams(tokenArray);
                List<String> removeStopWords = featureExtraction.removeStopWords(tokenArray);
                Set<String> vector = getVector(removeStopWords, stems, biGrams);
                for (String vec : vector) {
                    documentFrequency.put(vec, documentFrequency.getOrDefault(vec, 0) + 1);
                }
            }
        }
        System.out.println("TOTAL_ CORPUS DOCUMENTS: " + totalNoOfCorpusDocuments);
        return documentFrequency;
    }

    private static Set<String> getVector(List<String> removeStopWords, String[] stems, List<String> biGrams) {
        Set<String> vector = new HashSet<>();
        for (String s : removeStopWords) {
            vector.add(s.toLowerCase().trim());
        }
        for (String s : stems) {
            vector.add(s.toLowerCase().trim());
        }
        for (String s : biGrams) {
            vector.add(s.toLowerCase().trim());
        }
        return vector;
    }

    private static void write(HashMap<String, Integer> documentfreq) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(RESOURCE_DIRECTORY + "dict.txt");
        Set<String> keys = documentfreq.keySet();
        for (String key : keys) {
            printWriter.write(key + "\t" + documentfreq.get(key) + "\n");
        }
        printWriter.close();
    }
}
