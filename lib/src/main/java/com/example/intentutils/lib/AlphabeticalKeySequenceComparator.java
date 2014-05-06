package com.example.intentutils.lib;

import java.util.Comparator;

/**
 * Custom {@link java.util.Comparator} for comparing the String keys returned by the
 * {@link AlphabeticalKeySequence}. 
 * 
 * @author Timothy Heard
 */
public class AlphabeticalKeySequenceComparator
	implements Comparator<String>
{
	/**
	 * For this comparator, "a" is immediately followed by "b" and so on, 
	 * with "z" being followed by "aa". {@code null} is comparatively less
	 * than all other {@link String}s (i.e. it comes before "a").
	 */
	@Override
    public int compare(String s1, String s2)
    {
		int retVal;
		if(s1 == null && s2 != null)
		{
			retVal = -1;
		}
		else if(s2 == null && s1 != null)
		{
			retVal = 1;
		}
		else if(s1.equals(s2))
		{
			retVal = 0;
		}
		else if(s1.length() < s2.length())
		{
			retVal = -1;
		}
		else if(s1.length() > s2.length())
		{
			retVal = 1;
		}
		else
		{
			retVal = s1.compareTo(s2);
		}
		
		
		return retVal;
    }

}
