/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.shell;

import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import de.uniluebeck.compilatex.utilities.RegEx;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class Shell {

    private static final Logger LOGGER = Logger.getLogger(Shell.class.getName());

    /**
     * compile project
     * @param latexEnvironment the latex compiler/environment that should be used
     * @param jobDirectory path to the file that should be compiled
     * @param filename name of the main project file that will be compiled
     * @throws IOException in case command execution does fail
     */
    public static void compile(LatexEnvironment latexEnvironment, String jobDirectory, String filename) throws IOException {
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);
        
        if (isInstalled(latexEnvironment)) {

            /* create new latex executor */
            DefaultExecutor executor = new DefaultExecutor();
            executor.setStreamHandler(streamHandler);
            
            /* create result handler */
            DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();
            
            /* cd to project directory */
            executor.setWorkingDirectory(new File(jobDirectory));
            
            /* build command */
            CommandLine commandLine = new CommandLine(latexEnvironment.name().toLowerCase());
            commandLine.addArgument("-interaction=batchmode");
            Map map = new HashMap();
            map.put("file", filename);
            commandLine.addArgument("${file}");
            commandLine.setSubstitutionMap(map);

            /* execute command */
            LOGGER.log(Level.INFO, "execute command {0} in directory " + jobDirectory, commandLine.toString());
            executor.execute(commandLine, resultHandler);

            /* wait until compilation has finished */
            LOGGER.info("WAIT FOR RESULT");
            while(!resultHandler.hasResult()){
                proceedOnError(outputStream);
            }
            LOGGER.info("GOT RESULT");
        } else {
            LOGGER.log(Level.SEVERE, "latex environment {0} could not be found, not installed?", latexEnvironment.name());
        }
    }
    
    /**
     * extract the lines of the compilation log which indicate errors 
     * @param byteArrayOutputStream stream with the process' console output
     * @return list containg all errors if there were some during compilation
     */
    public static ArrayList<String> findErrorsInLog(ByteArrayOutputStream byteArrayOutputStream){
        String log = byteArrayOutputStream.toString();
        return RegEx.findErrors(log);
    }
    
    /**
     * watch compilation log for messages that indicate an input
     * (return key) must be sent to the console to continue
     * @param byteArrayOutputStream stream with the process' console output
     */
    public static void proceedOnError(ByteArrayOutputStream byteArrayOutputStream){
        String log = byteArrayOutputStream.toString();
        Matcher m = Pattern.compile("(?m)^.*<RETURN> to proceed.*$").matcher(log);
        while (m.find()) {
            Logger.getLogger(Shell.class.getName()).log(Level.INFO, "line containing return = {0}", m.group());
            try {
                Logger.getLogger(Shell.class.getName()).log(Level.INFO, "try writing enter to output to continue");
                BufferedWriter bos = new BufferedWriter(new OutputStreamWriter(byteArrayOutputStream));
                bos.write(KeyEvent.VK_ENTER);
            } catch (IOException ex) {
                Logger.getLogger(Shell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * find out if a given latex environment is installed on the (linux) system
     * @param latexEnvironment latex environment that will be looked up
     * @return true if latex environment is installed
     */
    public static boolean isInstalled(LatexEnvironment latexEnvironment) {
        return isInstalled(latexEnvironment.getName());
    }

    /**
     * find out if a given program/package/application is installed on the (linux) system
     * @param applicationName name of the application that will be looked up
     * @return true if application is installed
     */
    public static boolean isInstalled(String applicationName) {

        /* build command */
        CommandLine commandLine = new CommandLine("which");
        commandLine.addArgument(applicationName);

        /* create stream handler */
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PumpStreamHandler streamHandler = new PumpStreamHandler(outputStream);

        /* create executor and set stream handler */
        DefaultExecutor executor = new DefaultExecutor();
        executor.setStreamHandler(streamHandler);

        /* valid exit codes are 0 and 1 (in case application is not installed) */
        executor.setExitValues(new int[]{0, 1});

        /* execute command */
        try {
            executor.execute(commandLine);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "command \"" + commandLine.toString() + "\" could not be executed", ex);
        }

        /* application is installed if output returns it's name */
        return outputStream.toString().contains(applicationName);
    }
}
