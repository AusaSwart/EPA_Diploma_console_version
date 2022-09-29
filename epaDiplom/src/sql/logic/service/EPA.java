package sql.logic.service;

import sql.logic.models.DAO.*;
import sql.logic.models.entities.*;
import sql.logic.repositoryConnectDB.DBConnectionManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Scanner;

public class EPA {

    public static void main(String[] args) {

        // Connection to DB

        DBConnectionManager dcm = new DBConnectionManager("127.0.0.1:5432",
                "EPA", "postgres", "123qwe");

        // id and his privileges for connection with an account of employee

        long idEMPLOYEE = 0;
        int privilege = 0;

        // Privilege description
        // 0 - account blocked (can't do anything)
        // 1 - admin (can manipulate with another accounts)
        // 2 - common user (can manipulate only own account)
        // 3 - head (can manipulate own accont and also makes approves on logs)

        try {

            // Part with connection to DB

            Connection c = dcm.getConnection();
            c.setAutoCommit(false);
            System.out.println("   Connected to the database!");
            System.out.println("________________________________________________");

            // Employee greetings

            System.out.println("________Hello, Employee. Present yourself________");
            System.out.println("   Please, tape : \n   1 - If you are already have login;");
            System.out.println("   2 - If you are need to register;");
            System.out.println();

            boolean done = false;

            // First step: login or registration

            Scanner input = new Scanner(System.in);
            String decision;
            String comment;
            do {

                // Login (already have an account)

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

                        EmployeeDAO employeeDAO = new EmployeeDAO(c);
                        Employee employee = employeeDAO.findById(idEMPLOYEE);
                        privilege = employee.getPrivilege();

                        if (idEMPLOYEE == 0) {
                            System.out.println("Incorrect, try again");
                        } else {
                            System.out.println("+++++ Hello, new employee №" + idEMPLOYEE + " +++++");
                            System.out.println();
                            if (privilege == 0) {
                                System.out.println();
                                System.out.println("++++++++++++++++++++++++++++++");
                                System.out.println("   You have been blocked ");
                                System.out.println("++++++++++++++++++++++++++++++");
                                System.exit(0);
                            }
                            correct = true;
                        }
                    } while (!correct);

                    System.out.println("|___We are done with the entrance___|");
                    done = true;

                }

                // Register (doesn't have an account)

                else if (decision.equals("2")) {
                    System.out.println("________Lets create your account________");
                    System.out.println();
                    System.out.println("   Enter your login");
                    System.out.println();
                    input = new Scanner(System.in);
                    String loginUser;
                    boolean check = false;

                    // Check login, if it doesn't exist => create new account

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

                    // Put it into DB
                    // Create employee table

                    EmployeeDAO employeeDAO = new EmployeeDAO(c);
                    Employee employee = new Employee();
                    employee.setPrivilege(2);  // 2 - for common users, this can be changed only by admin
                    employee.setIdDep(0);    // 0 - for new employees, after they change it by themselves
                    employeeDAO.create(employee);
                    employeeDAO = new EmployeeDAO(c);
                    employee = new Employee();
                    employee = employeeDAO.findMaxIdEmp(employee);
                    idEMPLOYEE = employee.getId();

                    System.out.println("___Employee table created___");
                    System.out.println();

                    // Create main info table

                    MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
                    MainInfo mainInfo = new MainInfo();
                    mainInfo.setId(idEMPLOYEE);
                    System.out.println("   Entry day (dd.MM.yyyy):");
                    String buffer = input.nextLine();
                    DateFormat dtFmt = null;
                    dtFmt = new SimpleDateFormat("dd.MM.yyyy");
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

                    // Create login table

                    LoginDAO loginDAO = new LoginDAO(c);
                    Login login = new Login();
                    login.setLoginUser(loginUser);
                    login.setPasswordUser(passwordUser);
                    login.setId(idEMPLOYEE);
                    loginDAO.create(login);
                    System.out.println("___Login created___");
                    System.out.println();

                    System.out.println("+++++ Hello, new employee №" + idEMPLOYEE + " +++++");
                    System.out.println();
                    System.out.println("|__ We're done with registration __|");
                    done = true;
                } else {
                    System.out.println("   Incorrect decision!! Try again");
                    System.out.println();
                }
            } while (!done);

            // Show tasks, connected with this login

