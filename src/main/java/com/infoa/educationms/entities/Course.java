package com.infoa.educationms.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int courseId;

    private String title;

    @Column(name = "dept_name")
    private String deptName;

    private int credits;

    @Column(name = "course_introduction")
    private String introduction;

    private int capacity;

    private String required_room_type;

    private int grade_year;

    private int period;

    // constructors
    public Course() {
    }

    public Course(int courseId, String title, String deptName, int credits, String introduction, int capacity, String required_room_type, int grade_year, int period) {
        this.courseId = courseId;
        this.title = title;
        this.deptName = deptName;
        this.credits = credits;
        this.introduction = introduction;
        this.capacity = capacity;
        this.required_room_type = required_room_type;
        this.grade_year = grade_year;
        this.period = period;
    }

    // 转换为字符串
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", title='" + title + '\'' +
                ", deptName='" + deptName + '\'' +
                ", credits=" + credits +
                ", introduction='" + introduction + '\'' +
                ", capacity=" + capacity +
                ", required_room_type='" + required_room_type + '\'' +
                ", grade_year=" + grade_year +
                ", period=" + period +
                '}';
    }

    // getter and setter methods
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getRequiredRoomType() {
        return required_room_type;
    }

    public void setRequiredRoomType(String required_room_type) {
        this.required_room_type = required_room_type;
    }

    public int getGradeYear() {
        return grade_year;
    }

    public void setGradeYear(int grade_year) {
        this.grade_year = grade_year;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
