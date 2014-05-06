package com.example.intentutils.lib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Set of utility functions for encoding and decoding covert message in 
 * Intent objects.
 * 
 * @author Timothy Heard
 */

// TODO: Add a mechanism for specifying which and how many get/set by type 
// methods to use for encoding; expansion codes are then implicit

// TODO: Allow a custom String Comparator to be passed in so that clients 
// (the EncodingSchemas) can vary the key access ordering
public class EncodingUtils
{
	/*
	 * Custom action strings
	 */
	// NOTE: Any changes to these values must be reflected in the intent filter 
	// for any Activity of Service which is listening for Intents with these actions
	public static final String RECEIVER_COVERT_MESSAGE_ACTION = "receive_covert_message_action";
	public static final String CALCULATE_THROUGHPUT_ACTION= "calculate_throughput_action";
	public static final String CALCULATE_BIT_ERROR_RATE = "calculate_bit_error_rate";
	public static final String SEND_TIME_ACTION = "send_time";
	public static final String SEND_BIT_ERRORS_ACTION = "send_bit_errors";
	
	/*
	 * String key constants
	 */
	public static final String START_TIME_KEY = "start_time";
	public static final String END_TIME_KEY = "end_time";
	public static final String BIT_ERROR_KEY = "bit_error";
	public static final String RECEIVED_MESSAGE_KEY = "received_message";
	public static final String ORIGINAL_MESSAGE_KEY = "original_message";
	
	// TODO: Remove these flags or make use of them
	public static final int FLAG_FIELD_ONLY = 1;
	public static final int INTENT_FIELDS_ONLY = 2;
	public static final int BUNDLE_FIELDS_ONLY = 3;
	public static final int INTENT_AND_BUNDLE_FIELDS_ONLY = 4;
	public static final int NESTED_BUNDLE_FIELDS_ONLY = 5;
	public static final int INTENT_AND_NESTED_BUNDLE_FIELDS_ONLY = 6;
	public static final int MAX_BANDWIDTH = 7;
	public static final int MAX_STEALTH = 8;
	
	// TODO: Remove the encode() and decode() functions from this class and
	// have this class serve as more of an internal utils class for the 
	// different EncodingSchemas to use for encoding specific character/expansion
	// code pairs for given keys
	/**
	 * Encodes the given {@code message} into a series of {@link android.content.Intent}
	 * objects using the channel(s) indicated by the {@code channelFlag}
	 * parameter.
	 * 
	 * @param schema		The {@link EncodingSchema} to use for encoding the
	 * 						provided {@code message}.
	 * 
	 * @param apiVersion	Which version of the Android API is being used
	 * 
	 * @param channelFlag	Specifies which channel(s) should be used for 
	 * 						encoding the {@code message}
	 * 
	 * @param message		The message to encode
	 * 
	 * @return				A {@link List} of {@link android.content.Intent}s containing the
	 * 						encoded message	in order (note that the returned
	 * 						{@link android.content.Intent}s must be transmitted and received
	 * 						in the ordering they are returned in or else the
	 * 						message will not be communicated properly). If the
	 * 						message cannot be encoded for any reason 
	 * 						{@code null} will be returned instead.   
	 */
	/*
	public static List<Intent> encodeMessage(EncodingSchema schema, 
		int apiVersion, int channelFlag, String message)
	{
		// TODO: Make better use of this class (i.e. don't have it just be 
		// a call through class)
		return schema.encode(message);
	}
	*/
	
	/**
	 * Decodes the message contained in the provided {@link android.content.Intent} using the
	 * channel(s) indicated by the {@code channelFlag} parameter.
	 * 
	 * @param schema		The {@link EncodingSchema} to use for decoding the
	 * 						message.
	 * 
	 * @param buildVersion	Which build/version of Android is being used
	 * 
	 * @param channelFlag	Specifies which channel(s) should be used for 
	 * 						decoding the message.
	 * 
	 * @param messageIntent	The {@link android.content.Intent} containing the encoded message
	 * 
	 * @return				The decoded message as a {@link String} or 
	 * 						{@code null} if either no covert message is found
	 * 						(based on the specified channel(s) and API version)
	 * 						or if the message could not be decoded for any 
	 * 						reason.
	 */
	/*
	public static String decodeMessage(EncodingSchema schema, int buildVersion,
		int channelFlag, Intent messageIntent)
	{
		// TODO: Remove this block
		/*
		String message = null;
		
		int flags = messageIntent.getFlags();
		int covertFlags = (flags & ~getAllFlags());
		
		message = Integer.toBinaryString(covertFlags);
		*/
		// END TODO
	/*	
		return schema.decode(messageIntent, buildVersion);
	}
	*/
	
