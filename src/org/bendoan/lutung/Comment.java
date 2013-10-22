package org.bendoan.lutung;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.StringBuilder;

class Comment{
    String signature;
    HashMap<String, String> params; //type:name
    String returnType;
    String name;

    public Comment(String line){
        this.signature = line;
        this.params = new HashMap<String, String>();
        parseSignature();
    }

    private void parseSignature(){
        String[] trashKeywords = {"public", "private", "protected", "abstract", "static", "final", "strictfp", "transient", "volatile", "synchronized", "native", "threadsafe"};

        this.signature = this.signature.replace("(", " "); //replace with space so it splits
        this.signature = this.signature.replace(")", "");

        ArrayList<String> splitSignature = new ArrayList<>();
        for(String s : this.signature.trim().split(" ")) //add split parts to list
            if (!arrayContains(s, trashKeywords))
                splitSignature.add(removeTrashChars(s));

        this.returnType = splitSignature.get(0);
        this.name = splitSignature.get(1);

        for (int i = 3; i < splitSignature.size(); i+=2) //add params to param list
            this.params.put(splitSignature.get(i-1), splitSignature.get(i));
    }

    private String removeTrashChars(String str){
        String returnString = "";
        for (char c : str.toCharArray()){
            if(!(c == '{' || c == ','))
                returnString += c;
        }
        return returnString;
    }

    private boolean arrayContains(String str, String[] lst){
        for (String s : lst)
            if (str.contains(s))
                return true;
        return false;
    }

    private int getLongestWordLength(){
        int longestLength = returnType.length();
        return 0;
    }

    public String toString(){
        int maxWidth = 5;

        String returnString = "";
        returnString += "/**\n";
        returnString += String.format(" * %-7s %s\n", "Name", this.name);

        //get params
        for (String key : this.params.keySet()){
                returnString += String.format(" * %-7s %-5s %-5s\n", "@param", key, this.params.get(key));
        }

        if (!this.returnType.equals("void"))
            returnString += String.format(" * %-7s %-5s \n", "@return", this.returnType);

        returnString += "**/\n";
        return returnString;
    }
}
