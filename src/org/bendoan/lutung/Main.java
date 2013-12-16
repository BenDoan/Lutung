package org.bendoan.lutung;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.AbstractCollection;

import java.io.*;

class Main{
    public static void main(String[] args){
        if (args.length > 0){
            for (String fileName : args){
                String file = readFile(fileName);

                String[] splitFile = file.split("\n");
                for (int commentIndex : getMethodIndexes(splitFile)){  //method index order is reversed
                   Comment c = new Comment(splitFile[commentIndex]);
                   writeComment(fileName, c.toString(), commentIndex);
                }
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
        Collections.reverse(methodIndexes);
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

    private static void writeComment(String name, String comment, int index)
    {
        File f = new File(name);
        String  writeString = "";
        if(f.exists())
            writeString = readFile(name);

        ArrayList<String> lst = new ArrayList(Arrays.asList(writeString.split("\n")));

        lst.addAll(index, Arrays.asList(comment.split("\n")));
        writeString = join(lst, "\n");

        try{
            BufferedWriter output = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
            output.write(writeString);
            output.close();
        }catch (IOException e){
        }
    }

    //from: http://stackoverflow.com/questions/794248/a-method-to-reverse-effect-of-java-string-split
    private static String join(AbstractCollection<String> s, String delimiter) {
        if (s == null || s.isEmpty()) return "";
        Iterator<String> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next());
        while(iter.hasNext())
            builder.append(delimiter).append(iter.next());
        return builder.toString();
    }
}
