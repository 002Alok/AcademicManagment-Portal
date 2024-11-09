

import java.sql.*;
import java.util.Scanner;

public class Student {
    static Scanner sc = new Scanner(System.in);

    static Statement stmt = null;

    static String student_id;


    public static void auth() throws SQLException {
        try{
            Class.forName("org.postgresql.Driver");
            Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db1","postgres","12345");
            stmt = cn.createStatement();
        }catch (Exception e ){
            System.out.println(e);
        }
        System.out.println("Enter username:");
        student_id = sc.nextLine();
        System.out.println("Enter password:");
        String pwd = sc.nextLine();
        String query = String.format("select * from StudentRecord where student_id='%s';",student_id);
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        int a = rs.getRow();
        if (a==0){
            System.out.println("Incorrect UserName!");
            return;
        }
        String password = rs.getString("password");
        if(pwd.equals(password)){
            dashboard();
        }
        else{
            System.out.println("Password is Incorrect!");
            return;
        }
    }
    public static void dashboard() throws SQLException {

        while(true){
            System.out.println("Enter 'A' to Withdraw a Course");
            System.out.println("Enter 'B' to Enroll in a Course");
            System.out.println("Enter 'C' to  see your Academic Record");
            System.out.println("Enter 'D' to go Back");
            String input = sc.nextLine();
            if(input.equals("A")){
                Withdraw();
            } else if (input.equals("B")) {
                Enrollment();
            } else if (input.equals("C")) {
                MyCourse();
            } else if (input.equals("D")) {
                return;
            }else{
                System.out.println("Enter a valid Input");
            }
        }
    }


    public static double Gcon(String grade){
        double a;
        switch (grade){
            case "A":
                return 10;

            case "A-":
                return 9;
            case "B":
                return 8;
            case "B-":
                return 7;
            case "C":
                return 6;
            case "C-":
                return 5;
            case "D":
                return 4;
            default:
                return 0;
        }

    }
    public static void Withdraw() throws SQLException {
        System.out.println("Enter course ID you want to withdraw:");
        String course_id = sc.nextLine();
        String query="select * from running_Semiyearly;";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");

        String q1 =String.format("select * from Enroll where student_id='%s' and course_id='%s' and sem='%s' and curr_year='%s';",student_id,course_id,sem,year);
        ResultSet rs1 = stmt.executeQuery(q1);
        rs1.next();

        int a = rs1.getRow();

        if(a!=0){
            String q2 = String.format("delete from Enroll where student_id='%s' and course_id='%s' and sem='%s' and curr_year='%s';",student_id,course_id,sem,year);
            stmt.executeUpdate(q2);

            System.out.println("Successfully WithDrawl!");
        }else{
            System.out.println("You are not enrolled in this course in this sem!");
        }
    }
    public static boolean Check_pre(String course_id) throws SQLException {
        String q1="select * from running_Semiyearly;";

        ResultSet rs1 = stmt.executeQuery(q1);
        rs1.next();
        String sem = rs1.getString("curr_sem");
        String year = rs1.getString("curr_year");
        String q2 = String.format("select *from studentrecord where student_id='%s';",student_id);
        ResultSet rs2=stmt.executeQuery(q2);
        rs2.next();
        String batch = rs2.getString("batch");
        if(batch.equals(year)){
            return true;
        }

        String query = String.format("select * from course_catalog where course_id='%s'",course_id);
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String req_pre=rs.getString("prerequisite");
        String g1="NA",g2="F";
        String q4 = String.format("select * from Enroll where course_id='%s' and Student_id='%s' and grade!='%s' and grade !='%s';",req_pre,student_id,g1,g2);
        ResultSet rs4=stmt.executeQuery(q1);
        rs4.next();
        int a = rs4.getRow();
        if(a==0){
            return false;
        }
        return true;
    }

