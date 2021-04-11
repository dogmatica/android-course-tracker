package com.example.williamstultscourseguide.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CourseDao {

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId ORDER BY course_id")
    List<Course> getCourseList(int termId);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId AND course_id = :courseId")
    Course getCourse(int termId, int courseId);

    @Query("SELECT * FROM course_table WHERE term_id_fk = :termId AND course_name = :courseName")
    Course getCourseByName(int termId, String courseName);

    @Query("INSERT INTO course_table (term_id_fk, course_name) VALUES (:termId, \"Course Name\"); ")
    void addCourse(int termId);

    @Query("SELECT * FROM course_table")
    List<Course> getAllCourses();

    @Insert
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Query("DELETE FROM course_table")
    public void nukeCourseTable();

    @Query("DELETE FROM course_table WHERE course_id = :courseId")
    void deleteCourse(int courseId);

}
