

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Scanner;

public class Faculty {
    static Scanner sc= new Scanner(System.in);
    static Statement stmt=null;
    static String instructor_id;
    public static void auth() throws Exception {
        Class.forName("org.postgresql.Driver");
        Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db1","postgres","12345");
        stmt = cn.createStatement();

        System.out.println("Enter username:");
        instructor_id = sc.nextLine();
        System.out.println("Enter password:");
        String pwd = sc.nextLine();
        String query = String.format("select * from facultyRecord where instructor_id='%s';",instructor_id);
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int a = rs.getRow();
        if (a==0){
            System.out.println("Incorrect UserName!");
            return;
        }
        String password = rs.getString("password");
        if(pwd.equals(password)){
            Dashboard();
        }
        else{
            System.out.println("Password is Incorrect!");
            return;
        }
    }
    public static void Dashboard() throws Exception {

        while(true){
            Faculty f = new Faculty();
            System.out.println("Enter 'A' for  Offering a course");
            System.out.println("Enter 'B' to Drop a course");
            System.out.println("Enter 'C' to Upload Marks of Student");
            System.out.println("Enter 'D' to see Details of Student");
            System.out.println("Enter 'E' to go back");
            String input =sc.nextLine();
            if(input.equals("A")){
                Offer();
            } else if (input.equals("B")) {
                DropCourse();
            } else if (input.equals("C")) {
                UploadMarks();
            } else if (input.equals("D")) {
                StudentDetail();
            } else if (input.equals("E")) {
                return;
            }else{
                System.out.println("Enter a valid input");
            }
        }
    }
    public static void Offer() throws SQLException {
        System.out.println("Enter CourseCode:");
        String course_id = sc.nextLine();
        System.out.println("Enter cgpa criteria if not type NA:");
        String cgpa = sc.nextLine();//enter cgpa
        String query="select * from running_Semiyearly;";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");

        String q2=String.format("select * from float_course where course_id='%s' and sem='%s' and curr_year='%s';",course_id,sem,year);
        ResultSet rs1 = stmt.executeQuery(q2);
        rs1.next();
        int a = rs1.getRow();
        if(a!=0)
        {
            System.out.println("Course is already floated either by you or other faculty");
            return;
        }
        String q4 =String.format("select * from course_catalog where course_id='%s';",course_id);
        ResultSet rs3 = stmt.executeQuery(q4);
        rs3.next();
        if(rs3.getRow()==0)
        {
            System.out.println("Course is not present in course catalog!");
            return;
        }
        String Prereq=rs3.getString("prerequisite");
        String Credit = rs3.getString("credit");
        String ltp = rs3.getString("L_T_P");


        String q3 = String.format("insert into float_course values('%s','%s','%s','%s','%s','%s','%s','%s');",course_id,instructor_id,cgpa,Prereq,sem,year,Credit,ltp);
        stmt.executeUpdate(q3);
        System.out.println("Course is Added Successfully!");


    }
    public static void DropCourse() throws SQLException {
        System.out.println("Enter course ID you want to drop:");
        String course_id = sc.nextLine();
        String query="select * from running_Semiyearly";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");
        String q1 = String.format("select * from float_course where course_id ='%s' and instructor_id ='%s' and sem='%s' and curr_year='%s';",course_id,instructor_id,sem,year);
        ResultSet rs1 = stmt.executeQuery(q1);
        rs1.next();
        int a = rs1.getRow();
        if(a==0){
            System.out.println("There is not such course floated by you!");
            return;
        }

            String q2 = String.format("delete from float_course where course_id ='%s' and instructor_id ='%s' and sem='%s' and curr_year='%s';",course_id,instructor_id,sem,year);
            stmt.executeUpdate(q2);
            String q3 = String.format("delete from Enroll where course_id='%s' and instructor_id ='%s' and sem='%s' and curr_year='%s';",course_id,instructor_id,sem,year);
            stmt.executeUpdate(q3);

        System.out.println("Course Droped Successfully!");

    }
    public static void UploadMarks() throws SQLException {
        System.out.println("Enter the Course ID:");
        String course_id = sc.nextLine();
        System.out.println("Enter semester:");
        String semi = sc.nextLine();
        System.out.println("Enter year:");
        String year = sc.nextLine();
        String query="select * from running_Semiyearly";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String curr_year = rs.getString("curr_year");
        if(sem.equals(semi) && year.equals(curr_year)){
            System.out.println("This Course is not applicable for upload marks");
        }
        String q1=String.format("select * from float_course where course_id ='%s' and instructor_id='%s' and (sem ='%s' and curr_year ='%s');",course_id,instructor_id,semi,year);
        ResultSet r1 = stmt.executeQuery(q1);
        r1.next();
        int a = r1.getRow();
        if(a==0){
            System.out.println("There is no such course available for uploading marks or offer by you in that sem!");
            return;
        }
        System.out.println("Enter File Address:");
        String csvFile=sc.nextLine();

        try {
            File file = new File(csvFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            String[] tempArr;
            while((line = br.readLine()) != null) {
                tempArr = line.split(",");
                for(String tempStr : tempArr) {
                    String q3= String.format("UPDATE Enroll SET grade='%s' WHERE student_id='%s' and course_id='%s' and instructor_id='%s' and sem='%s' and curr_year='%s';",tempArr[1],tempArr[0],course_id,instructor_id,semi,year);
                    stmt.executeUpdate(q3);
                }
                System.out.println();
            }
            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Successfully Update!");
        return;
    }

    public static void StudentDetail() throws SQLException {
        System.out.println("Enter Student ID:");
        String student_id = sc.nextLine();
        String query =String.format("select * from StudentRecord where student_id = '%s';",student_id);
        ResultSet rs=stmt.executeQuery(query);
        rs.next();
        int a = rs.getRow();
        if(a==0){
            System.out.println("Invalid Student ID!");
            return;
        }
        String q2 = String.format("select * from Enroll where student_id='%s';",student_id);
        ResultSet rs1=stmt.executeQuery(q2);
        rs1.next();
        if(rs1.getRow()==0){
            System.out.println("This is first semester of Student!");
            return;
        }
        while(rs1.next()){
            System.out.print("Course_ID:" + rs1.getString("course_id"));
            System.out.print(", Semester:"+rs1.getString("sem"));
            System.out.print(", Year:"+rs1.getString("curr_year"));
            System.out.print(", Credit:"+rs1.getString("credit"));
            System.out.println(", Grade:"+rs1.getString("grade"));
        }
        System.out.println("Student Record Successfully Open!");
    }
}
