/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.utilities;

import de.uniluebeck.compilatex.shell.Shell;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public enum LatexEnvironment {

    PDFLATEX, LUATEX, XETEX;

    public String getName() {
        return this.name().toLowerCase();
    }

    public static LatexEnvironment valueOfName(String name) {
        for (LatexEnvironment value : values()) {
            if (value.getName().equals(name.toLowerCase())) {
                return value;
            }
        }
        throw new IllegalArgumentException("No enum const " + LatexEnvironment.class + "@name." + name);
    }
    
    public static List<String> names(){
        List<String> list = new ArrayList<>();
        for(LatexEnvironment env : values()){
            list.add(env.getName());
        }
        return list;
    }
    
    public static List<String> installed(){
        List<String> list = new ArrayList<>();
        for(LatexEnvironment env : values()){
            /* exposed only installed latex environments */
            if(Shell.isInstalled(env.getName())){
                list.add(env.getName());
            }
        }
        return list; //.toArray(new String[list.size()]);
    }
}
