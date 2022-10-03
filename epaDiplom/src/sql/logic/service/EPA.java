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
                    employee.setIdDep(0);      // 0 - for new employees, after they change it by themselves
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
            if (idT != 0 && (privilege == 1 || privilege == 3)) {
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
                            logStatement = new LogStatement();
                            logStatement = logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            logStatement.setApprove(1); // yes
                            logStatementDAO.updateApprove(logStatement);
                            System.out.println("   Done");
                            doe = true;
                        } else if (decision.equals("n")) {
                            logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            logStatement = new LogStatement();
                            logStatement = logStatementDAO.findByIdForApprove(idEMPLOYEE);
                            logStatement.setApprove(2); // no
                            logStatementDAO.updateApprove(logStatement);
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
                        System.out.println(" №" + idEMPLOYEE);
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
                        System.out.println("   Mail: " + contact.getMail());
                        System.out.println();
                        System.out.println("_Date of entry: " + mainInfo.getEntryD());
                        System.out.println("_Birth day: " + mainInfo.getBirthD());
                        System.out.println();
                        System.out.println("________________________________________________");
                        break;

                    case "2":

                        // Make a log statement

                        System.out.println("________________________________________________");
                        System.out.println();

                        System.out.println("   Who yours boss?");
                        Scanner inputs = new Scanner(System.in);
                        long answerCh = inputs.nextLong();
                        employeeDAO = new EmployeeDAO(c);
                        employee = new Employee();
                        employee = employeeDAO.findById(answerCh);
                        long buff = employee.getId();
                        if (answerCh == buff) {
                            logStatementDAO = new LogStatementDAO(c);
                            logStatement = new LogStatement();
                            logStatement.setIdApprover(answerCh);
                            logStatement.setIdEmployee(idEMPLOYEE);
                            String decisionCase;
                            boolean vac = false;
                            do {
                                System.out.println();
                                System.out.println("   What type of leave do you interested in?");
                                System.out.println("      1 - sick leave");
                                System.out.println("      2 - vacation");
                                System.out.println("      3 - at your own expense");
                                System.out.println("      4 - dismissal");
                                System.out.println("      5 - else");
                                input = new Scanner(System.in);
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
                            input = new Scanner(System.in);
                            int days = input.nextInt();
                            logStatement.setDaysSum(days);               // Sum of days

                            System.out.println();

                            logStatement.setApprove(3);                // Approve

                            System.out.println("   When? (Date format - dd.MM.yyyy) : ");
                            input = new Scanner(System.in);
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
                            LogStatement logStatement1 = logStatementDAO.getIDLS(date, idEMPLOYEE);
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

                        } else {
                            System.out.println("   Wrong id. Try again");
                            System.out.println();
                        }
                        break;

                    case "3":

                        // Create a task

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Let's create task. You need to write in comment" +
                                " what do you need");
                        Date dateT = new Date(System.currentTimeMillis());
                        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                        formatter.format(dateT);
                        TaskDAO taskDAO = new TaskDAO(c);
                        Task task = new Task();
                        task.setDateTask(dateT);
                        taskDAO.create(task);
                        task = new Task();
                        taskDAO = new TaskDAO(c);
                        task = taskDAO.findMaxIdTask(task);
                        long taskID = task.getId();
                        System.out.println("_Task table created____");
                        System.out.println();
                        System.out.println("   Which employees need to do this task?\n" +
                                "   Write few id's and '1', when you're done");
                        long answer;
                        do {
                            employeeDAO = new EmployeeDAO(c);
                            inputs = new Scanner(System.in);
                            answer = inputs.nextLong();
                            employee = new Employee();
                            employee = employeeDAO.findById(answer);
                            long checkTask = employee.getId();
                            String ans;
                            if (answer == checkTask) {
                                employeeTaskDAO = new EmployeeTaskDAO(c);
                                employeeTask = new EmployeeTask();
                                employeeTask.setId(answer);
                                employeeTask.setIdEmployee(idEMPLOYEE);
                                employeeTask.setIdTask(taskID);
                                System.out.println("   Any comments? y/n");
                                input = new Scanner(System.in);
                                ans = input.nextLine();
                                if (ans.equals("y")) {
                                    input = new Scanner(System.in);
                                    comment = input.nextLine();
                                    employeeTask.setCommentTE(comment);
                                } else System.out.println(" Okay");
                                employeeTaskDAO.create(employeeTask);
                                System.out.println("   Table created");
                                System.out.println();
                            } else if (answer == 0) {
                                System.out.println("Okay");
                                System.out.println();
                                break;
                            } else {
                                System.out.println("   Incorrect id");
                                System.out.println();
                            }
                        } while (answer != 1);
                        System.out.println("   Task done");
                        break;

                    case "4":

                        // Create an event

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Lets create a event. First we need to create event,\n" +
                                "   then send them to the employeers.\n   Okay, write type of event:");
                        System.out.println();
                        EventDAO eventDAO = new EventDAO(c);
                        Event event = new Event();
                        input = new Scanner(System.in);
                        String typeOfEvent = input.nextLine();
                        event.setTypeOfEvent(typeOfEvent);
                        System.out.println("   Now short description (comment)");
                        input = new Scanner(System.in);
                        comment = input.nextLine();
                        event.setCommentFE(comment);
                        System.out.println("   And last for this part is date (format - dd.MM.yyyy): ");
                        String buffer = input.next();
                        DateFormat dtFmt = null;
                        dtFmt = new SimpleDateFormat("dd.MM.yyyy");
                        Date dateEvent = new Date(dtFmt.parse(buffer).getTime());
                        event.setDateOfEvent(dateEvent);
                        eventDAO.create(event);
                        eventDAO = new EventDAO(c);
                        event = new Event();
                        eventDAO.findMaxId(event);
                        long idEvent = event.getId();
                        System.out.println("_Table event created____");
                        System.out.println();
                        System.out.println("   Now we need to send these to employees\n" +
                                "Choose for whom");
                        System.out.println("   1 - by department\n" +
                                "   2 - by privilege\n" +
                                "   3 - to all employees");
                        System.out.println();
                        input = new Scanner(System.in);
                        String ans = input.nextLine();
                        boolean com = true;
                        do {
                            switch (ans) {
                                case "1":
                                    System.out.println("0 новенький \n" +
                                            "  1 - ректорат\n" +
                                            "  2 - пресс-служба\n" +
                                            "  3 - сектор предупреждения и ликвидации чрезвычайных ситуацийn" +
                                            "  4 - юридический отдел\n" +
                                            "  5 - центр кадровой работы\n" +
                                            "  6 - отдел по работе с персоналом\n" +
                                            "  7 - отдел документационного обеспечения\\n" +
                                            "  8 - сектор по работе со студентами\n" +
                                            "  9 - бухгалтерия\n" +
                                            "  10 - библиотека\n" +
                                            "  11 - учебно-методическое управление\n" +
                                            "  12 - центр информатизации и инновационных разработок БГУИР\n" +
                                            "  13 - отдел вахтовой службы\n" +
                                            "  14 - центр продвижения образовательных услуг\n" +
                                            "  15 - факультет информационных технологий и управления\n" +
                                            "  16 - факультет радиотехники и электроники\n" +
                                            "  17 - факультет компьютерных систем и сетей\n" +
                                            "  18 - факультет информационной безопасности\n" +
                                            "  19 - инженерно-экономический факультет\n" +
                                            "  20 - факультет доуниверситетской подготовки и профессиональной ориентации\n" +
                                            "  21 - центр материально-технического обеспечения");
                                    System.out.println();
                                    inputs = new Scanner(System.in);
                                    answerCh = inputs.nextLong();
                                    employeeDAO = new EmployeeDAO(c);
                                    List<Long> employeesId = employeeDAO.findIDByDep(answerCh);
                                    for (int i = 0; i < employeesId.size(); i++){
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(employeesId.get(i));
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                    break;

                                case "2":
                                    System.out.println("  1 - admin (can manipulate with another accounts)\n" +
                                            "  2 - common user (can manipulate only own account)\n" +
                                            "  3 - head (can manipulate own accont and also makes approves on logs)");
                                    System.out.println();
                                    inputs = new Scanner(System.in);
                                    int priv = inputs.nextInt();
                                    employeeDAO = new EmployeeDAO(c);
                                    employee = new Employee();
                                    employeeDAO.findIdByPriv(priv);
                                    List<Long> employeesIds = employeeDAO.findIdByPriv(priv);
                                    for (int i = 0; i < employeesIds.size(); i++){
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(employeesIds.get(i));
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                    break;

                                case "3":
                                    employeeDAO = new EmployeeDAO(c);
                                    employee = new Employee();
                                    employeeDAO.findIdList();
                                    List<Long> employeesIdS = employeeDAO.findIdList();
                                    for (int i = 0; i < employeesIdS.size(); i++){
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(employeesIdS.get(i));
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                    break;

                                default:
                                    System.out.println("   Incorrect decision, try else");
                            }
                        } while (com);
                        System.out.println("   We're done");
                        break;

                    case "5":

                        // See all employees

                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   There is list of all employees :");
                        System.out.println();
                        employeeDAO = new EmployeeDAO(c);
//                        mainInfoDAO = new MainInfoDAO(c);
//                        contactDAO = new ContactDAO(c);
                        List<Employee> employees = employeeDAO.findAllInList();
//                        List<MainInfo> mainInfos = mainInfoDAO.findAllInList();
//                        List<Contact> contacts = contactDAO.findAllInList();
//                        for (int i = 0; i < employees.size(); i++ ){
//                            System.out.println(employees.get(i));
//                            System.out.println(mainInfos.get(i));
//                            System.out.println(contacts.get(i));
//                            System.out.println("___________________");
//                        }
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
                        // don't work
                        System.out.println("________________________________________________");
                        System.out.println();
                        System.out.println("   Choose what employee you want to delete?");
                        input = new Scanner(System.in);
                        long deleteEmployee = input.nextLong();
                        if (idEMPLOYEE == deleteEmployee) {
                            System.out.println("   You cannot delete your own account");
                            System.out.println();
                            break;
                        }
                        System.out.println("   Are you sure?\n   y/n");
                        input = new Scanner(System.in);
                        String yesNo = input.nextLine();
                        employeeDAO = new EmployeeDAO(c);
                        employee = employeeDAO.findById(deleteEmployee);
                        long check = employee.getId();
                        if (check == deleteEmployee) {
                            if (yesNo.equals("y") && idEMPLOYEE != check) {
                                contactDAO = new ContactDAO(c);
                                contact = contactDAO.findById(check);
                                if (contact.getId() != 0) {
                                    contactDAO.delete(check);
                                }
                                LoginDAO loginDAO2 = new LoginDAO(c);
                                mainInfoDAO = new MainInfoDAO(c);
                                employeeDAO = new EmployeeDAO(c);
                                loginDAO2.delete(check);
                                mainInfoDAO.delete(check);
                                employeeDAO.delete(check);
                                System.out.println("   Okay, done");
                                System.out.println();
                            } else if (yesNo.equals("n")) {
                                System.out.println("   Okay");
                                System.out.println();
                            } else {
                                System.out.println("   Incorrect decision");
                                System.out.println();
                            }
                            break;
                        } else {
                            System.out.println("   Wrong employee's id");
                            System.out.println();
                        }

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
                            System.out.println("   You can't block this user");
                            break;
                        }
                        System.out.println("   Are you sure?\n   y/n");
                        input = new Scanner(System.in);
                        yesNo = input.nextLine();
                        employeeDAO = new EmployeeDAO(c);
                        employee = new Employee();
                        employee = employeeDAO.findById(blockEmployee);
                        long checkId = employee.getId();
                        if (checkId == blockEmployee) {
                            if (yesNo.equals("y")) {
                                employeeDAO = new EmployeeDAO(c);
                                employee = new Employee();
                                employee = employeeDAO.findById(blockEmployee);
                                employee.setPrivilege(0);
                                employee.setIdDep(employee.getIdDep());
                                employee.setId(blockEmployee);
                                employeeDAO.update(employee);
                                System.out.println("   Employee №" +
                                        blockEmployee +
                                        " have been blocked");
                            } else if (yesNo.equals("n")) {
                                System.out.println("   Okay");
                            } else System.out.println("   Wrong desicion");
                            break;
                        } else {
                            System.out.println("   Such id doesn't exist");
                            System.out.println();
                        }
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
