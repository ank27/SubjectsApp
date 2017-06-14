package com.subjectappl.Models;


import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Subject extends RealmObject{
    @PrimaryKey
    public int id;
    public String subject_title;
    public byte[] subject_image;
    public String subject_description;

    public Subject(){}

    public Subject(int id,String subject_title,String subject_description, byte[] subject_image){
        this.id=id;
        this.subject_title=subject_title;
        this.subject_image=subject_image;
        this.subject_description=subject_description;
    }

}
