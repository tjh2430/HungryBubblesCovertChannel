package com.example.intentutils.lib;

/**
 * Represents a set of encodable characters.
 *
 * @author Timothy Heard
 */
public interface EncodingDictionary
{
	public int getCharCode(char c, int buildVersion);
	
	public char getChar(int charCode, int buildVersion);

	boolean isSupportedChar(char c, int buildVersion);
	
	boolean isValidCharCode(int charCode, int buildVersion);
	
	int getDictionarySize(int buildVersion);
}
