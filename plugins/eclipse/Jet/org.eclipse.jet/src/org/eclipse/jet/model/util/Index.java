package org.eclipse.jet.model.util;

public class Index {
  
    protected char splitChar;
    protected Index right;
    protected String content;
    
    public Index(String str) {
      init();
      parser(str);
    }
    
    protected void init(){
      splitChar = '.';
    }
    
    public void parser(String str){
      str = str.trim();
      int index = str.indexOf(splitChar);
      if(index > -1){
        right = new Index(str.substring(index + 1, str.length()));
        content = str.substring(0, index);
      }else{
        content = str;
      }
    }
    
    public String getContent()
    {
      return content;
    }

    public void setContent(String content)
    {
      this.content = content;
    }

    public Index getRight()
    {
      return right;
    }

    public String getLastRight(){
      return getLastRight(this);
    }
    
    private String getLastRight(Index index){
      Index right = index.getRight();
      String content = index.getContent();
      if(right == null){
        return content;
      }else{
        return getLastRight(right);
      }
    }
    
}