            EmployeeTaskDAO employeeTaskDAO = new EmployeeTaskDAO(c);
            EmployeeTask employeeTask = employeeTaskDAO.findById(idEMPLOYEE);
            long idT = employeeTask.getIdTask();
            if (idT != 0) {
                System.out.println("________________________________________________");
                System.out.println();
                System.out.println("   Today's tasks :");
                System.out.println();
                employeeTask = employeeTaskDAO.findComplicatedReq(idEMPLOYEE);
                for (int i = 0; i < employeeTask.getEmployeeTasks().size(); i++) {
                    System.out.println(employeeTask.getEmployeeTasks().get(i));
                    System.out.println(employeeTask.getTasks().get(i));
                }
            }

            // Events, connected with this login

            NoticeEventDAO noticeEventDAO = new NoticeEventDAO(c);
            NoticeEvent noticeEvent = noticeEventDAO.findById(idEMPLOYEE);
            idT = noticeEvent.getIdEvent();
            if (idT != 0) {
                System.out.println("________________________________________________");
                System.out.println();
                noticeEvent = noticeEventDAO.findComplicatedReqFE(idEMPLOYEE);
                for (int i = 0; i < (noticeEvent.getNoticeEvents().size()); i++) {
                    System.out.println(noticeEvent.getNoticeEvents().get(i));
                    System.out.println(noticeEvent.getEvents().get(i));
                }
            }

            // Log statement approves n' notices about them

            // 1 - approved
            // 2 - dismiss
            // 3 - need answer

            LogStatementDAO logStatementDAO = new LogStatementDAO(c);
            LogStatement logStatement = logStatementDAO.findById(idEMPLOYEE);
            idT = logStatement.getId();
            if (idT != 0) {
                System.out.println("________________________________________________");
                System.out.println();
                boolean doe;
                logStatement = logStatementDAO.findComplicatedReqLS(idEMPLOYEE);
                for (int i = 0; i < (logStatement.getLogStatements().size()); i++) {
                    System.out.println(logStatement.getLogStatements().get(i));
                    System.out.println(logStatement.getDocuments().get(i));

                    // Approve this statement
                    doe = false;
                    do {
                        System.out.println("   Approve this statement? y/n");
                        decision = input.nextLine();
                        if (decision.equals("y")) {
                            LogStatement logStatement1 = logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            logStatement1.setApprove(1); // yes
                            logStatementDAO.updateApprove(logStatement1);
                            System.out.println("   Done");
                            doe = true;
                        } else if (decision.equals("n")) {
                            logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            LogStatement logStatement1 = logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            logStatement1.setApprove(2); // no
                            logStatementDAO.updateApprove(logStatement1);
                            System.out.println("   Done");
                            doe = true;
                        } else System.out.println(" Incorrect, try again");

                    } while (!doe);
                }
            }

            // Main menu with available actions

