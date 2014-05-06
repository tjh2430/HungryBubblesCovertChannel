package com.example.intentutils.lib;

import android.content.Intent;

import java.util.List;

/**
 * Interface which defines a schema for encoding and decoding information
 * in an {@link android.content.Intent}.
 * 
 * @author Timothy Heard
 */
public interface EncodingSchema
{
	// TODO: Things to add:
	//
	// 1. Max message size
	// 2. Max Intent size
	// 3. (Serializable) representation of a character set
	// 4. (Serializable) collection of character sets into a full encoding scheme, including expansion 
	//    code -> character set mappings
	// 5. Bundle key access ordering? => Pass in a String Comparator
	// 6. (Serializable) Bundle getter call ordering?
	
	public List<Intent> encode(String message, int buildVersion);
	public String decode(Intent carrier, int buildVersion);
	
	/**
	 * Returns {@code true} if the given {@code character} is supported by the 
	 * encoding schema and {@code false} otherwise. 
	 */
	// TODO: may want to change this to accept a String parameter to allow full
	// words to be encoded using a single code (Huffman-type coding for a 
	// dicionary or control language)
	public boolean isCharSupported(char character);
	
	/**
	 * Returns how many different characters/symbols which can be represented
	 * and encoded using this schema.
	 */
	public int getEncodableCharactersCount(int buildVersion);
}
