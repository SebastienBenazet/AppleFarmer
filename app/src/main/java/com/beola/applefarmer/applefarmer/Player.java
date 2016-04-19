package com.beola.applefarmer.applefarmer;


public class Player {

    private String name;
    private Float appleNumber = 0.0f, applePerSecond = 0.0f, clickValue = 1.0f;
    private Integer clickNumber = 0;

    public Player(){}

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Float getAppleNumber(){
        return this.appleNumber;
    }

    public void setAppleNumber(Float appleNumber){
        this.appleNumber = appleNumber;
    }

    public Float getApplePerSecond(){
        return this.applePerSecond;
    }

    public void setApplePerSecond(Float applePerSecond){
        this.applePerSecond = applePerSecond;
    }

    public Float getClickValue() {
        return this.clickValue;
    }

    public void setClickValue(Float clickValue) {
        this.clickValue = clickValue;
    }

    public Integer getClickNumber(){
        return this.clickNumber;
    }

    public void setClickNumber(Integer clickNumber){
        this.clickNumber = clickNumber;
    }
}
