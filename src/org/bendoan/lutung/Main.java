package org.bendoan.lutung;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

class Main{
    public static void main(String args[]){
        if (args.length > 0){
            String fileName = args[0];
            String file = readFile(fileName);
            String[] splitFile = file.split("\n");
            for (int commentIndex : getMethodIndexes(splitFile)){
               Comment c = new Comment(splitFile[commentIndex]);
            }
        }else{
            System.out.println("No argument given");
        }
    }

    private static ArrayList<Integer> getMethodIndexes(String[] splitFile){
        ArrayList<Integer> methodIndexes = new ArrayList<>();
        for (int i = 0; i < splitFile.length; i++){
            Pattern pattern = Pattern.compile("((public|private|protected|static|final|native|synchronized|abstract|threadsafe|transient)+\\s)+[\\$_\\w\\<\\>\\[\\]]*\\s+[\\$_\\w]+\\([^\\)]*\\)?\\s*\\{?[^\\}]*\\}?");
            Matcher matcher = pattern.matcher(splitFile[i].trim());

            if(matcher.matches())
                methodIndexes.add(i);
        }
        System.out.println(methodIndexes);
        return methodIndexes;
    }

    private static String readFile(String name){
        String returnString = "";
        BufferedReader br = null;

        try {
            String currentLine;
            br = new BufferedReader(new FileReader(name));

            while ((currentLine = br.readLine()) != null){
                returnString += currentLine + "\n";
            }
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (br != null){
                try{
                    br.close();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        }

        return returnString;
    }
}
