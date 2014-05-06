package com.example.intentutils.lib;

/**
 * Generates an alphabetical sequence of strings. Based on the accepted
 * answer for http://stackoverflow.com/questions/8710719/generating-an-alphabetic-sequence-in-java
 * 
 * @author Timothy Heard
 */
public class AlphabeticalKeySequence
{
	private int now;
    private char[] vs;
    
    public AlphabeticalKeySequence()
    {
        vs = new char['Z' - 'A' + 1];
        for(char i='A'; i<='Z';i++) vs[i - 'A'] = i;
    }

    private StringBuilder alpha(int i)
    {
        assert i > 0;
        char r = vs[--i % vs.length];
        int n = i / vs.length;
        return n == 0 ? new StringBuilder().append(r) : alpha(n).append(r);
    }

    public String next() 
    {
        return alpha(++now).toString();
    }
}
