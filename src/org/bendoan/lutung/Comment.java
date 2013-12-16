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
        String[][] shortcuts = {
            {"toString", "Returns the string representation of the object"},
            {"equals", "Tests equality between this object and another"},
            {"compareTo", "Compares this object to another"}
        };
        for (String[] shortcut : shortcuts)
            if (name.equals(shortcut[0]))
                return shortcut[1];

        String[] splitStr = name.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])"); //splits camelCase
        String desc = "";
        boolean isAction = true;

        String[][] idioms = {
            {"is", "Checks if is"},
            {"set", "Sets the"},
            {"get", "Gets the"},
            {"return", "Returns the"},
            {"gen", "Generates the"},
            {"parse", "Parses the"},
            {"remove", "Removes the"},
            {"print", "Prints the"},
            {"display", "Prints the"},
            {"process", "Processes the"}
        };
        for (String[] idiom : idioms){
            if (splitStr[0].equals(idiom[0])){
                desc = idiom[1] + " ";
            }
        }

        //add rest of words to description
        if (desc == ""){// does not match any idiom
            desc = "the ";
            for(String s : Arrays.copyOfRange(splitStr, 1, splitStr.length)){
                desc = s.toLowerCase() + " ";
            }
        }else{
            for(String s : splitStr){
                desc += s.toLowerCase() + " ";
            }
        }

        //expands abbreviations
        String[][] abbrs = {
            {"char", "character"},
            {"chars", "characters"},
            {"str", "string"},
            {"min", "minimum"},
            {"max", "maximum"},
        };
        for (String[] abbr : abbrs)
            desc.replace(abbr[0], abbr[1]);

        return desc;
    }

    public String toString(){
        String spaces = getBeginSpace();
        String returnString = "";

        returnString += spaces + "/**\n";

        returnString += spaces + String.format(" * %s\n", getNameDescription(this.name));
        returnString += spaces + " *\n";

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
