/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package de.uniluebeck.compilatex.utilities;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class RegEx {
    
    /**
     * extract the lines of the compilation log which indicate errors 
     * 
     * @param log the logfile
     * @return list of all lines containing an error
     */
    public static ArrayList<String> findErrors(String log){
        return findLines(log, "(?m)^.*rror:.*$");
    }
    
    /**
     * extract the lines of the compilation log which indicate warnings 
     * 
     * @param log the logfile
     * @return list of all lines containing a warning
     */
    public static ArrayList<String> findWarnings(String log){
        return findLines(log, "(?m)^.*arning:.*$");
    }
    
    /**
     * extract the lines containing a certain regex 
     * 
     * @param log the logfile
     * @param regex regular expression
     * @return list of all lines containing a warning
     */
    public static ArrayList<String> findLines(String log, String regex){
        ArrayList<String> list = new ArrayList<>();
        Matcher m = Pattern.compile(regex).matcher(log);
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }
}
