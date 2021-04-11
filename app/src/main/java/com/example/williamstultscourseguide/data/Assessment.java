package com.example.williamstultscourseguide.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        tableName = "assessment_table",
        foreignKeys = @ForeignKey(
                entity = Course.class,
                parentColumns = "course_id",
                childColumns = "course_id_fk",
                onDelete = CASCADE
                ),
        indices = {@Index(value = "assessment_title", unique = true), @Index(value = "course_id_fk")}
)

public class Assessment {
    @PrimaryKey(autoGenerate = true)
    private int assessment_id;
    @ColumnInfo(name = "course_id_fk")
    private int course_id_fk;
    @ColumnInfo(name = "assessment_title")
    private String assessment_title;
    @ColumnInfo(name = "assessment_type")
    private String assessment_type;
    @ColumnInfo(name = "assessment_due")
    private Date assessment_due;
    @ColumnInfo(name = "assessment_goal")
    private Date assessment_goal;
    @ColumnInfo(name = "assessment_status")
    private String assessment_status;

    public int getAssessment_id() { return assessment_id;}

    public void setAssessment_id(int assessment_id)  {this.assessment_id = assessment_id;}

    public int getCourse_id_fk() { return course_id_fk;}

    public void setCourse_id_fk(int course_id_fk)  {this.course_id_fk = course_id_fk;}

    public String getAssessment_title() { return assessment_title;}

    public void setAssessment_title(String assessment_title)  {this.assessment_title = assessment_title;}

    public String getAssessment_type() { return assessment_type;}

    public void setAssessment_type(String assessment_type)  {this.assessment_type = assessment_type;}

    public Date getAssessment_due() { return assessment_due;}

    public void setAssessment_due(Date assessment_due)  {this.assessment_due = assessment_due;}

    public Date getAssessment_goal() { return assessment_goal;}

    public void setAssessment_goal(Date assessment_goal)  {this.assessment_goal = assessment_goal;}

    public String getAssessment_status() { return assessment_status; }

    public void setAssessment_status(String assessment_status) {this.assessment_status = assessment_status;}
}
