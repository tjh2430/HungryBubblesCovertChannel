package com.example.intentutils.lib;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// TODO: Make the encoding scheme more intelligent by encoding the more 
// frequently used characters in the smaller type fields (i.e. byte, short,
// etc.)

// TODO: Retrofit to work with API 7/all APIs
public class LowerCaseAlphaEncoder implements EncodingSchema
{
	// TODO: Change to a configurable value
	private static final int MAX_MESSAGE_SIZE = Integer.MAX_VALUE;

	// TODO: Change to a configurable value
	private static final int BUILD_VERSION = 8;
	
	private EncodingDictionary dictionary;
	
	// TODO: Use a more intelligent, less detectable key generation scheme?
	private AlphabeticalKeySequence keySequence;
	
	// Comparator for ordering the String keys in a Bundle.
	private Comparator<String> keyComparator;
	
	public LowerCaseAlphaEncoder()
	{
		dictionary = new LowerCaseAlphaEncodingDictionary();
		keySequence = new AlphabeticalKeySequence();
		keyComparator = new AlphabeticalKeySequenceComparator();
	}
	
	@Override
	public List<Intent> encode(String message, int buildVersion)
	{
		char[] chars = message.toCharArray();
		int numSegments = chars.length / MAX_MESSAGE_SIZE;
		
		// If MAX_MESSAGE_SIZE does not divide evenly into the number of
		// characters contained in the message to encode then an additional
		// segment is going to be required for encoding the remainder
		if((chars.length % MAX_MESSAGE_SIZE) != 0)
		{
			++numSegments;
		}
		
		List<Intent> messageCarriers = new ArrayList<Intent>();
		for(int i = 0; i < numSegments; i++)
		{
			// Calculate the bounding indices for the current segment. Note
			// that the start index is inclusive and the end index is 
			// exclusive. If this is the last segment then chars.length will be
			// used as the end index; otherwise the start index of the next 
			// segment ((i + 1) * MAX_MESSAGE_SIZE) will be used.
			int startIndex = i * MAX_MESSAGE_SIZE;
			int endIndex = i == (numSegments - 1) ? chars.length : (i + 1) * MAX_MESSAGE_SIZE;
			
			messageCarriers.add(encodeSegment(chars, startIndex, endIndex));
		}
		
		return messageCarriers;
	}

	/**
	 * Attempts to decode the covert message contained in the {@code carrier}
	 * {@link android.content.Intent}'s {@link android.os.Bundle}. Will either return the decoded message
	 * as a {@link String} or {@code null} if the provided {@code carrier} or 
	 * the {@link android.os.Bundle} of extras for that {@link android.content.Intent} are {@code null}.
	 */
	@Override
	public String decode(Intent carrier, int buildVersion)
	{
		Bundle dataBundle;
		if(carrier == null || (dataBundle = carrier.getExtras()) == null)
		{
			return null;
		}
		
		Set<String> bundleKeys;
		bundleKeys = new TreeSet<String>(keyComparator);
		bundleKeys.addAll(dataBundle.keySet());
		
		// Create a StringBuilder with an initial capacity equal to the number
		// of Bundle keys (which is equivalent to the possible number of 
		// encoded characters) for accumulating the covertly-encoded characters
		StringBuilder messageBuilder = new StringBuilder(bundleKeys.size());
		
		try
		{
			for(String key: bundleKeys)
			{
				messageBuilder.append(decodeChar(dataBundle, key, buildVersion));
			}
		}
		catch(IllegalArgumentException e)
		{
			// TODO: Log the fact that some message could not be fully encoded 
			// in a more application visible way
			
			// TODO: DO NOT USE LOGCAT IN THE FINAL IMPLEMENTATION => too visible
			Log.w(this.getClass().getName(), "Could not fully decode message");
		}
		
		return messageBuilder.toString();
	}

	/**
	 * Returns {@code true} if the given {@code character} is supported by the 
	 * encoding schema and {@code false} otherwise. 
	 */
	@Override
	public boolean isCharSupported(char character)
	{
		return dictionary.isSupportedChar(character, BUILD_VERSION);
	}

	@Override
	public int getEncodableCharactersCount(int buildVersion)
	{
		return dictionary.getDictionarySize(buildVersion);
	}
	
	/**
	 * Encodes the characters in {@code charArray} from {@code startIndex}
	 * (inclusively) to {@code endIndex} (exclusively) into a {@link android.os.Bundle}
	 * encapsulated in an {@link android.content.Intent}.
	 *  
	 * @throws IllegalArgumentException	If the {@code startIndex} is not a 
	 * valid {@code charArray} index, if {@code endIndex} is less than 0 or
	 * greater than {@code charArray}.{@code length} (not that it can be equal 
	 * to the length of the array as it is exclusive), or if {@code startIndex}
	 * is greater than or equal to {@code endIndex}.
	 */
	private Intent encodeSegment(char[] charArray, int startIndex, int endIndex)
		throws IllegalArgumentException
	{
		if(startIndex < 0 || startIndex >= charArray.length || 
		   endIndex < 0 || endIndex > charArray.length || 
		   startIndex >= endIndex)
		{
			throw new IllegalArgumentException("In " + 
				this.getClass().getCanonicalName() + ": Invalid index value");
		}
		
		Intent carrier = new Intent();
		Bundle dataPacket = new Bundle();
		
		for(int i = startIndex; i < endIndex; i++)
		{
			encodeChar(dataPacket, keySequence.next(), charArray[i], BUILD_VERSION);
		}
		
		carrier.putExtras(dataPacket);
		return carrier;
	}
	
	/**
	 * Encodes a single character in the given {@link android.os.Bundle} using the given
	 * {@code key}.
	 * 
	 * @throws IllegalArgumentException	If an unsupported character (anything 
	 * that is not a lower case character from a-z) is provided.
	 */
    private void encodeChar(Bundle dataPacket, String key, char c, int buildVersion)
		throws IllegalArgumentException
	{
    	if(!isCharSupported(c))
    	{
    		throw new IllegalArgumentException("In " + 
    			this.getClass().getName() + ".encodeChar(): Unsupported " + 
				"character " + c + " encountered");
    	}
    	
		int charCode = dictionary.getCharCode(c, buildVersion);
		
		EncodingUtils.encodeCharCode(dataPacket, key, charCode,
			buildVersion);
	}
	

	/**
	 * Decodes the character encoded in the given {@link android.os.Bundle} for the given
	 * {@code key}.
	 * 
	 * @throws IllegalArgumentException	If the provided {@code key} does not 
	 * exist in the given {@link android.os.Bundle} or if no character encoding was found
	 * for that {@code key}. 
	 */
	private char decodeChar(Bundle dataPacket, String key, int buildVersion)
		throws IllegalArgumentException
    {
		int charCode = EncodingUtils.decodeCharCode(dataPacket, key, buildVersion);
		return dictionary.getChar(charCode, buildVersion);
    }
}
