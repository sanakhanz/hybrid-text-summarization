package com.text.summarization;

import java.util.Arrays;
import java.util.List;

public class WeightCalculator {
//term freq
    public double tf(List<String> doc, String term) {
        double result = 0;
        for (String word : doc) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        return result / doc.size();
    }
    //inverse doc freq
    public double idf(List<List<String>> docs, String term) {
        double n = 0;
        for (List<String> doc : docs) {
            for (String word : doc) {
                if (term.equalsIgnoreCase(word)) {
                    n++;
                    break;
                }
            }
        }
        return Math.log(docs.size() / n);
    }
    //term freq-inverse doc freq
    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
        return tf(doc, term) * idf(docs, term);
    }
    public static void main(String[] args) {
        List<String> doc2 = Arrays.asList("another", "another", "eg", "eg", "eg" ) ;
        List<String> doc1 = Arrays.asList("this", "is", "another", "another", "egaaaad");
     //   List<String> doc3 = Arrays.asList("Has", "the", "disturb", "id", "sum");
        List<List<String>> documents = Arrays.asList(doc1, doc2);

        WeightCalculator calculator = new WeightCalculator();
        double tfidf = calculator.tfIdf(doc2, documents, "eg");
        System.out.println("TF-IDF (eg) = " + tfidf);

    }


}
