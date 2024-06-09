package com.project.demo.logic.entity.form;

import com.project.demo.logic.entity.user.User;
import jakarta.persistence.*;

import java.util.Date;


@Table(name = "Forms")
@Entity
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long formId;

    private Date date;
    private Integer age;

    @Column(name = "sleep_hours")
    private Integer sleepHours;

    @Column(name = "exercise_days")
    private String exerciseDays;

    private Boolean useDrugs;
    private Boolean useAlcohol;
    private String gender;
    private String job;

    @Column(name = "eduacation_level")
    private String eduacationLevel;

    @Column(name = "family_history")
    private String familyHistory;

    @Column(name = "medical_condition")
    private String medicalCondition;

    @Column(name = "mental_illness")
    private String mentalIllness;

    @Column(name = "diet_type")
    private String dietType;

    @Column(name = "screen_time")
    private Integer screenTime;

    @Column(name = "stress_management")
    private String stressManagement;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Form() {}

    public Form(Long formId, Date date, Integer age, Integer sleepHours, String exerciseDays, Boolean useDrugs, Boolean useAlcohol, String gender, String job, String eduacationLevel, String familyHistory, String medicalCondition, String mentalIllness, String dietType, int screenTime, String stressManagement, User user) {
        this.formId = formId;
        this.date = date;
        this.age = age;
        this.sleepHours = sleepHours;
        this.exerciseDays = exerciseDays;
        this.useDrugs = useDrugs;
        this.useAlcohol = useAlcohol;
        this.gender = gender;
        this.job = job;
        this.eduacationLevel = eduacationLevel;
        this.familyHistory = familyHistory;
        this.medicalCondition = medicalCondition;
        this.mentalIllness = mentalIllness;
        this.dietType = dietType;
        this.screenTime = screenTime;
        this.stressManagement = stressManagement;
        this.user = user;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(Integer sleepHours) {
        this.sleepHours = sleepHours;
    }

    public String getExerciseDays() {
        return exerciseDays;
    }

    public void setExerciseDays(String exerciseDays) {
        this.exerciseDays = exerciseDays;
    }

    public Boolean getUseDrugs() {
        return useDrugs;
    }

    public void setUseDrugs(Boolean useDrugs) {
        this.useDrugs = useDrugs;
    }

    public Boolean getUseAlcohol() {
        return useAlcohol;
    }

    public void setUseAlcohol(Boolean useAlcohol) {
        this.useAlcohol = useAlcohol;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getEduacationLevel() {
        return eduacationLevel;
    }

    public void setEduacationLevel(String eduacationLevel) {
        this.eduacationLevel = eduacationLevel;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(String medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public String getMentalIllness() {
        return mentalIllness;
    }

    public void setMentalIllness(String mentalIllness) {
        this.mentalIllness = mentalIllness;
    }

    public String getDietType() {
        return dietType;
    }

    public void setDietType(String dietType) {
        this.dietType = dietType;
    }

    public Integer getScreenTime() {
        return screenTime;
    }

    public void setScreenTime(Integer screenTime) {
        this.screenTime = screenTime;
    }

    public String getStressManagement() {
        return stressManagement;
    }

    public void setStressManagement(String stressManagement) {
        this.stressManagement = stressManagement;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
