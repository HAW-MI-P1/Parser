package com.mip.nora_lukas.parser.nlp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.NERIDAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;



public class DemoAndTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
	    PrintWriter out;
	    if (args.length > 1) {
	      out = new PrintWriter(args[1]);
	    } else {
	      out = new PrintWriter(System.out);
	    }
	    PrintWriter xmlOut = null;
	    if (args.length > 2) {
	      xmlOut = new PrintWriter(args[2]);
	    }
	    Properties props=new Properties();
	    props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	    
	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	    Annotation annotation;
	    if (args.length > 0) {
	      annotation = new Annotation(IOUtils.slurpFileNoExceptions(args[0]));
	    } else {
	      annotation = new Annotation("Who (in the age between 20 and 30) are in South Afrika(10) right now?");
	    }

	    pipeline.annotate(annotation);
	    pipeline.prettyPrint(annotation, out);
	    if (xmlOut != null) {
	      pipeline.xmlPrint(annotation, xmlOut);
	    }

	    // An Annotation is a Map and you can get and use the various analyses individually.
	    // For instance, this gets the parse tree of the first sentence in the text.
	    out.println();
	    // The toString() method on an Annotation just prints the text of the Annotation
	    // But you can see what is in it with other methods like toShorterString()
	    out.println("The top level annotation");
	    out.println(annotation.toShorterString());
	    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	    if (sentences != null && sentences.size() > 0) {
	      ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
	      out.println("The first sentence is:");
	      out.println(sentence.toShorterString());
	      Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
	      out.println();
	      out.println("The first sentence tokens are:");
	      for (CoreMap token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
	        ArrayCoreMap aToken = (ArrayCoreMap) token;
	        out.println(aToken.toShorterString());
	      }
	      out.println("The first sentence parse tree is:");
	      tree.pennPrint(out);
	      out.println("The first sentence basic dependencies are:"); 
	      System.out.println(sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class).toString(SemanticGraph.OutputFormat.LIST));
	      out.println("The first sentence collapsed, CC-processed dependencies are:");
	      SemanticGraph graph = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);
	      System.out.println(graph.toString(SemanticGraph.OutputFormat.LIST));
	     System.out.println("NER:");
	   
	         // traversing the words in the current sentence
	         // a CoreLabel is a CoreMap with additional token-specific methods
	         for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
	           // this is the text of the token
	           String word = token.get(TextAnnotation.class);
	           // this is the POS tag of the token
	           String pos = token.get(PartOfSpeechAnnotation.class);
	           // this is the NER label of the token
	           String ne = token.get(NamedEntityTagAnnotation.class);     
	           
	           System.out.println(word+":"+ne);
	          
	    
	         }}}
}
