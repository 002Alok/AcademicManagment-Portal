
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.util.Scanner;
public class main{
    public static void main(String[] args) throws Exception {
        try{
            Class.forName("org.postgresql.Driver");
            Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db1","postgres","12345");
            Statement stmt = cn.createStatement();

        }catch(Exception e){
            System.out.println(e);
        }
        System.out.println("Enter 1 as Admin Login");
        System.out.println("Enter 2 as Instructor Login");
        System.out.println("Enter 3 as Student Login");
        Scanner sc = new Scanner(System.in);
        String s= sc.nextLine();
        if(s.equals("1")){
            Academic a = new Academic();
            a.Dashboard1();
        }




        else if (s.equals("2")) {
            Faculty f = new Faculty();
            f.Dashboard();

        }



        else if(s.equals("3")){
            Student st = new Student();
            st.dashboard();
        }else{
            System.out.println("Please Enter a valid Input!");
        }
    }
}