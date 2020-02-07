package autonomous;

import java.io.*;
import java.util.Scanner;
import java.util.regex.Pattern;

public class AutoPath {

    private File file;

    private Scanner reader;

    private String raw;

    public AutoPath(String filePath) {
        
        // creates a blank raw string for later safety
        raw = "";

        // attempts to find the file and create a scanner to read it,
        try {

            file = new File(filePath);

            reader = new Scanner(file); 

            while(reader.hasNextLine()) {

                raw += reader.nextLine();

            }

        } catch (Exception e) {

            System.err.print("File not found: " + filePath);

        }        

    }

    /*
    public Command[] getCommandQueue() {

        String[] matches = raw.split("\\[[^\\[\\]]*?\\]");

    }
    */

}