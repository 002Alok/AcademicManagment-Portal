CREATE TABLE course_catalog(
   course_id varchar(50) not null,
   name varchar(50) not null,
   L_T_P varchar(50) not null,
   prerequisite varchar(50) ,
   credit varchar(50) not null,

   PRIMARY KEY(course_id)
 );

CREATE TABLE FacultyRecord(
 instructor_id varchar(50) not null,
 name varchar(50) not null,
 department varchar(50) not null,
 password varchar(50) not null,

 PRIMARY KEY(instructor_id)
);

CREATE TABLE AdminRecord(
 Academic_id varchar(50) not null,
 Password varchar(50) not null
);

 CREATE TABLE StudentRecord(
  student_id varchar(50) not null,
  name varchar(50) not null,
  dept_name varchar(50) not null,
  password varchar(50) not null,
  batch varchar(50) not null,
  PRIMARY KEY(student_id)

 );



CREATE TABLE float_course(
 course_id varchar(50) not null,
 instructor_id varchar(50) not null,
 cgpa varchar(50) ,
 prerequisite varchar(50),
 sem varchar(50) not null,
 curr_year varchar(50) not null,
 credit varchar(50) not null,
 L_T_P varchar(50) not null,
 PRIMARY KEY(course_id,instructor_id,sem,curr_year)
);

CREATE TABLE Enroll(
 student_id varchar(50) not null,
 instructor_id varchar(50) not null,
 sem varchar(50) not null,
 curr_year varchar(50) not null,
 course_id varchar(50) not null,
 grade varchar(50) not null,
 credit varchar(50) not null,
 PRIMARY KEY(student_id,sem,curr_year,course_id)
);


CREATE TABLE running_Semiyearly(
 curr_sem varchar(50) not null,
 curr_year varchar(50) not null
);

CREATE TABLE first_year(
sem1 varchar(50) not null,
sem2 varchar(50) not null,
batch varchar(50) not null
);