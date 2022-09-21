package sql.logic.service;

import sql.logic.models.DAO.*;
import sql.logic.models.entities.*;
import sql.logic.repositoryConnectDB.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EPA {

        public static void main(String[] args) {

        // connection to DB
        DBConnectionManager dcm = new DBConnectionManager("127.0.0.1:5432",
                "EPA", "postgres", "123qwe");

        // id for connection with an account of employee
        long idEMPLOYEE = 0;

                try {
                        // part with connection to DB
                        Connection c = dcm.getConnection();
                        c.setAutoCommit(false);
                        System.out.println("   Connected to the database!");
                        System.out.println("________________________________________________");

                        // employee greetings
                        System.out.println("________Hello, Employee. Present yourself________");
                        System.out.println("   Please, tape : \n   1 - If you are already have login;");
                        System.out.println("   2 - If you are need to register;");
                        System.out.println();

                        boolean done = false;

                        // main list: login or registration
                        Scanner input = new Scanner(System.in);
                        String decision;
                        do {
                                // login (already have an account)
                                decision = input.nextLine();
                                if (decision.equals("1")) {
                                        boolean correct = false;
                                        do {
                                                System.out.println("________Please enter your login_______");
                                                System.out.println();
                                                input = new Scanner(System.in);
                                                String loginUser = input.nextLine();
                                                System.out.println();
                                                System.out.println("________Please enter your password________");
                                                System.out.println();
                                                String passwordUser = input.nextLine();
                                                LoginDAO loginDAO = new LoginDAO(c);
                                                Login login = loginDAO.checkPassLog(loginUser, passwordUser);
                                                idEMPLOYEE = login.getId();
                                                if (idEMPLOYEE == 0) { System.out.println("Incorrect, try again");}
                                                else {
                                                        System.out.println("   Hello employee № " + idEMPLOYEE);
                                                        System.out.println();
                                                        correct = true;
                                                }

                                        } while (!correct);

                                        System.out.println("   We are done with the entrance");
                                        System.out.println("________________________________________________________");
                                        System.out.println();
                                        done = true;

                                }

                                // register (doesn't have an account)
                                else if (decision.equals("2")) {

                                        System.out.println("________Lets create your account________");
                                        System.out.println();
                                        System.out.println("   Enter your login");
                                        System.out.println();
                                        input = new Scanner(System.in);
                                        String loginUser;
                                        boolean check = false;

                                        // check login, if doesn't exist => create new account
                                        do {
                                                loginUser = input.nextLine();
                                                System.out.println();
                                                LoginDAO loginDAO = new LoginDAO(c);
                                                Login login = loginDAO.checkLogin(loginUser);
                                                if (login.getLoginUser() == null) {
                                                        System.out.println("   Okay, now password");
                                                        System.out.println();
                                                        check = true;
                                                } else {
                                                        System.out.println("   Incorrect, this login is already taken ");
                                                        System.out.println("   Try again");
                                                }
                                        } while (!check);

                                        String passwordUser = input.nextLine();
                                        System.out.println();

                                        //put it into DB
                                        // create employee table
                                        EmployeeDAO employeeDAO = new EmployeeDAO(c);
                                        Employee employee = new Employee();
                                        employee.setStatus(1);
                                        employee.setPrivilege(0);
                                        employee.setIdDep(0);
                                        employeeDAO.create(employee);
                                        employeeDAO = new EmployeeDAO(c);
                                        employee = new Employee();
                                        employee = employeeDAO.findMaxIdEmp(employee);
                                        idEMPLOYEE = employee.getId();

                                        System.out.println("___Employee table created___");
                                        System.out.println();

                                        // create main info table
                                        MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
                                        MainInfo mainInfo = new MainInfo();
                                        mainInfo.setId(idEMPLOYEE);

                                        System.out.println("   Entry day (MM.dd.yyyy):");

                                        String buffer = input.nextLine();
                                        DateFormat dtFmt = null;
                                        dtFmt = new SimpleDateFormat("MM.dd.yyyy");
                                        java.sql.Date entryD = new Date(dtFmt.parse(buffer).getTime());
                                        mainInfo.setEntryD(entryD);

                                        System.out.println("   Now first, middle and last name:");

                                        String name = input.nextLine();
                                        mainInfo.setFirstName(name);
                                        name = input.nextLine();
                                        mainInfo.setMiddleName(name);
                                        name = input.nextLine();
                                        mainInfo.setLastName(name);

                                        mainInfoDAO.create(mainInfo);
                                        System.out.println();
                                        System.out.println("___Main info table created___");
                                        System.out.println();

                                        // create login table
                                        LoginDAO loginDAO = new LoginDAO(c);
                                        Login login = new Login();
                                        login.setLoginUser(loginUser);
                                        login.setPasswordUser(passwordUser);
                                        login.setId(idEMPLOYEE);

                                        loginDAO.create(login);
                                        System.out.println("___Login created___");
                                        System.out.println();

                                        System.out.println("   Hello, new employee №" + idEMPLOYEE);
                                        System.out.println();
                                        System.out.println("   We're done with registration");
                                        System.out.println("________________________________________________");
                                        System.out.println();
                                        done = true;

                                }
                                else {
                                        System.out.println("   Incorrect decision!! Try again");
                                        System.out.println();

                                }

                        } while (!done);

                        //Show tasks, connected with this login

                        System.out.println("   Today's tasks :");

                        EmployeeTaskDAO employeeTaskDAO = new EmployeeTaskDAO(c);
                        System.out.println();
                        employeeTaskDAO.findByIDList(idEMPLOYEE).forEach(System.out::println);
                        System.out.println();
//____________________(!) need to do additional list of table task, connected with this (emp_task) through id of task
                        // n do check on existing tasks

                        System.out.println("________________________________________________");
                        System.out.println();

                        // events, connected with this login
                        NoticeEventDAO noticeEventDAO = new NoticeEventDAO(c);
                        noticeEventDAO.findByIdList(idEMPLOYEE).forEach(System.out::println);
//____________________(!) need to do additional list for Event, n connect with id of recipient
                        // n check on existing event


                        // old part, but work correctly
//                        System.out.println("   Today's events for you :");
//                        stmt = c.createStatement();
//                        tmp = "SELECT id_event FROM public.notice_event WHERE id_recipient='"
//                                + idEMPLOYEE + "';";
//                        rs = stmt.executeQuery(tmp);
//                        int idEvent = 0;
//                        while (rs.next()) {
//                                idEvent = rs.getInt("id_event");
//                        }
//                        if (idEvent != 0){
//                                tmp = "SELECT date_of_event, type_of_event, comment_fe FROM public.event WHERE id='"
//                                        + idEvent + "';";
//                                rs = stmt.executeQuery(tmp);
//                                while (rs.next()) {
//                                        Date dateOfEvent = rs.getDate("date_of_event");
//                                        String typeOfEvent = rs.getString("type_of_event");
//                                        String commentFE = rs.getString("comment_fe");
//                                        System.out.println("   Event is: " + typeOfEvent);
//                                        System.out.println("   Date: " + dateOfEvent);
//                                        System.out.println("   Comment: " + commentFE);
//                                        System.out.println();
//                                }
//                        } else System.out.println("   There is no events");
                        System.out.println("________________________________________________");







//____________________(!) create log statements, check on existing and connection with table documents
                        // also do types of leave n approve from recipient

                        System.out.println();



                        // old part, but work correctly
//                        stmt = c.createStatement();
//                        tmp = "SELECT id, id_employee, type_leave, date_leave, days_sum, date_of_ls,"
//                                + "comment_ls, approve FROM public.log_statement WHERE id_approver ="
//                                + idEMPLOYEE + ";";
//                        rs = stmt.executeQuery(tmp);
//                        int approveFromApr = 0;
//        //                int idLS = 0;
//                        int idEmployee = 0;
//                        while (rs.next()) {
//      //                          idLS = rs.getInt("id");
//                                idEmployee = rs.getInt("id_employee");
//                                int typeLeave = rs.getInt("type_leave");
//                                int sumOfDays = rs.getInt("days_sum");
//                                int approve = rs.getInt("approve");
//                                Date dateLeave = rs.getDate("date_leave");
//                                Date dateOfLS = rs.getDate("date_of_ls");
//                                String commentLS = rs.getString("comment_ls");
//                                approveFromApr = approve;
//                                if (approveFromApr == 3) {
//                                        System.out.println("   You need to see this statement's:");
//                                        System.out.println("   This employee " + idEmployee + " wants to go on:");
//                                        if (typeLeave == 1) System.out.printf("    vacation ");
//                                        else if (typeLeave == 2) System.out.printf("   sick leave ");
//                                        else if (typeLeave == 3) System.out.printf("   at own expense ");
//                                        else if (typeLeave == 4) System.out.printf("   another ");
//                                        else if (typeLeave == 5) System.out.printf("   leave ");
//                                        System.out.println(dateLeave);
//                                        if (typeLeave <5) System.out.println("   for " + sumOfDays + " days");
//                                        System.out.println("   Day of statement: " + dateOfLS);
//                                        System.out.println("   Comment: " + commentLS);
//                                        System.out.println();
//                                        }
//                        }

                        //approve LS


                        System.out.println("________________________________________________");
                        System.out.println();


                        do {
                                System.out.println("________Main menu________");
                                System.out.println("   Choose what you want to do now:");
                                System.out.println("   1 - Take a look on info about you");
                                System.out.println("   2 - Make log statement");
                                System.out.println("   3 - Create a task");
                                System.out.println("   4 - Create an event");
                                System.out.println("   5 - Make a look on list of employees");
                                System.out.println("   7 - End Session");
                                decision = input.nextLine();


                                switch (decision) {
                                        // Main Info of employee
                                        case "1":
                                                System.out.println("________________________________________________");
                                                EmployeeDAO employeeDAO = new EmployeeDAO(c);
                                                Employee employee = employeeDAO.findById(idEMPLOYEE);
                                                DepartmentDAO departmentDAO = new DepartmentDAO(c);
                                                Department department = departmentDAO.findById(employee.getIdDep());
                                                MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
                                                MainInfo mainInfo = mainInfoDAO.findById(idEMPLOYEE);
                                                ContactDAO contactDAO = new ContactDAO(c);
                                                Contact contact = contactDAO.findById(idEMPLOYEE);
                                                //JobEmployeeDAO
                                                  // solve problem with few titles
                                                System.out.println();
                                                System.out.println("___ Main Info ___");
                                                System.out.println("   Employee " + mainInfo.getFirstName() +
                                                        " " + mainInfo.getMiddleName() + " " +
                                                        mainInfo.getLastName() + ",");
                                                System.out.println("   Status " + employee.getStatus());
                                                System.out.println();
                                                System.out.println("   №" + idEMPLOYEE);
                                                System.out.println("");
                                                System.out.println("_Name of department: "
                                                        + department.getNameDep());
                                                // add job title\titles
                                                //System.out.println("_Job title: " + );
                                                System.out.println("_Location____");
                                                System.out.println("   Work place: " + mainInfo.getCabinetOffice());
                                                System.out.println("   Street: " + contact.getLocationStreet());
                                                System.out.println();
                                                System.out.println("_Contact____");
                                                System.out.println("   Work number: " + contact.getWorkNumber());
                                                System.out.println("   Personal number: " + contact.getPersonalNumber());

                                                System.out.println();
                                                System.out.println(" Date of entry: " + mainInfo.getEntryD());
                                                System.out.println();
                                                System.out.println("________________________________________________");
                                                // maybe need to add smth more?

                                        // Make a log statement
                                        case "2":


                                        // Create a task
                                        case "3":


                                        // Create an event
                                        case "4":


                                        // See all employees
                                        case "5":


                                }


                        }while (!decision.equals("7"));

                        System.out.println();
                        System.out.println("________Have a nice day________");
                        System.out.println("________________________________________________");


                } catch (SQLException e) {
                        System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
                } catch (Exception e) {
                        e.printStackTrace();
                }



         }
}


// need to do crypto on password
// main menu with decisions what client want to do
// create normal readme on git
