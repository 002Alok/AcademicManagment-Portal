

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.Scanner;

public class Academic {
    static Scanner sc= new Scanner(System.in);
    static Statement stmt=null;

    public static void auth() throws Exception {
        try{
            Class.forName("org.postgresql.Driver");
            Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db1","postgres","12345");
            stmt = cn.createStatement();
            System.out.println("Successfully Connected!");
        }catch (Exception e ){
            System.out.println(e);
        }
        System.out.println("Enter username:");
        String Admin_id = sc.nextLine();
        System.out.println("Enter password:");
        String pwd = sc.nextLine();
        String query = String.format("select * from AdminRecord where Academic_id='%s';",Admin_id);
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int a = rs.getRow();
        if (a==0){
            System.out.println("Incorrect UserName!");
            return;
        }
        String password = rs.getString("password");
        if(pwd.equals(password)){
            Dashboard1();
        }
        else{
            System.out.println("Password is Incorrect!");
            return;
        }
    }

    public static void Dashboard1() throws Exception{


        while(true){
            System.out.println("Enter 'A' for  Add a new Student");
            System.out.println("Enter 'B' for  Add a new Facutly");
            System.out.println("Enter 'C' for  Add a new Course");
            System.out.println("Enter 'D' to See Detail of a Student");
            System.out.println("Enter 'E' to Generate Transcript");
            System.out.println("Enter 'F' to upgrade Semester");
            System.out.println("Enter 'G' to go back");
            String input = sc.nextLine();


            if(input.equals("A")){
                AddStudent();
            } else if (input.equals("B")) {
                AddFaculty();
            } else if (input.equals("C")) {
                addCourseCatalog();
            } else if (input.equals("D")) {
                DetailStudent();
            } else if (input.equals("E")) {
                GenerateTranscript();
            }else if(input.equals("F")){
                UpgradeSem();
            } else if (input.equals("G")) {
                return;
            } else{
                System.out.println("Enter a valid input");

            }
        }
    }
    public static void AddStudent() throws SQLException {
        System.out.println("Enter the student ID:");
        String student_id = sc.nextLine();
        String q1 = String.format("Select * from StudentRecord where student_id ='%s';",student_id);
        ResultSet rs = stmt.executeQuery(q1);
        rs.next();
        int a = rs.getRow();
        if(a!=0){
            System.out.println("Student ID is already assigned");

            return ;
        }
        System.out.println("Enter name of student:");
        String name = sc.nextLine();
        System.out.println("Enter Department of student:");
        String dep=sc.nextLine();
        System.out.println("Enetr batch year:");
        String batch =sc.nextLine();
        String query = String.format("insert into StudentRecord values('%s','%s','%s','%s','%s');",student_id,name,dep,student_id,batch);
        stmt.executeUpdate(query);
        System.out.println("Student Added Successfully!");
    }
    public static void AddFaculty() throws SQLException {
        System.out.println("Enter the instructor ID:");
        String instructor_id = sc.nextLine();
        String q1 = String.format("Select * from FacultyRecord where instructor_id ='%s';",instructor_id);
        ResultSet rs = stmt.executeQuery(q1);
        rs.next();
        int a = rs.getRow();
        if(a!=0){
            System.out.println("instructor ID is already assigned");

            return ;
        }
        System.out.println("Enter name of instructor:");
        String name = sc.nextLine();
        System.out.println("Enter Department of instructor:");
        String dep=sc.nextLine();


        String query = String.format("insert into FacultyRecord values('%s','%s','%s','%s');",instructor_id,name,dep,instructor_id);
        stmt.executeUpdate(query);
        System.out.println("Faculty Added Successfully!");
    }

    public static void addCourseCatalog() throws SQLException {
        System.out.println("Enter course_id of new course:");
        String course_id = sc.nextLine();
        String q1 = String.format("Select * from course_catalog where course_id ='%s';",course_id);
        ResultSet rs = stmt.executeQuery(q1);
        rs.next();
        int a = rs.getRow();
        if(a!=0){
            System.out.println("course ID is already assigned!");

            return ;
        }
        System.out.println("Enter name of new course:");
        String name = sc.nextLine();
        System.out.println("Enter L_T_P of new course:");
        String ltp =sc.nextLine();
        System.out.println("Enter prerequisite of new course:");
        String pre = sc.nextLine();
        System.out.println("Enter credit of new course:");
        String credit =sc.nextLine();
        String query = String.format("insert into course_catalog values('%s','%s','%s','%s','%s');",course_id,name,ltp,pre,credit);
        stmt.executeUpdate(query);
        System.out.println("Course is Successfully Added!");
    }
    public static void DetailStudent() throws SQLException {
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
        while(rs1.next()){
            System.out.print("Course_ID:" + rs1.getString("course_id"));
            System.out.print(", Semester:"+rs1.getString("sem"));
            System.out.print(", Year:"+rs1.getString("curr_year"));
            System.out.print(", Credit:"+rs1.getString("credit"));
            System.out.println(", Grade:"+rs1.getString("grade"));
        }
        System.out.println("Record open Successfully!");
    }
    public static void GenerateTranscript(){
        System.out.println("Enter Student ID:");
        String student_id=sc.nextLine();
        File myObj;
        try {
            myObj = new File(student_id+".txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred." + e);
            return;

        }

        try {

            FileWriter fileWritter = new FileWriter("filename.txt",true);
            BufferedWriter bw = new BufferedWriter(fileWritter);
            String q1 = String.format("select * from StudentRecord where student_id ='%s';",student_id);
            ResultSet r1 = stmt.executeQuery(q1);
            r1.next();
            int a = r1.getRow();
            if(a==0){
                System.out.println("Invalid Student ID");
                return;
            }
            bw.write("Entry no.-" + r1.getString("student_id"));
            bw.write("Name:" +r1.getString("name"));
            bw.write("Batch:" +r1.getString("batch"));
            String query = String.format("select * from Enroll where student_id='%s'",student_id);
            ResultSet rs= stmt.executeQuery(query);
            while(rs.next()){
                bw.write("Course_ID:" +rs.getString("course_id"));
                bw.write("Credit:" +rs.getString("credit"));
                bw.write("Semester:" +rs.getString("sem"));
                bw.write("Year:" +rs.getString("curr_year"));
                bw.write("Grade:" +rs.getString("grade"));
            }
            bw.close();
            System.out.println("Done");
        } catch(Exception e){
            System.out.println(e);
        }
    }


    public static void UpgradeSem() throws Exception {
        String q1= String.format("select * from running_Semiyearly;");
        ResultSet r1=stmt.executeQuery(q1);
        r1.next();
        String sem = r1.getString("curr_sem");
        String year=r1.getString("curr_year");

        int i_sem=0,i_year=0;
        i_sem=Integer.parseInt(sem);
        i_year=Integer.parseInt(year);
        if(i_sem==2){
            i_sem=1;
            i_year= i_year+1;
        }else{
            i_sem=2;
        }
        String n_sem= Integer.toString(i_sem);
        String n_year= Integer.toString(i_year);
        String q3 = String.format("delete from running_Semiyearly; ");
        stmt.executeUpdate(q3);
        String q2 = String.format("insert into running_Semiyearly values('%s','%s');",n_sem,n_year);
        stmt.executeUpdate(q2);

        System.out.println("Semester Increment is done!");
    }
}
