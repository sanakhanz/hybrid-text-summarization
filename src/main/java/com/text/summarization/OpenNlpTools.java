package com.text.summarization;

import java.lang.*;
import java.io.*;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.stemmer.PorterStemmer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class OpenNlpTools {
    private SentenceDetectorME detector;
    private Tokenizer tokenizer;
    private POSTaggerME posTaggerME;
    private PorterStemmer porterStemmer;


    public OpenNlpTools() throws IOException {

        InputStream senStream = new FileInputStream("resources/en-sent.bin");
        SentenceModel model = new SentenceModel(senStream);
        detector = new SentenceDetectorME(model);

        InputStream tokStream = new FileInputStream("resources/en-token.bin");
        TokenizerModel tokenizerModel = new TokenizerModel(tokStream);
        tokenizer = new TokenizerME(tokenizerModel);

        InputStream posStream = new FileInputStream("resources/en-pos-maxent.bin");
        POSModel posModel = new POSModel(posStream);
        posTaggerME = new POSTaggerME(posModel);

        porterStemmer = new PorterStemmer();


    }

    String[] getSentences(String text) {
        return detector.sentDetect(text);
    }

    String[] getTokens(String text) {
        return tokenizer.tokenize(text);
    }

    String[] getPosTag(String[] tokens) {
        return posTaggerME.tag(tokens);
    }

    String[] getStems(String[] tokens) {
        String[] stems = new String[tokens.length];
        int i = 0;
        for (String token : tokens) {
            stems[i++] = porterStemmer.stem(token);
        }
        return stems;
    }


    public static void main(String[] arg) throws IOException {
        String sa = "sana is my name. i live in kanitar. i do mscit in ku. i am completing my project ";
        OpenNlpTools ob = new OpenNlpTools();
        String[] s = ob.getSentences(sa);
        String[] tokens = ob.getTokens(sa);
        String[] posTag = ob.getPosTag(tokens);
        String[] stems = ob.getStems(tokens);

        System.out.println("Sentences");
        for (int i = 0; i < s.length; i++) {
            System.out.println(s[i]);
        }

        System.out.println("tokens");
        for (String t : tokens ) {
            System.out.println(t);
        }
        System.out.println("pos");
        for (String t : posTag ) {
            System.out.println(t);
        }
        System.out.println("Stems");
        for (String t : stems ) {
            System.out.println(t);
        }
    }

}


