package com.example.williamstultscourseguide.data;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface AssessmentDao {

    @Query("SELECT * FROM assessment_table ORDER BY assessment_id")
    List<Assessment> getAssessmentList();

    @Query("SELECT * FROM assessment_table WHERE course_id_fk = :courseId AND assessment_id = :assessmentId")
    Assessment getAssessment(int courseId, int assessmentId);

    @Query("SELECT * FROM assessment_table")
    List<Assessment> getAllAssessments();

    @Insert
    void insertAssessment(Assessment assessment);

    @Insert
    void insertAll(Assessment... assessment);

    @Update
    void updateTerm(Assessment assessment);

    @Update
    void deleteTerm(Assessment assessment);

    @Query("DELETE FROM assessment_table")
    public void nukeAssessmentTable();

}
