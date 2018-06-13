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
    public void calculateTermFrequency(String[] vector) {
        for (String vec : vector) {
            termFrequency.put(vec, termFrequency.getOrDefault(vec, 0) + 1);
        }
    }

    //inverse doc freq
    public double idf(String term) {
        if (documentFrequency.containsKey(term)) {
            int docFreq = documentFrequency.get(term);
            return Math.log(TOTAL_NO_OF_CORPUS / docFreq);
        } else {
            return 1.0;
        }
    }

    //term freq-inverse doc freq
    public double tfIdf(String term) {
        return termFrequency.get(term) * idf(term);
    }

    public static void main(String[] args) throws IOException {
        OpenNlpTools openNlpTools = new OpenNlpTools();
        HashMap<String, Integer> documentFreq = calculateDocFrequency(openNlpTools);
        write(documentFreq);
        WeightCalculator calculator = new WeightCalculator();
        String[] tokens = "Calculates Df using background corpus".split(" ");
        calculator.calculateTermFrequency(tokens);
        System.out.println(calculator.tfIdf("Df"));
    }

    /**
     * Calculates Df using background corpus.
     *
     * @throws IOException
     */
    public static HashMap<String, Integer> calculateDocFrequency(OpenNlpTools openNlpTools) throws IOException {
        int totalNoOfCorpusDocuments = 0;
        HashMap<String, Integer> documentFrequency = new HashMap<>();
        HashMap<Integer, Set<String>> documentss = new HashMap<>();
        File dir = new File(RESOURCE_DIRECTORY + "Documents/");
        File[] allfiles = dir.listFiles();
        for (File f : allfiles) {
            if (f.getName().endsWith(".txt")) {
                String s = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())));
                String document = s.toLowerCase();
                Set<String> tokenSet = new HashSet<>();
                String[] tokenArray = openNlpTools.getTokens(document);
                String[] stems = openNlpTools.getStems(tokenArray);

                for (int i = 0; i < tokenArray.length; i++) {
                    String tok = tokenArray[i].trim().toLowerCase();
                    if (!tokenSet.contains(tok)) {
                        tokenSet.add(tok);
                    }
                    documentFrequency.put(tok, documentFrequency.getOrDefault(tok, 0) + 1);
                }
                for (int i = 0; i < stems.length; i++) {
                    String stm = stems[i].trim().toLowerCase();
                    if (!tokenSet.contains(stm)) {
                        tokenSet.add(stm);
                    }
                    documentFrequency.put(stm, documentFrequency.getOrDefault(stm, 0) + 1);
                }
                documentss.put(totalNoOfCorpusDocuments++, tokenSet);
            }
        }
        System.out.println("TOTAL_ CORPUS DOCUMENTS: " + totalNoOfCorpusDocuments);
        return documentFrequency;
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
