package com.example.managerapp;

public class SetOptionDestination {
    int position;
    String code;

    public SetOptionDestination(int position){
        this.position = position;
    }
    public SetOptionDestination(String code){
        this.code = code;
    }

    public String getOption(){
        switch (position){
            case 1:
                return "EATING";
            case 2:
                return "ACCOMMODATION";
            case 3:
                return "OTHER";
            default:
                return "OTHER";
        }
    }

    public int getOptionByCode(){
        switch (code){
            case "EATING":
                return 1;
            case "ACCOMMODATION":
                return 2;
            case "OTHER":
                return 3;
            default:
                return 3;
        }
    }

}
