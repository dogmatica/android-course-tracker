package com.example.williamstultscourseguide.data;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.List;

public class PopulateDatabase extends AppCompatActivity {

    public static String LOG_TAG = "PopData";
    Term tempTerm1 = new Term();
    Term tempTerm2 = new Term();
    Term tempTerm3 = new Term();
    Course tempCourse1 = new Course();
    Course tempCourse2 = new Course();
    Course tempCourse3 = new Course();
    Assessment tempAssessment1 = new Assessment();
    Assessment tempAssessment2 = new Assessment();
    Assessment tempAssessment3 = new Assessment();
    Coursementor tempCourseMentor1 = new Coursementor();
    Coursementor tempCourseMentor2 = new Coursementor();
    Coursementor tempCourseMentor3 = new Coursementor();

    MainDatabase db;

    public void populate(Context context) {
        db = MainDatabase.getInstance(context);
        try {
            insertTerms();
            insertCourses();
            insertAssessments();
            insertCoursementors();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Populate DB Failed");
        }
    }

    private void insertTerms() {
        Calendar start;
        Calendar end;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 4);
        tempTerm1.setTerm_name("Spring 2021");
        tempTerm1.setTerm_start(start.getTime());
        tempTerm1.setTerm_end(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 4);
        end.add(Calendar.MONTH, 10);
        tempTerm2.setTerm_name("Fall 2021");
        tempTerm2.setTerm_start(start.getTime());
        tempTerm2.setTerm_end(end.getTime());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, 10);
        end.add(Calendar.MONTH, 16);
        tempTerm3.setTerm_name("Spring 2022");
        tempTerm3.setTerm_start(start.getTime());
        tempTerm3.setTerm_end(end.getTime());

        db.termDao().insertAll(tempTerm1, tempTerm2, tempTerm3);
    }

    private void insertCourses() {
        Calendar start;
        Calendar end;
        List<Term> termList = db.termDao().getTermList();
        if (termList == null) return;

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, -1);
        tempCourse1.setCourse_name("Coders Anonymous");
        tempCourse1.setCourse_start(start.getTime());
        tempCourse1.setCourse_end(end.getTime());
        tempCourse1.setCourse_notes("PrePopulate Notes: notes notes notes notes notes");
        tempCourse1.setCourse_status("Completed");
        tempCourse1.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        //end.add(Calendar.MONTH, -1);
        tempCourse2.setCourse_name("The Suspicious Donut");
        tempCourse2.setCourse_start(start.getTime());
        tempCourse2.setCourse_end(end.getTime());
        tempCourse2.setCourse_notes("PrePopulate Notes: notes notes notes notes notes");
        tempCourse2.setCourse_status("Completed");
        tempCourse2.setTerm_id_fk(termList.get(0).getTerm_id());

        start = Calendar.getInstance();
        end = Calendar.getInstance();
        //start.add(Calendar.MONTH, -2);
        end.add(Calendar.MONTH, 4);
        tempCourse3.setCourse_name("Campus Conspiracies");
        tempCourse3.setCourse_start(start.getTime());
        tempCourse3.setCourse_end(end.getTime());
        tempCourse3.setCourse_notes("PrePopulate Notes: notes notes notes notes notes");
        tempCourse3.setCourse_status("In-Progress");
        tempCourse3.setTerm_id_fk(termList.get(0).getTerm_id());

        db.courseDao().insertCourse(tempCourse1);
        db.courseDao().insertCourse(tempCourse2);
        db.courseDao().insertCourse(tempCourse3);

    }

    public void insertAssessments() {

        Calendar due;
        Calendar goal;
        List<Term> termList = db.termDao().getTermList();
        List<Course> courseList = db.courseDao().getCourseList(termList.get(0).getTerm_id());
        if (courseList == null) return;

        due = Calendar.getInstance();
        goal = Calendar.getInstance();
        due.add(Calendar.MONTH, -1);
        tempAssessment1.setAssessment_due(due.getTime());
        tempAssessment1.setAssessment_goal(goal.getTime());
        tempAssessment1.setAssessment_title("Steps Project");
        tempAssessment1.setAssessment_type("Performance");
        tempAssessment1.setCourse_id_fk(courseList.get(0).getCourse_id());
        tempAssessment1.setAssessment_status("Passed");

        due = Calendar.getInstance();
        goal = Calendar.getInstance();
        due.add(Calendar.MONTH, 1);
        goal.add(Calendar.MONTH, 1);
        tempAssessment2.setAssessment_due(due.getTime());
        tempAssessment2.setAssessment_goal(goal.getTime());
        tempAssessment2.setAssessment_title("Jelly Exam");
        tempAssessment2.setAssessment_type("Objective");
        tempAssessment2.setCourse_id_fk(courseList.get(1).getCourse_id());
        tempAssessment2.setAssessment_status("Failed");

        due = Calendar.getInstance();
        goal = Calendar.getInstance();
        due.add(Calendar.MONTH, 3);
        goal.add(Calendar.MONTH, 2);
        tempAssessment3.setAssessment_due(due.getTime());
        tempAssessment3.setAssessment_goal(goal.getTime());
        tempAssessment3.setAssessment_title("My Conspiracy Theory");
        tempAssessment3.setAssessment_type("Performance");
        tempAssessment3.setCourse_id_fk(courseList.get(2).getCourse_id());
        tempAssessment3.setAssessment_status("Pending");

        db.assessmentDao().insertAll(tempAssessment1, tempAssessment2, tempAssessment3);

    }

    public void insertCoursementors() {

        List<Term> termList = db.termDao().getTermList();
        List<Course> courseList = db.courseDao().getCourseList(termList.get(0).getTerm_id());
        if (courseList == null) return;

        tempCourseMentor1.setCoursementor_name("John Doe");
        tempCourseMentor1.setCoursementor_email("jdoe@stults.edu");
        tempCourseMentor1.setCoursementor_phone("555-666-7777");
        tempCourseMentor1.setCourse_id_fk(courseList.get(0).getCourse_id());

        tempCourseMentor2.setCoursementor_name("Tim Horton");
        tempCourseMentor2.setCoursementor_email("thorton@stults.edu");
        tempCourseMentor2.setCoursementor_phone("555-444-3333");
        tempCourseMentor2.setCourse_id_fk(courseList.get(1).getCourse_id());

        tempCourseMentor3.setCoursementor_name("Alex Trump");
        tempCourseMentor3.setCoursementor_email("atrump@stults.edu");
        tempCourseMentor3.setCoursementor_phone("555-222-8888");
        tempCourseMentor3.setCourse_id_fk(courseList.get(2).getCourse_id());

        db.coursementorDao().insertAll(tempCourseMentor1, tempCourseMentor2, tempCourseMentor3);


    }


}