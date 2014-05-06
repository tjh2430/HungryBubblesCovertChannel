package com.example.intentutils.lib;

/**
 * Represents the encoding for all of the lower case letters from 'a' to 'z' as
 * well as single spaces. 
 * 
 * @author Timothy Heard
 */
public class LowerCaseAlphaEncodingDictionary implements EncodingDictionary
{
	// The number of lower case characters plus the space character
	private static final int DICTIONARY_SIZE = 27;
	
	// The space character is the last character code supported by this 
	// dictionary, and character codes start at zero, which is why the code for
	// the space character is 26 (the dictionary size - 1)
	private static final int SPACE_CODE = 26;
	
	@Override
	public int getCharCode(char c, int buildVersion)
	{
		if(c == ' ')
		{
			return SPACE_CODE;
		}
		
		return (c - 'a');
	}
	
	@Override
	public char getChar(int charCode, int buildVersion)
		throws IllegalArgumentException
	{
		if(!isValidCharCode(charCode, buildVersion))
		{
			throw new IllegalArgumentException("In " + 
				this.getClass().getName() + ".getChar(): character code " + 
				charCode + " is not valid.");
		}
	
		if(charCode == SPACE_CODE)
		{
			return ' ';
		}
		
		return (char) (charCode + 'a');
	}

	@Override
	public boolean isValidCharCode(int charCode, int buildVersion)
	{
		return (charCode < DICTIONARY_SIZE) && (charCode >= 0);
	}
	
	@Override
	public boolean isSupportedChar(char c, int buildVersion)
	{
		return (c == ' ') || (c <= 'z') && (c >= 'a');
	}

	@Override
	public int getDictionarySize(int buildVersion)
    {
	    return DICTIONARY_SIZE;
    }
}
