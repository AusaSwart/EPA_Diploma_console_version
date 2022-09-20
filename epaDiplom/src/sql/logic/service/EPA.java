package sql.logic.service;

import sql.logic.models.DAO.*;
import sql.logic.models.entities.*;
import sql.logic.repositoryConnectDB.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class EPA {

        public static void main(String[] args) {

            //connection to DB
        DBConnectionManager dcm = new DBConnectionManager("127.0.0.1:5432",
                "EPA", "postgres", "123qwe");

            //id for connection with an account of employee
        long idEMPLOYEE = 0;
        //Statement stmt = null;

                try {
                        // part with connection to DB
                        Connection c = dcm.getConnection();
                        c.setAutoCommit(false);
                        System.out.println("   Connected to the database!");
                        System.out.println("________________________________________________");

                        //employee greetings
                        System.out.println("________Hello, Employee. Present yourself________");
                        System.out.println("   Please, tape : \n   1 - If you are already have login;");
                        System.out.println("   2 - If you are need to register;");
                        System.out.println();

                        boolean done = false;

                        //main list: login or registration
                        Scanner input = new Scanner(System.in);
                        String decision;
                        do {
                                // login (already have account)
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

                                        System.out.println();
                                        System.out.println("   We are done with the entrance");
                                        System.out.println("________________________________________________________");
                                        System.out.println();
                                        done = true;

                                }

                                // register (doesnt have account)
                                else if (decision.equals("2")) {

                                        System.out.println("________Lets create your account________");
                                        System.out.println();
                                        System.out.println("   Enter your login");
                                        System.out.println();
                                        input = new Scanner(System.in);
                                        String loginUser;
                                        boolean check = false;

                                        // check login, if exist => create new
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
                                        EmployeeDAO employeeDAO = new EmployeeDAO(c);
                                        Employee employee = new Employee();
                                        employee.setStatus(1);
                                        employee.setPrivilege(0);
                                        employee.setIdDep(0);
                                        employeeDAO.create(employee);
                                        employee = new Employee();
                                        employeeDAO.findIdEmp(employee);
                                        idEMPLOYEE = employee.getId();
                                        System.out.println("   " + idEMPLOYEE + " created");
                                        System.out.println();

                                        // create login 'n password
//                                        LoginDAO loginDAO = new LoginDAO(c);
//                                        Login login = new Login();
//                                        login.setLoginUser(loginUser);
//                                        login.setPasswordUser(passwordUser);
//                                        login.setId(idEMPLOYEE);
//                                        loginDAO.create(login);
//                                        System.out.println("   Login created");
//                                        System.out.println();

                                        // create main info
//                                        MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
//                                        MainInfo mainInfo = new MainInfo();
//                                        mainInfo.setId(idEMPLOYEE);
//                                        System.out.println("   Entry day:");

//                                        String buffer = input.nextLine();
//
//                                        SimpleDateFormat format = new SimpleDateFormat();
//                                        format.applyPattern("dd.MM.yyyy");
//                                        Date entryD = format.parse(buffer);
//                                        mainInfo.setEntryD(entryD);
//
//                                        System.out.println("   Now first, middle and last name:");
//
//                                        String name = input.nextLine();
//                                        mainInfo.setFirstName(name);
//                                        name = input.nextLine();
//                                        mainInfo.setMiddleName(name);
//                                        name = input.nextLine();
//                                        mainInfo.setLastName(name);
//
//                                        mainInfoDAO.create(mainInfo);



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

//                        System.out.println("   Today's tasks :");
//
//                        EmployeeTaskDAO employeeTaskDAO = new EmployeeTaskDAO(c);
//                        EmployeeTask employeeTask = employeeTaskDAO.findById(idEMPLOYEE);
//                        System.out.println();
//
//                        // сделать список
//                        System.out.println("   Task you're need to do is №" + employeeTask.getIdTask());
//                        System.out.println("   Comment: " + employeeTask.getCommentTE());
//                        System.out.println("   From employee №" + employeeTask.getIdEmployee());
//                        System.out.println();
//
//                        System.out.println("________________________________________________");
//                        System.out.println();

                        // events, connected with this login
//                        EventDAO eventDAO = new EventDAO(c);
//                        Event event = eventDAO.findById(idEMPLOYEE);
                        // сделать список

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






                        //тоже нужен список
                        // log

                        System.out.println();
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
                                        case "1":
//                                                EmployeeDAO employeeDAO = new EmployeeDAO(c);
//                                                Employee employee = employeeDAO.findById(idEMPLOYEE);
//                                                DepartmentDAO departmentDAO = new DepartmentDAO(c);
//                                                Department department = departmentDAO.findById(employee.getIdDep());
//                                                MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
//                                                MainInfo mainInfo = mainInfoDAO.findById(idEMPLOYEE);
//                                                ContactDAO contactDAO = new ContactDAO(c);
//                                                Contact contact = contactDAO.findById(idEMPLOYEE);
//                                                System.out.println();
         //                                       System.out.println(" " + mainInfo.getFirstName() +
         //                                               " " + );
                                        case "2":

                                        case "3":

                                        case "4":

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


