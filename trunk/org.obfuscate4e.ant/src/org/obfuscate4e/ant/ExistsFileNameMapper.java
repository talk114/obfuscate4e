/*
 * $Id:$
 */
package org.obfuscate4e.ant;

import java.io.File;

import org.apache.tools.ant.util.FileNameMapper;


public class ExistsFileNameMapper implements FileNameMapper {

    public String[] mapFileName(String sourceFileName) {
        File file = new File(sourceFileName);
        if (file.exists()) {
            return new String[] {sourceFileName};
        }
        return new String[0];
    }

    public void setFrom(String from) {
    }

    public void setTo(String to) {
    }

}
