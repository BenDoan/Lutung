import java.util.HashMap;

class Comment{
    HashMap<String, String> params;

    public Comment(){
        this.params = new HashMap<String, String>();
    }

    private void addParam(name, type){
        this.params.put(name, type);
    }

    public String toString(){
        String returnString;
        returnString += "/**";
        returnString += "**/";
        return returnString;
    }
}
