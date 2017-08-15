package com.jmr.entity; 
import java.util.Date; 
   
public class User {
    


     private int id;
     private String account;
     private String password;
     private String name;
     private int status;
     private int cuserid;
     private Date createdatetime;
     private int uuserid;
     private Date lastupdatetime;
   
     public void setId(int id){
          this.id = id;
     }
     
     public void setAccount(String account){
          this.account = account;
     }
     
     public void setPassword(String password){
          this.password = password;
     }
     
     public void setName(String name){
          this.name = name;
     }
     
     public void setStatus(int status){
          this.status = status;
     }
     
     public void setCuserid(int cuserid){
          this.cuserid = cuserid;
     }
     
     public void setCreatedatetime(Date createdatetime){
          this.createdatetime = createdatetime;
     }
     
     public void setUuserid(int uuserid){
          this.uuserid = uuserid;
     }
     
     public void setLastupdatetime(Date lastupdatetime){
          this.lastupdatetime = lastupdatetime;
     }
     
}
