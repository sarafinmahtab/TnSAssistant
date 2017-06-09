package com.sarafinmahtab.tnsassistant.teacher;

/**
 * Created by Arafin on 6/8/2017.
 */

public class Course {

    private String course_id, course_code, course_title, session, credit;

    public Course(String course_id, String course_code, String course_title, String session, String credit) {
        this.course_id = course_id;
        this.course_code = course_code;
        this.course_title = course_title;
        this.session = session;
        this.credit = credit;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_code = course_id;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getCourse_title() {
        return course_title;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }
}
