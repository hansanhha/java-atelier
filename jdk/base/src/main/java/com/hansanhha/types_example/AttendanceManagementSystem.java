package com.hansanhha.types_example;

import java.util.Collection;

public interface AttendanceManagementSystem {

    void addLecture(Lecture lecture);

    void addAttendant(Lecture lecture, Attendant attendant);

    void markAttendance(Lecture lecture, Attendant attendant);

    void markAbsence(Lecture lecture, Attendant attendant);

    Collection<Attendant> getAttendants(Lecture lecture);
}
