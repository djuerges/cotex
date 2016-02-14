/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uniluebeck.compilatex.test.shell;

import de.uniluebeck.compilatex.shell.Shell;
import de.uniluebeck.compilatex.utilities.LatexEnvironment;
import de.uniluebeck.compilatex.test.data.TestDataProvider;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Daniel Jürges <djuerges@googlemail.com>
 */
public class ShellTest {

    String TMP_DIR;
    String FILENAME;

    @AfterMethod
    public void tearDownClass() throws Exception {
        ArrayList<String> endings = new ArrayList<>(
                Arrays.asList(".aux", ".out", ".log", ".nlo", ".toc", ".lof", ".idx"));

        for (String ending : endings) {
            Files.deleteIfExists(Paths.get(TMP_DIR, FILENAME + ending));
        }
    }

    @Test(description = "test compilation for valid and erroneous tex files",
            dataProviderClass = TestDataProvider.class, dataProvider = "tex-files")
    public void testCompile(File file) {
        TMP_DIR = file.getParent();
        FILENAME = file.getName().split("\\.")[0];

        try {
            Shell.compile(LatexEnvironment.PDFLATEX, file.getParent(), file.getName());
        } catch (IOException ex) {
            Logger.getLogger(ShellTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        assertTrue(FILENAME + ".aux does not exist!", Files.exists(Paths.get(TMP_DIR, FILENAME + ".aux")));
        assertTrue(FILENAME + ".log does not exist!", Files.exists(Paths.get(TMP_DIR, FILENAME + ".log")));
        assertTrue(FILENAME + ".pdf does not exist!", Files.exists(Paths.get(TMP_DIR, FILENAME + ".pdf")));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "fake-application-names")
    public void testNonInstalledApplication(String applicationName) {
        assertFalse("found match although it's not a valid application name!", Shell.isInstalled(applicationName));
    }

    @Test(dataProviderClass = TestDataProvider.class, dataProvider = "latex-environments")
    public void testInstalledLatexEnviroments(LatexEnvironment environment) {
        assertTrue("package isn't found although installed!", Shell.isInstalled(environment));
    }
}
