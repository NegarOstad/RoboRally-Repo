/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class reading strings from resources and arbitrary input streams.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class IOUtil {

    /**
     * Reads a string from some InputStream. The solution is based
     * on google's Guava and a solution from Baeldung:
     * https://www.baeldung.com/convert-input-stream-to-string#guava
     *
     * @param inputStream the input stream
     * @return the string of the input stream
     */
    public static String readString(InputStream inputStream) {

        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return inputStream;
            }
        };
        try {
            return byteSource.asCharSource(Charsets.UTF_8).read();
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Returns a string from a resource of the project. This method is implemented
     * in such a way that resource can be read when the project is deployed in
     * a jar file.
     *
     * @param relativeResourcePath the relative path to the resource
     * @return the string contents of the resource
     */
    public static String readResource(String relativeResourcePath) {
        ClassLoader classLoader = IOUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(relativeResourcePath);
        return IOUtil.readString(inputStream);
    }

// overright
    public static List<String> readResources(String relativeResourcePath) {
        List<String> lines = new ArrayList<>();

        try {
            ClassLoader classLoader = IOUtil.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(relativeResourcePath);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static List<String> readResourcee(String relativeResourcePath) {
        List<String> lines = new ArrayList<>();

        ClassLoader classLoader = IOUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(relativeResourcePath);
        if (inputStream != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return lines;
}}


