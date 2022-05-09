package com.example.nirapotta;
public class DataTemp {

    private int id;
    private String name;
    private String phone;
    private String uname;

    public DataTemp(String n, String d){
        name= n;
        phone= d;
    }

    public DataTemp(String un){
        uname= un;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setphone(String phone) {
        this.phone = phone;
    }

    public String getphone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
