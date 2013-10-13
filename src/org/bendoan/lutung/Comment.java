package org.bendoan.lutung;

import java.util.HashMap;
import java.util.ArrayList;
import java.lang.StringBuilder;

class Comment{
    String signature;
    HashMap<String, String> params; //param type:name
    String returnType;
    String name;

    public Comment(String line){
        this.signature = line;
        this.params = new HashMap<String, String>();
        parseSignature();
    }

    private void parseSignature(){
        String[] trashKeywords = {
                                    "public",
                                    "private",
                                    "protected",
                                    "abstract",
                                    "static",
                                    "final",
                                    "strictfp",
                                    "transient",
                                    "volatile",
                                    "synchronized",
                                    "native",
                                    "threadsafe"
                                };

        ArrayList<String> splitSignature = new ArrayList<>();
        this.signature = this.signature.replace("(", " ");
        this.signature = this.signature.replace(")", "");

        for(String s : this.signature.trim().split(" "))
            if (!arrayContains(s, trashKeywords))
                splitSignature.add(removeTrashChars(s));

        this.returnType = splitSignature.get(0);
        this.name = splitSignature.get(1);
        for (int i = 3; i < splitSignature.size(); i+=2)
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

    public String toString(){
        String returnString = "";
        returnString += "/**\n";
        returnString += " * Name\t\t" + this.name + "\n";
        returnString += " * Params\t" + this.params + "\n";
        if (this.returnType != "void")
            returnString += " * @return\t" + this.returnType + "\n";
        returnString += "**/\n";
        return returnString;
    }
}
