package autonomous;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autonomous.Command.CommandType;

public class AutoPath {

    private File file;

    private Scanner reader;

    private String raw;

    // the base path for the json files will be home/lvuser/deploy/autopaths
    // then add /filename.json to the end
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

    private double[][] getDoubleArray() {

        // this next line is black magic, don't touch
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*?\\]");
        Matcher matches = pattern.matcher(raw);

        int matchCount = 0;
        while (matches.find()) {

            matchCount++;

        }

        matches.reset();

        double[][] commandValArray = new double[matchCount][];
        
        int index = 0;

        while (matches.find()) {

            String group = matches.group();
            String[] splitCommands = group.substring(1, group.length() - 1).split(",");

            double[] commandValues = new double[3];
            commandValues[0] = Double.parseDouble(splitCommands[0]);
            commandValues[1] = Double.parseDouble(splitCommands[1]);
            commandValues[2] = Double.parseDouble(splitCommands[2]);

            commandValArray[index] = commandValues;

            index++;

        }

        return commandValArray;

    }

    public ArrayList<Command> getCommandQueue() {

        double[][] commandVals = this.getDoubleArray();

        ArrayList<Command> commandQueue = new ArrayList<Command>();

        for(int i = 0; i < commandVals.length; i++) {

            Command.CommandType type = (commandVals[i][0] == 0) ? CommandType.ROTATE : CommandType.MOVE;

            commandQueue.add(new Command(type, commandVals[i][1], commandVals[i][2]));

        }

        return commandQueue;

    }

}