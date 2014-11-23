package com.mip.nora_lukas.parser.nlp;

import org.json.simple.JSONObject;

public class SimpleTest {

	/**
	 * @param args
	 */



	public static void main(String[] args) {
		Parser parser=new Parser();
		JSONObject json=parser.parse("Who (in the age between 20 and 30) is in South Afrika(10) right now and likes cats and dogs or pigs?");

		//	JSONObject json= parser.parse("Who is a cat?");
		System.out.println("json: "+json);
	}


}