            System.out.println("________________________________________________");
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
            System.out.println();
            do {
                System.out.println("________Main menu________");
                System.out.println();
                System.out.println("   Choose what you want to do now:");
                System.out.println("     1 - Take a look on info about you");
                System.out.println("     2 - Make a log statement");
                System.out.println("     3 - Create a task");
                System.out.println("     4 - Create an event");
                System.out.println("     5 - Take a look on list of employees");
                System.out.println("     6 - Update info about you");
                if (privilege == 1) {      // For admin
                    System.out.println("     7(*) - Delete Account");
                    System.out.println("     8(*) - Correct Info in Account");
                    System.out.println("     9(*) - Block Account"); //(?)
                }
                System.out.println("     0 - End Session");
                System.out.println();
                System.out.println("________________________________________________");
                input = new Scanner(System.in);
                decision = input.nextLine();

                // Cases

                switch (decision) {

                    case "1":

                        // Main Info for employee about his account

                        System.out.println("________________________________________________");
                        System.out.println();
                        EmployeeDAO employeeDAO = new EmployeeDAO(c);
                        Employee employee = employeeDAO.findById(idEMPLOYEE);
                        DepartmentDAO departmentDAO = new DepartmentDAO(c);
                        Department department = departmentDAO.findById(employee.getIdDep());
                        MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
                        MainInfo mainInfo = mainInfoDAO.findById(idEMPLOYEE);
                        ContactDAO contactDAO = new ContactDAO(c);
                        Contact contact = contactDAO.findById(idEMPLOYEE);
                        JobEmployeeDAO jobEmployeeDAO = new JobEmployeeDAO(c);
                        JobEmployee jobEmployee = jobEmployeeDAO.findComplicatedReqFJ(idEMPLOYEE);

                        System.out.println("___ Main Info ___");
                        System.out.println("_Employee " + mainInfo.getFirstName() +
                                " " + mainInfo.getMiddleName() + " " +
                                mainInfo.getLastName() + ",");
                        System.out.println();
                        System.out.println("   №" + idEMPLOYEE);
                        System.out.println("");
                        System.out.println("_Name of department: "
                                + department.getNameDep());
                        jobEmployee.getJobTitles().forEach(System.out::println);
                        System.out.println();
                        System.out.println("_Location____");
                        System.out.println("   Work place: " + mainInfo.getCabinetOffice());
                        System.out.println("   Street: " + contact.getLocationStreet());
                        System.out.println();
                        System.out.println("_Contact____");
                        System.out.println("   Work number: " + contact.getWorkNumber());
                        System.out.println("   Personal number: +" + contact.getPersonalNumber());
                        System.out.println();
                        System.out.println("_Date of entry: " + mainInfo.getEntryD());
                        System.out.println();
                        System.out.println("________________________________________________");
                        break;

                    case "2":

                        // Make a log statement

                        System.out.println("________________________________________________");
                        System.out.println();


                        logStatementDAO = new LogStatementDAO(c);
                        logStatement = new LogStatement();
                        logStatement.setIdApprover(1000004);          // idApprover    //he's dean, but it's temporarily
                        logStatement.setIdEmployee(idEMPLOYEE);       // idEmployee    // We set chef when we have normal info
                        String decisionCase;
                        boolean vac = false;
                        do {
                            System.out.println("   What type of leave do you interested in?");
                            System.out.println("      1 - sick leave");
                            System.out.println("      2 - vacation");
                            System.out.println("      3 - at your own expense");
                            System.out.println("      4 - dismissal");
                            System.out.println("      5 - else");
                            decisionCase = input.nextLine();
                            switch (decisionCase) {
                                case "1" -> {
                                    logStatement.setTypeLeave(1);
                                    vac = true;
                                }
                                case "2" -> {
                                    logStatement.setTypeLeave(2);
                                    vac = true;
                                }
                                case "3" -> {
                                    logStatement.setTypeLeave(3);
                                    vac = true;
                                }
                                case "4" -> {
                                    logStatement.setTypeLeave(4);
                                    vac = true;
                                }
                                case "5" -> {
                                    logStatement.setTypeLeave(5);
                                    vac = true;
                                }
                                default -> throw new IllegalStateException("Unexpected value: " + decisionCase);
                            }
                        } while (!vac);

                        System.out.println("   Any comments? y/n");
                        input = new Scanner(System.in);
                        decisionCase = input.nextLine();
                        boolean com = false;
                        do {
                            if (decisionCase.equals("y")) {
                                input = new Scanner(System.in);
                                comment = input.nextLine();
                                logStatement.setCommentLs(comment);       // Comment
                                com = true;
                                break;
                            } else if (decisionCase.equals("n")) {
                                System.out.println("   Okay");
                                logStatement.setCommentLs(null);         // Comment
                                com = true;
                                break;
                            } else System.out.println("   Wrong decision. Try again ");
                        } while (!com);
                        System.out.println();
                        System.out.println("   Period? (sum of days): ");
                        int days = input.nextInt();

                        logStatement.setDaysSum(days);               // Sum of days

                        System.out.println();

                        logStatement.setApprove(3);                // Approve

                        System.out.println("   When? (Date format - dd.MM.yyyy) : ");
                        String buffer = input.next();
                        DateFormat dtFmt = null;
                        dtFmt = new SimpleDateFormat("dd.MM.yyyy");
                        Date dateLeave = new Date(dtFmt.parse(buffer).getTime());
                        logStatement.setDateLeave(dateLeave);      // Date Leave

                        Date date = new Date(System.currentTimeMillis());
                        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        formatter.format(date);
                        logStatement.setDateOfLs(date);            // Date of LS

                        logStatementDAO.create(logStatement); //_____LogStatement created_____

                        System.out.println();
                        System.out.println("   Do you have any documents?");
                        input = new Scanner(System.in);
                        decisionCase = input.nextLine();
                        long idLS = 0;
                        LogStatement logStatement1 = logStatementDAO.getIDLS( date, idEMPLOYEE);
                        idLS = logStatement1.getId();
                        if (decisionCase.equals("y") && idLS != 0) {
                            DocumentDAO documentDAO = new DocumentDAO(c);
                            Document document = new Document();
                            System.out.println("   Write path for your doc");
                            input = new Scanner(System.in);
                            comment = input.nextLine();
                            document.setBodyDoc(comment);
                            document.setId_LS(idLS);
                            documentDAO.create(document); //_____Document created_______
                            System.out.println("   Document created");
                            break;
                        } else if (decisionCase.equals("n")) {
                            System.out.println("   Okay");
                            break;
                        } else System.out.println("   Wrong decision. Try again, ");

                        break;

                    case "3":

                        // Create a task

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Lets create task. You need to write in comment" +
                                " what do you need");
                        Date dateT = new Date(System.currentTimeMillis());
                        formatter = new SimpleDateFormat("dd.MM.yyyy");
                        formatter.format(dateT);
                        TaskDAO taskDAO = new TaskDAO(c);
                        Task task1 = new Task();
                        task1.setDateTask(dateT);
                        taskDAO.create(task1);
                        task1 = taskDAO.findMaxIdTask(task1);
                        long taskID = task1.getId();
                        System.out.println("_Task table created____");
                        comment = input.nextLine();
                        //EmployeeTaskDAO employeeTaskDAO1 = new EmployeeTaskDAO(c);
                        //System.out.println("   Which employees need to do this task?");


                        // need to create task on few employees


                        break;

                    case "4":

                        // Create an event

                        System.out.println("________________________________________________");
                        System.out.println();


                    case "5":

                        // See all employees

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   There is list of all employees :");
                        System.out.println();
                        EmployeeDAO employeeDAO3 = new EmployeeDAO(c);
                        List<Employee> employees = employeeDAO3.findAllInList();
                        employees.forEach(System.out::println);
                        System.out.println();
                        System.out.println("   This is end of list");
                        System.out.println();
                        break;

                    case "6":

                        // Update info about personal account

                        System.out.println("________________________________________________");
                        System.out.println();

                        break;

                    case "7":

                        // Delete employee's account (Admin)

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Choose what employee you want to delete?");
                        input = new Scanner(System.in);
                        long deleteEmployee = input.nextLong();
                        System.out.println("   Are you sure?\n   y/n");
                        input = new Scanner(System.in);
                        String yesNo = input.nextLine();
                        if (yesNo.equals("y") && idEMPLOYEE != deleteEmployee) {
                            ContactDAO contactDAO2 = new ContactDAO(c);
                            Contact contact2 = contactDAO2.findById(deleteEmployee);
                            if (contact2.getId() != 0) {
                                contactDAO2.delete(deleteEmployee);
                            }
                            LoginDAO loginDAO2 = new LoginDAO(c);
                            loginDAO2.delete(deleteEmployee);
                            MainInfoDAO mainInfoDAO2 = new MainInfoDAO(c);
                            mainInfoDAO2.delete(deleteEmployee);
                            EmployeeDAO employeeDAO2 = new EmployeeDAO(c);
                            employeeDAO2.delete(deleteEmployee);
                            System.out.println("   Okay, done");
                        } else if (idEMPLOYEE == deleteEmployee) {
                            System.out.println("   You cannot delete your own account");
                        } else if (yesNo.equals("n")) {
                            System.out.println("   Okay");
                        } else {
                            System.out.println("   Incorrect decision");
                        }
                        break;

                    case "8":

                        // Correct Info in employee's account (Admin)

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   ");


                        break;

                    case "9":

                        // Block employee's account (Admin)

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Whose account you want to block?");
                        input = new Scanner(System.in);
                        long blockEmployee = input.nextLong();
                        if (blockEmployee == 1000000) {
                            System.out.println("   You can't delete this user");
                            break;
                        }
                        System.out.println("   Are you sure?\n   y/n");
                        input = new Scanner(System.in);
                        yesNo = input.nextLine();
                        if (yesNo.equals("y")) {
                            EmployeeDAO employeeDAO1 = new EmployeeDAO(c);
                            Employee employee1 = employeeDAO1.findById(blockEmployee);
                            //check jo existing
                            employee1.setPrivilege(0);
                            employee1.setIdDep(employee1.getIdDep());
                            employee1.setId(blockEmployee);
                            employeeDAO1.update(employee1);
                            System.out.println("   Employee №" +
                                    blockEmployee +
                                    " have been blocked");
                        } else if (yesNo.equals("n")) {
                            System.out.println("   Okay");
                        } else System.out.println("   Wrong desicion");
                        break;
                }

                // Exit from app

            } while (!decision.equals("0"));

            System.out.println();
            System.out.println("|________Have a nice day________|");
            System.out.println("________________________________________________");

            //  Block of exceptions

        } catch (
                SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (
                Exception e) {
            e.printStackTrace();
        }


    }
}
