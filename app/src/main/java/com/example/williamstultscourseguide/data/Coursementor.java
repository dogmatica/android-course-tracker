package com.example.williamstultscourseguide.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "coursementor_table")
public class Coursementor {
    @PrimaryKey(autoGenerate = true)
    private int mentor_id;
    @ColumnInfo(name = "mentor_name")
    private String mentor_name;
    @ColumnInfo(name = "mentor_phone")
    private String mentor_phone;
    @ColumnInfo(name = "mentor_email")
    private String mentor_email;

    public int getMentor_id() { return mentor_id;}

    public void setMentor_id(int mentor_id)  {this.mentor_id = mentor_id;}

    public String getMentor_name() { return mentor_name;}

    public void setMentor_name(String mentor_name)  {this.mentor_name = mentor_name;}

    public String getMentor_phone() { return mentor_phone;}

    public void setMentor_phone(String mentor_phone)  {this.mentor_phone = mentor_phone;}

    public String getMentor_email() { return mentor_email;}

    public void setMentor_email(String mentor_email)  {this.mentor_email = mentor_email;}

}