    public static boolean Check_creditLimit(int total_credit) throws SQLException {
        String query="select * from running_Semiyearly;";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");
        int i_sem=0;
        int i_year=0;
        try{
            i_sem = Integer.parseInt(sem);
            i_year= Integer.parseInt(year);
        }catch (Exception e){
            System.out.println(e);
        }
        String q1 = String.format("select * from studentRecord where student_id='%s';",student_id);
        ResultSet rs1 = stmt.executeQuery(q1);
        rs1.next();
        String batch = rs1.getString("batch");
        int i_batch=0;
        try {
            i_batch = Integer.parseInt(batch);
        }catch(Exception e){
            System.out.println(e);
        }
        int curr_yrs = i_year + 1 - i_batch;
        int curr_sem = 0;
        if(i_sem ==1){
            curr_sem = curr_yrs*2-1;
        }
        else{
            curr_sem = curr_yrs*2;
        }
        double credit_limit = 0;
        String q3 = String.format("select * from first_year;");
        ResultSet rs2 = stmt.executeQuery(q3);
        rs2.next();
        String sem1 = rs2.getString("sem1");
        String sem2 = rs2.getString("sem2");
        double i_sem1=0;
        double i_sem2=0;
        try{
            i_sem1= Double.parseDouble(sem1);
            i_sem2=Double.parseDouble(sem2);
        }catch (Exception e){
            System.out.println(e);
        }
        double TotalCredit =0;
        if(curr_sem == 1 && batch.equals(year) ){
            TotalCredit = i_sem1;
        } else if (curr_sem==2 && batch.equals(year)){
            TotalCredit = i_sem2;

        }else{
            if(i_sem==1){
                int a = i_sem-1;
                int b = i_sem;
                int c =i_year-1;
                String as="",bs="",cs="";
                try {
                    as = Integer.toString(a);
                    bs = Integer.toString(b);
                    cs = Integer.toString(c);
                }catch (Exception e){System.out.println(e);
                }
                String q4 = String.format("select * from Enroll where student_id='%s' and grade!='NA' and grade!='F' and curr_year='%s';",student_id,cs);
                ResultSet rs4= stmt.executeQuery(q4);
                while(rs4.next()){
                    String q5= rs4.getString("credit");
                    int credit=0;
                    try {
                        credit= Integer.parseInt(q5);
                    } catch (Exception e){System.out.println(e);
                    }
                    TotalCredit +=credit;
                }
                TotalCredit =( TotalCredit*1.25 )/2;
            }
            else if(i_sem==2){
                int a =i_sem-1;
                int b = i_sem;
                int c=i_year;
                int d =i_year-1;
                String as="",bs="",cs="",ds="";
                try{
                    as=Integer.toString(a);
                    bs=Integer.toString(b);
                    cs=Integer.toString(c);
                    ds=Integer.toString(d);
                }catch (Exception e){
                    System.out.println(e);
                }
                String q4 = String.format("select * from Enroll where student_id='%s' and grade!='NA' and grade!='F' and((sem='%s' and curr_year='%s') or(sem='%s' and curr_year='%s'));",student_id,as,cs,bs,ds);
                ResultSet rs4= stmt.executeQuery(q4);
                while(rs4.next()){
                    String q5= rs4.getString("credit");
                    int credit=0;
                    try {
                        credit= Integer.parseInt(q5);
                    } catch (Exception e){
                        System.out.println(e);
                    }
                    TotalCredit +=credit;
                }
                TotalCredit = (TotalCredit*1.25)/2;


            }
            else{
                System.out.println("You are not belong to this clg!");
            }
        }

        if(total_credit>TotalCredit){
            System.out.println("CreditLimit Exceed!");
            return false;
        }
        return true;
    }
    public static double Calc_cgpa() throws SQLException {
        String q1 = String.format("select * from Enroll where student_id='%s' and grade!='NA' and grade!='F' and grade!='E';",student_id);
        ResultSet rs = stmt.executeQuery(q1);
        double TotalCredit=0.0;
        double cgpa = 0;
        while(rs.next()){
            String credit = rs.getString("credit");
            double i_credit=0;
            try{
                i_credit=Double.parseDouble(credit);
            }catch (Exception e){
                System.out.println(e);
            }
            TotalCredit += i_credit;
            String grade = rs.getString("grade");
            double i_grade = Gcon(grade);

            cgpa += i_grade*i_credit;
        }

        return cgpa/TotalCredit;
    }
    public static boolean Check_cgpa(String course_id) throws SQLException {
        String query="select * from running_Semiyearly";

        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");

        String q2 = String.format("select *from studentrecord where student_id='%s';",student_id);
        ResultSet rs2=stmt.executeQuery(q2);
        rs2.next();
        String batch = rs2.getString("batch");
        if(batch.equals(year) && sem.equals("1")){
            return true;
        }

        String q1 = String.format("select * from float_course where course_id='%s' and sem ='%s' and curr_year='%s';",course_id,sem,year);
        ResultSet rs1= stmt.executeQuery(q1);

        rs1.next();
        String cgpa_req = rs1.getString("cgpa");
        double cg_req=0;
        if(cgpa_req.equals("NA")){
            cg_req=0;
        }else{
           try {
            cg_req=Double.parseDouble(cgpa_req);
           }catch (Exception e){
            System.out.println(e);
            return false;
           }
        }
        double cgpa=Calc_cgpa();

        if(cgpa<cg_req){
            return false;
        }
        return true;
    }