	/**
	 * Calculates the result of combining (logically or-ing) all of the defined 
	 * Intent flags and returns the result.
	 */
	public static int getAllFlags()
	{
		// Total of 20 flags here (all those defined up to API version 7)
		return (Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |  
				Intent.FLAG_ACTIVITY_CLEAR_TOP | 
				Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
				Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | 
				Intent.FLAG_ACTIVITY_FORWARD_RESULT |
				Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY | 
				Intent.FLAG_ACTIVITY_MULTIPLE_TASK |
				Intent.FLAG_ACTIVITY_NEW_TASK | 
				Intent.FLAG_ACTIVITY_NO_ANIMATION | 
				Intent.FLAG_ACTIVITY_NO_HISTORY | 
				Intent.FLAG_ACTIVITY_NO_USER_ACTION |
				Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | 
				Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
				Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | 
				Intent.FLAG_ACTIVITY_SINGLE_TOP |
				Intent.FLAG_DEBUG_LOG_RESOLUTION | 
				Intent.FLAG_FROM_BACKGROUND | 
				Intent.FLAG_GRANT_READ_URI_PERMISSION | 
				Intent.FLAG_GRANT_WRITE_URI_PERMISSION |				
				Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		
		/* These are all of the flags listed in the Inent.setFlags() documentation
		return (Intent.FLAG_GRANT_READ_URI_PERMISSION |
		Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
		Intent.FLAG_DEBUG_LOG_RESOLUTION |
		Intent.FLAG_FROM_BACKGROUND |
		Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT |
		Intent.FLAG_ACTIVITY_CLEAR_TASK |
		Intent.FLAG_ACTIVITY_CLEAR_TOP |
		Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
		Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS |
		Intent.FLAG_ACTIVITY_FORWARD_RESULT |
		Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY |
		Intent.FLAG_ACTIVITY_MULTIPLE_TASK |
		Intent.FLAG_ACTIVITY_NEW_TASK |
		Intent.FLAG_ACTIVITY_NO_ANIMATION |
		Intent.FLAG_ACTIVITY_NO_HISTORY |
		Intent.FLAG_ACTIVITY_NO_USER_ACTION |
		Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |
		Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED |
		Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | 
		Intent.FLAG_ACTIVITY_SINGLE_TOP |
		Intent.FLAG_ACTIVITY_TASK_ON_HOME | 
		Intent.FLAG_RECEIVER_REGISTERED_ONLY);
		*/
		
		/**
		 * Theses Intent flags are defined in later versions of the API (6 more)
		 */
		
		// API Version 8
		// Intent.FLAG_RECEIVER_REPLACE_PENDING
		
		// API Version 11
		// Intent.FLAG_ACTIVITY_CLEAR_TASK 
		// Intent.FLAG_ACTIVITY_TASK_ON_HOME 
		
		// API version 12
		// Intent.FLAG_EXCLUDE_STOPPED_PACKAGES
		// Intent.FLAG_INCLUDE_STOPPED_PACKAGES
		
		// API Version 16
		// Intent.FLAG_RECEIVER_FOREGROUND
		
		// NOTE: Using the number of currently defined Intent flags up to API version
		// 4.0.3 only 26 bits out of 32 available bits should be under use for this 
		// Intent field (only 20 bits for API version 7)
	}
	
	/**
	 * Returns the count of how many times {@code searchString} occurs in {@code str}.
	 */
	public static int countOccurencesIn(String str, String searchString)
	{
		int count = 0;
		int index = 0;
		
		while(str.indexOf(searchString, index) >= index)
		{
			count++;
			index = str.indexOf(searchString, index) + searchString.length();
			
			if(index > str.length())
			{
				break;
			}
		}
		
		return count;
	}
	
	/**
	 * Returns the number of characters which can be encoded without the use of
	 * expansion codes when using the provided android build version.
	 * 
	 * @throws IllegalArgumentException	If the provided {@code buildVersion} is
	 * not a valid Android build version number as defined by 
	 * android.os.Build.VERSION_CODES (see
	 * http://developer.android.com/reference/android/os/Build.VERSION_CODES.html)
	 */
	public static int getCharacterSetSize(int buildVersion)
		throws IllegalArgumentException
	{
		// TODO: use a constant instead of a hard-coded value
		return 21;
	}
	
	// TODO: Document the fact that the lower order codes (i.e. smaller integer values)
	// will be encoded using the smaller Bundle fields 
	//
	// TODO: Add comments
	// Build version constants: http://developer.android.com/reference/android/os/Build.VERSION_CODES.html
	public static void encodeCharCode(Bundle dataPacket, String key, int charCode, int buildVersion)
	{
		Bundle bundle = dataPacket;
		
		int expansionCode = charCode / getCharacterSetSize(buildVersion);
		for(int i = 0; i < expansionCode; i++)
		{
			// TODO: Remove
			Log.d("CovertChannel", "Nesting bundle " + i);
			
			Bundle nestedBundle = new Bundle();
			
			// TODO: Allow for variance in the expansion code key to decrease
			// detectability
			bundle.putBundle(key, nestedBundle);
			key = "a";
			bundle = nestedBundle;
		}
		
		// TODO: Add error checking to ensure that the expansion code and char code line up
		// and then remove the throws block at the end of the switch (possibly)
		
		// TODO: Make sure that this is safe and that it works
		int baseCharCode = charCode - (expansionCode * getCharacterSetSize(buildVersion)); 

		// TODO: Remove
		Log.d("CovertChannel", "charCode = " + charCode + ", expansionCode = " + expansionCode + 
				", baseCharCode = " + baseCharCode);
		
		if(baseCharCode < 0)
		{
			
			throw new IllegalArgumentException("In EncodingUtils.encodeChar(): Unsupported " + 
				"character code " + baseCharCode + " encountered");
		}
		
		switch(baseCharCode)
		{
		case 0:
			bundle.putBoolean(key, true);
			break;
		case 1:
			bundle.putBooleanArray(key, new boolean[0]);
			break;
		case 2:
			bundle.putByte(key, (byte)1);
			break;
		case 3:
			bundle.putByteArray(key, new byte[0]);
			break;
		case 4:
			bundle.putChar(key, (char) 1);
			break;
		case 5:
			bundle.putCharArray(key, new char[0]);
			break;
		case 6:
			bundle.putCharSequence(key, (CharSequence) "1");
			break;

		// Note that the use of CharSequenceArrays and Strings for encoding 
		// data is mutually exclusive (i.e. either putCharSequenceArray() 
		// or putString() can be used, but not both)
		case 7:
			bundle.putCharSequenceArray(key, new CharSequence[0]);
			// bundle.putString(key, "1");
			break;
			
		// Note that all ArrayList types are mutually exclusive for the
		// purposes of covert message encoding using this channel
		case 8:
			bundle.putCharSequenceArrayList(key, new ArrayList<CharSequence>());
			// bundle.putIntegerArrayList(key, new ArrayList<Integer>());
			// bundle.putParcelableArrayList(key, new ArrayList<Parcelable>());
			break;
		case 9:
			bundle.putDouble(key, 1.0);
			break;
		case 10:
			bundle.putDoubleArray(key, new double[0]);
			break;
		case 11:
			bundle.putFloat(key, (float) 1.0);
			break;
		case 12:
			bundle.putFloatArray(key, new float[0]);
			break;
		case 13:
			bundle.putInt(key, 1);
			break;
		case 14:
			bundle.putIntArray(key, new int[0]);
			break;
		case 15:
			bundle.putLong(key, 1L);
			break;
		case 16:
			bundle.putLongArray(key, new long[0]);
			break;
		case 17:
			bundle.putParcelableArray(key, new Parcelable[0]);
			break;
		case 18:
			bundle.putShort(key, (short) 1);
			break;
		case 19:
			bundle.putShortArray(key, new short[0]);
			break;
		case 20:
			bundle.putSparseParcelableArray(key, new SparseArray<Parcelable>());
			break;
		default:
			throw new IllegalArgumentException("In EncodingUtils.encodeChar(): Unsupported " + 
					"character code " + baseCharCode + " encountered");
		}	
		
		/* TODO: Figure out how to get this working (if it is even possible)
		case :
			dataPacket.putParcelable(key, new Parcelable() {
				
				@Override
				public int describeContents()
				{
					// TODO Auto-generated method stub
					return 0;
				}
				
				@Override
				public void writeToParcel(Parcel dest, int flags)
				{
					// TODO Auto-generated method stub
					
				}});
			break;
			
		// NOTE: short and short-arrays can be retrieved as serializables
		case 'v':
			dataPacket.putSerializable(key, new Serializable(){});
			break;
		*/
	}

	public static int decodeCharCode(Bundle dataPacket, String key, int buildVersion)
	{
		// The Bundle to decode for the base character code value (i.e. the character
		// code which will be used with the expansion code in order to reconstruct the
		// original character
		Bundle charCodeBundle = dataPacket;
		
		Bundle nestedBundle = dataPacket.getBundle(key);
		int expansionCode = 0;
		int charSetSize = getCharacterSetSize(buildVersion);
		int charCode;
		
		while(nestedBundle != null)
		{
			expansionCode++;
			
			// The inner-most Bundle (i.e. the one of the end of the nesting 
			// chain) is the once which contains the actual character encoding
			charCodeBundle = nestedBundle;
			
			// TODO: Use a constant and/or allow for variability
			nestedBundle = nestedBundle.getBundle("a");
			key = "a";
		}
		
		if(charCodeBundle.getBoolean(key))
		{
			charCode = 0;
		}
		else if(charCodeBundle.getBooleanArray(key) != null)
		{
			charCode = 1;
		}
		else if(charCodeBundle.getByte(key) != 0)
		{
			charCode = 2;
		}
		else if(charCodeBundle.getByteArray(key) != null)
		{
			charCode = 3;
		}
		else if(charCodeBundle.getChar(key) != 0)
		{
			charCode = 4;
		}
		else if(charCodeBundle.getCharArray(key) != null)
		{
			charCode = 5;
		}
		else if(charCodeBundle.getCharSequence(key) != null)
		{
			charCode = 6;
		}
		
		// Note that only one of CharSequence Arrays and Strings can be used
		// since they will both cause getString() and getCharSequenceArray()
		// to return a non-null value
		else if(charCodeBundle.getCharSequenceArray(key) != null)
		{
			charCode = 7;
		}
		
		/*
		else if(charCodeBundle.getString(key) != null)
		{
			c = 'z';
		}
		 */
		
		// TODO: Figure out if there is a better way to do this (maybe request
		// specific ArrayList types to allow encoding schemas to vary which arraylist 
		// type is is used to enable the schemas to have more control over their 
		// signatures
		//
		// All ArrayList types are treated as being equivalent by the Bundle 
		// get-by-type methods and as such on the CharSequence ArrayList type 
		// is used for encoding data
		else if(charCodeBundle.getCharSequenceArrayList(key) != null)
		{
			charCode = 8;
		}
		
		/*
		 
		else if(charCodeBundle.getIntegerArrayList(key) != null)
		{
			charCode = 15;
		}
		
		
		else if(charCodeBundle.getParcelableArrayList(key) != null)
		{
			charCode = 19;
		}
		 */
		
		else if(charCodeBundle.getDouble(key) != 0.0)
		{
			charCode = 9;
		}
		else if(charCodeBundle.getDoubleArray(key) != null)
		{
			charCode = 10;
		}
		else if(charCodeBundle.getFloat(key) != 0.0)
		{
			charCode = 11;
		}
		else if(charCodeBundle.getFloatArray(key) != null)
		{
			charCode = 12;
		}
		else if(charCodeBundle.getInt(key) != 0)
		{
			charCode = 13;
		}
		else if(charCodeBundle.getIntArray(key) != null)
		{
			charCode = 14;
		}
		else if(charCodeBundle.getLong(key) != 0)
		{
			charCode = 15;
		}
		else if(charCodeBundle.getLongArray(key) != null)
		{
			charCode = 16;
		}
		else if(charCodeBundle.getParcelableArray(key) != null)
		{
			charCode = 17;
		}
		
		// Note that getSerializable() returns non-null if either the short 
		// or short array is set
		/*
		else if(charCodeBundle.getSerializable(key) != null)
		{
			c = 'v';
		}
		*/
		
		// Note: getShort() and getShortArray() are distinct, but both will 
		// cause getSerializable() to return a non-null value
		else if(dataPacket.getShort(key) != 0)
		{
			charCode = 18; 
		}
		else if(dataPacket.getShortArray(key) != null)
		{
			charCode = 19;
		}
		else if(dataPacket.getSparseParcelableArray(key) != null)
		{
			charCode = 20;
		}
		else 
		{
			// TODO: Remove
			Log.d("CovertChannel", "HERE!");
			
			throw new IllegalArgumentException("In EncodingUtils.decodeChar():" +
				" No value could be decoded for the given key (" + key +
				")");
		}

		// TODO: figure out how to use this one (or if it is even possible)
		/*
		else if(dataPacket.getParcelable(key) != null)
		{
			c = 's';
		}
		*/
		
		// Applies the effects of the expansion code
		charCode += (charSetSize * expansionCode);

		return charCode;
	}
	
    /**
     * Method for reading data from a text resource file in the res/raw/ folder
     * based on code found at
     * http://stackoverflow.com/questions/4087674/android-read-text-raw-resource-file
     */
    public static String readRawTextFile(Context ctx, int resId, int charsToRead)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);
        
        try
        {
	        inputStream.reset();
        } 
        catch (IOException e1)
        {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        int i, charsRead = 0;
        try 
        {
        	i = inputStream.read();
        	while (i != -1 && charsRead < charsToRead)
        	{
        		outputStream.write(i);
            	i = inputStream.read();
        		++charsRead;
            }
        	
        	inputStream.close();
        }
        catch (IOException e)
        {
        	return null;
        }
          
        String retString = outputStream.toString();
        
        try
        {
	        inputStream.close();
        } 
        catch (IOException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        
        try
        {
	        outputStream.close();
        } 
        catch (IOException e)
        {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        
        return retString;
    }
}
