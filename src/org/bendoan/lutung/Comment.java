package org.bendoan.lutung;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Arrays;

class Comment{
    String signature;
    HashMap<String, String> params; //type:name
    String returnType;
    String name;

    public Comment(String line){
        this.signature = line;
        this.params = parseSignature();
    }

    private HashMap<String, String> parseSignature(){
        HashMap<String, String> returnParams = new HashMap<String, String>();
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
            returnParams.put(splitSignature.get(i-1), splitSignature.get(i));

        return returnParams;
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

    private String getBeginSpace(){
        String spaces = "";
        for (char c : this.signature.toCharArray()){
            if (c == ' ' || c == '\t')
                spaces += c;
            else
                break;
        }
        return spaces;
    }

    private String getNameDescription(String name){
        if (name.equals("toString"))
            return "Returns the string representation of the object";
        if (name.equals("equals"))
            return "Tests equality between this object and another";
        if (name.equals("compareTo"))
            return "Compares this object to another";

        String[] splitStr = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"); //splits camelCase
        String desc = "";
        boolean isAction = true;

        if (splitStr[0].equals("is")){
            desc = "Is ";
        }else if (splitStr[0].equals("set")){
            desc = "Sets the ";
        }else if (splitStr[0].equals("get")){
            desc = "Gets the ";
        }else if (splitStr[0].equals("return")){
            desc = "Returns the ";
        }else if (splitStr[0].equals("gen")){
            desc = "Generates the ";
        }else if (splitStr[0].equals("parse")){
            desc = "Parses the ";
        }else if (splitStr[0].equals("remove")){
            desc = "Removes the ";
        }else{
            desc = "the ";
            isAction = false;
        }

        if (isAction){
            for(String s : Arrays.copyOfRange(splitStr, 1, splitStr.length)){
                desc += s.toLowerCase() + " ";
            }
        }else{
            for(String s : splitStr){
                desc += s.toLowerCase() + " ";
            }
        }

        return desc;
    }

    public String toString(){
        String spaces = getBeginSpace();
        String returnString = "";

        returnString += spaces + "/**\n";

        returnString += spaces + String.format(" * %s\n", getNameDescription(this.name));

        //add params
        for (String key : this.params.keySet()){
                returnString += spaces + String.format(" * %-7s %-5s %-5s\n", "@param", this.params.get(key), getNameDescription(this.params.get(key)));
        }

        if (!this.returnType.equals("void"))
            returnString += spaces + String.format(" * %-7s %-5s \n", "@return", this.returnType);

        returnString += spaces + "**/\n";
        return returnString;
    }
}
