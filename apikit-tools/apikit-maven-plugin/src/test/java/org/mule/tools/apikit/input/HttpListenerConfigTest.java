/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.tools.apikit.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mule.tools.apikit.Helper.countOccurences;

import org.mule.tools.apikit.Scaffolder;
import org.mule.tools.apikit.misc.FileListUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class HttpListenerConfigTest
{
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private FileListUtils fileListUtils = new FileListUtils();


    @Before
    public void setUp() {
        folder.newFolder("scaffolder");
        folder.newFolder("scaffolder-existing");
    }

    //@Test
    //public void parseValidFile() throws Exception
    //{
    //    File muleXmlOut = folder.newFolder("scaffolder");
    //    List<File> xmls = Arrays.asList(getFile("scaffolder/multipleMimeTypes.xml"));
    //
    //    InputStream stream = fileStreamEntry.getValue();
    //    File file = fileStreamEntry.getKey();
    //}
    //
    //
    //@Test
    //public void testAlreadyExistsGenerate() throws Exception {
    //    List<File> yamls = Arrays.asList(getFile("scaffolder-existing/simple.yaml"));
    //    File xmlFile = getFile("scaffolder-existing/simple.xml");
    //    List<File> xmls = Arrays.asList(xmlFile);
    //    File muleXmlOut = folder.newFolder("mule-xml-out");
    //
    //    Scaffolder scaffolder = createScaffolder(yamls, xmls, muleXmlOut);
    //    scaffolder.run();
    //
    //    assertTrue(xmlFile.exists());
    //    String s = IOUtils.toString(new FileInputStream(xmlFile));
    //
    //    assertEquals(1, countOccurences(s, "httpListenerConfig"));
    //    assertEquals(1, countOccurences(s, "post:/pet"));
    //}


    private File getFile(String s) throws  Exception {
        File file = folder.newFile(s);
        file.createNewFile();
        InputStream resourceAsStream = HttpListenerConfigTest.class.getClassLoader().getResourceAsStream(s);
        IOUtils.copy(resourceAsStream,
                     new FileOutputStream(file));
        return file;
    }
}