    public static int curr_credit() throws SQLException {
        String query = String.format("select * from running_semiyearly;");
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");
        String q3 =String.format("select * from Enroll where student_id='%s' and grade='NA' and sem='%s' and curr_year='%s'; ",student_id,sem,year);
        ResultSet rs3=null;
        try {
            rs3=stmt.executeQuery(q3);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int total_credit=0;
        while(rs3.next()){
            String q4=rs3.getString("credit");
            int credit=0;
            try{
                credit = Integer.parseInt(q4);
            }catch (Exception e){
                System.out.println(e);
            }
            total_credit+=credit;

        }

        return total_credit;
    }
    public static void Enrollment() throws SQLException {
        System.out.println("Enter course ID:");
        String course_id = sc.nextLine();
        String query="select * from running_Semiyearly;";
        ResultSet rs = stmt.executeQuery(query);
        rs.next();
        String sem = rs.getString("curr_sem");
        String year = rs.getString("curr_year");

        String q2 = String.format("select * from float_course where course_id='%s' and sem='%s' and curr_year='%s';",course_id,sem,year );
        ResultSet rs1 = stmt.executeQuery(q2);
        rs1.next();
        int a = rs1.getRow();
        if(a==0) {
            System.out.println("Such course is not floated");
            return;}
        int total_credit=curr_credit();
        boolean check=Check_creditLimit(total_credit);
        boolean cgpa=Check_cgpa(course_id);
        boolean pre=Check_pre(course_id);
        boolean alrEnroll = AlreadyEnrolled(course_id);
        if(a!=0 && check && cgpa && pre && alrEnroll){
               rs1 = stmt.executeQuery(q2);
               rs1.next();
            String instructor_id = rs1.getString("instructor_id");
            String credit = rs1.getString("credit");
            String q1 = String.format("insert into Enroll values('%s','%s','%s','%s','%s','%s','%s');",student_id,instructor_id,sem,year,course_id,"NA",credit);
            stmt.executeUpdate(q1);
            System.out.println("Enrolled Successfully!");
        }
        else{
            System.out.println("You are not able to Enroll at present !");}
    }
    public static void MyCourse() throws SQLException {
        String query= String.format("select * from Enroll where student_id = '%s';",student_id);
        ResultSet rs= stmt.executeQuery(query);
        boolean as = false;
        while(rs.next()){
            as = true;
            System.out.print("Student_id:"+ rs.getString("student_id"));
            System.out.print(", course_id:"+ rs.getString("course_id"));
            System.out.print(", Semester:"+ rs.getString("sem"));
            System.out.print(", Year:"+ rs.getString("curr_year"));
            System.out.print(", Credit:"+ rs.getString("credit"));
            System.out.println(", Grade:"+ rs.getString("grade"));
        }
        if(!as){
            System.out.println("You are not enrolled in any course!");} else{
            System.out.println("Academic Record Displayed successfully!");}
    }
    public static boolean AlreadyEnrolled(String course_id) throws SQLException {
        String query =String.format("select * from Enroll where student_id='%s' and course_id ='%s' and grade!='F';",student_id,course_id);
        ResultSet rs= stmt.executeQuery(query);
        rs.next();
        int a = rs.getRow();
        if(a!=0){
            System.out.println("Already Enrolled!");
            return false;

        }
        return true;
    }

}

