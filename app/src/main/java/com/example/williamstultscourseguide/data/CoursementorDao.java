package com.example.williamstultscourseguide.data;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface CoursementorDao {

    @Query("SELECT * FROM coursementor_table ORDER BY coursementor_id")
    List<Coursementor> getCoursementorList();

    @Query("SELECT * FROM coursementor_table WHERE course_id_fk = :courseId AND coursementor_id = :coursementorId")
    Coursementor getCoursementor(int courseId, int coursementorId);

    @Query("SELECT * FROM coursementor_table")
    List<Coursementor> getAllCoursementors();

    @Insert
    void insertCoursementor(Coursementor coursementor);

    @Insert
    void insertAll(Coursementor... coursementor);

    @Update
    void updateCoursementor(Coursementor coursementor);

    @Update
    void deleteCoursementor(Coursementor coursementor);

    @Query("DELETE FROM coursementor_table")
    public void nukeCoursementorTable();

}
