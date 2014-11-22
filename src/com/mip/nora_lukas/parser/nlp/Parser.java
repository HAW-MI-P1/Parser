package com.mip.nora_lukas.parser.nlp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.IndexedWord;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.semgraph.semgrex.SemgrexMatcher;
import edu.stanford.nlp.semgraph.semgrex.SemgrexPattern;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class Parser {

	public Parser(){
		fillMaps();
	}
	
	
	//THIS IS JUST FOR GETTING STARTED...
	//possible values for json:
	String interests="interests";
	String location="location";
	String name ="name";
	String age="age";
	
	HashMap<String, String> verb_to_key = new HashMap<String, String>();
	
	private void fillMaps(){
		verb_to_key.put("like", interests);
		verb_to_key.put("have", interests);
		verb_to_key.put("lives", location);
		verb_to_key.put("name", name);
		verb_to_key.put("call", name);
	}
	
	
//	 @Override
	    public JSONObject parse(String naturalLanguage) {
	    	
		    
	    	//create parsing toolchain
	    	Properties props=new Properties();
	 	   	props.put("annotators","tokenize, ssplit, pos, lemma, ner, parse, dcoref");
	 	    StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
	 	    Annotation annotation = new Annotation(naturalLanguage);
	 	    pipeline.annotate(annotation);

	 	    
	 	   List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
	 	   ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
	 	   Map<IndexedWord, List<IndexedWord>> verbs= get_full_verb_and_object(sentence);
	 	   
	
	 	   Map<String,List<String>> prevJson=new HashMap<String,List<String>>();
	 	   
	 	   for(Map.Entry<IndexedWord, List<IndexedWord>> entry:verbs.entrySet()){
	 		   String key=verb_to_key.get((entry.getKey()).lemma());
 		//	   System.out.println("trying to find key: "+entry.getKey()+" with value "+ entry.getValue());

	 		   if(key != null){
	 			   List<String> val = prevJson.get(key);
	 			   List<String> val2=words_to_lemmas(entry.getValue());
	 		//    	System.out.println("lemmas: "+val2);
	 			   if(val != null)val2.addAll(val);
	 			   prevJson.put(key, val2);
	 		   }
	 		   
	 	   }
	 	   
	// 	   System.out.println("created map:"+prevJson);
	 	   
	 	   
	 	   //TODO: create own method
	 	   //create JSON Object
	 	   JSONObject json=new JSONObject();
	 	   for (Entry<String,List<String>> entry: prevJson.entrySet()){
	 		   json.put("weight", 1);
	 		   json.put(" attribute ", entry.getKey());
	 		   json.put("value_type", "string_list");
	 		   JSONArray list=new JSONArray();
	 		   list.addAll(entry.getValue());
	 		   json.put("value", entry.getValue());
	 	   }
	 	 	
	        return json;
	    }
	    

	    private List<String> words_to_lemmas(List<IndexedWord> list){
	    	List<String> lemmas=new ArrayList<String>();
	    	for(int i=0; i<list.size(); i++) lemmas.add(list.get(i).lemma());
	    	return lemmas;
	    }
	    
	    
	    /*
	     * returns main verbs with corresponding object
	     * */
	  private Map<IndexedWord, List<IndexedWord>> get_full_verb_and_object(ArrayCoreMap annot_sentence){
		  
		  Tree tree = annot_sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
		  SemanticGraph graph = SemanticGraphFactory.generateUncollapsedDependencies(tree);
		  System.err.println(graph); 
		
		//  SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/VB.*/}=A");
		  SemgrexPattern semgrex = SemgrexPattern.compile("{tag:/NN.*/}=A <<dobj  {tag:/VB.*/}=B");
//		  SemgrexPattern semgrex = SemgrexPattern.compile("{}=A <<dobj {}=B");
		  SemgrexMatcher matcher = semgrex.matcher(graph);
		  Map<IndexedWord, List<IndexedWord>> verbs=new HashMap<IndexedWord, List<IndexedWord>>();
		    while (matcher.find()) {
		       // System.err.println(matcher.getNode("A") + " <<dobj " + matcher.getNode("B"));
		    	IndexedWord nodeA = matcher.getNode("A");
		    	IndexedWord nodeB = matcher.getNode("B");
		    	// System.out.println("Verbalphrase:" + nodeA + " Lemma:"+nodeA.lemma() + " tag:"+nodeA.tag());
		    	 List<IndexedWord>temp=verbs.get(nodeB);
		    	 if(temp!=null){
		    		 temp.add(nodeA);
		    	 }else{
		    		 temp=new ArrayList<IndexedWord>();
		    		 temp.add(nodeA);
		    	 }
		    	 verbs.put(nodeB,temp);
		      }
		    
		//   System.out.println("verbs: "+verbs);

		  return verbs;
	  }
	    
	    
	    void testlib(Annotation annotation, StanfordCoreNLP pipeline){

	    	//print stuff zum testen
	        PrintWriter out = new PrintWriter(System.out);
	        pipeline.prettyPrint(annotation, out);
	        
	        /*
		 	    * FROM HERE: EXAMPLE HOW TO GET TO THE PARSED DATA
		 	    * Interesting for us is NER and ParseTree (main verb and objects)
		 	    */
		 	   
		 	   
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
			          
			    
			         }}
	    }

	
}


