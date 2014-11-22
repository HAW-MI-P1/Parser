package com.mip.nora_lukas.parser.nlp;

import static org.junit.Assert.*;

import java.util.ArrayList;

import jdk.nashorn.internal.ir.annotations.Ignore;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class ParserTest {

//THIS IS A TEST
	Parser parser;
    @Before
    public void setUp() {
        System.out.println("Starting Parser Test");
        parser=new Parser();
    }

	
	@Test
	public void testSimpleSentences() {
		JSONObject test01=parser.parse("who likes dogs?");
		String[] objects01={"dog"};
		assertTrue(test01.containsValue(Arrays.asList(objects01)));
		
		JSONObject test02=parser.parse("which person likes dogs and cats?");
		String[] objects02={"dog","cat"};
		assertTrue(test02.containsValue(Arrays.asList(objects02)));
		
		JSONObject test03=parser.parse("who likes (dogs and cats) or pigs?");
		String[] objects03={"dog","pig","cat"};
		assertTrue(test03.containsValue(Arrays.asList(objects03)));
		
		JSONObject test04=parser.parse("who is called Jane?");
		System.out.println(test04);
		String attribute04_key="attribute";
		String attribute04_value="name";
		String value04_key="value";
		String[] value04_value={"Jane"};
		assertTrue(test04.get(attribute04_key).equals(attribute04_value));
		assertTrue(test04.get(value04_key).equals(Arrays.asList(value04_value)));

		
	}
	
	@Ignore
	@Test
	public void testMoreComplexSentences() {
		//fail("Not yet implemented");
		//assertTrue
	}


}
