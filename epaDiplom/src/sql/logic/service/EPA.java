package sql.logic.service;

import sql.logic.models.DAO.*;
import sql.logic.models.DAO.entities.*;
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
            System.out.println("|-----------------------------------------------------|");

            // Employee greetings

            System.out.println("""
                    |____________Hello, Employee. Present yourself_____________|
                    |   Please, tape :                                         |
                    |  1 - If you are already have login                       |
                    |  2 - If you are need to register                         |
                    |----------------------------------------------------------|""".indent(3));

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
                            System.out.println("|--------------------------------------------------|");
                            System.out.println("|---------- Hello, new employee №" + idEMPLOYEE + " ----------|");
                            System.out.println("|__________________________________________________|");
                            System.out.println("    \\-------We are done with the entrance------/");
                            System.out.println();
                            if (privilege == 0) {
                                System.out.println();
                                System.out.println("""
                                        ++++++++++++++++++++++++++++++
                                        +   You have been blocked    +
                                        ++++++++++++++++++++++++++++++""");

                                System.exit(0);
                            }
                            correct = true;
                        }
                    } while (!correct);
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

                    // Create job employee table

                    JobEmployeeDAO jobEmployeeDAO = new JobEmployeeDAO(c);
                    JobEmployee jobEmployee = new JobEmployee();
                    jobEmployee.setIdJobTitle(212);   // trainee
                    jobEmployee.setId(idEMPLOYEE);
                    jobEmployeeDAO.create(jobEmployee);
                    System.out.println("___Job title created___");
                    System.out.println();

                    // Create contact table

                    ContactDAO contactDAO = new ContactDAO(c);
                    Contact contact = new Contact();
                    System.out.println("   Enter ur mail: ");
                    loginUser = input.nextLine();
                    input = new Scanner(System.in);
                    System.out.println("   Input your work number");
                    long wNum = input.nextLong();
                    System.out.println("   Input your personal number");
                    long pNum = input.nextLong();
                    contact.setLocationStreet(null);
                    contact.setWorkNumber(wNum);
                    contact.setPersonalNumber(pNum);
                    contact.setMail(loginUser);
                    contact.setId(idEMPLOYEE);
                    contactDAO.create(contact);
                    System.out.println("___Contact created___");

                    System.out.println("|----- Hello, new employee №" + idEMPLOYEE + " -----|");
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
                System.out.println("|-----------------------------------------------------|");
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
                System.out.println("|-----------------------------------------------------|");
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
                System.out.println("|-----------------------------------------------------|");
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

            System.out.println("|-----------------------------------------------------|");
            System.out.println();
            do {
                System.out.println("________Main menu________");
                System.out.println();
                System.out.println("""
                        Choose what you want to do now:
                        1 - Take a look on info about you
                        2 - Make a log statement
                        3 - Create a task
                        4 - Create an event
                        5 - Take a look on list of employees
                        6 - Change login n' password, contact's""".indent(3));
                if (privilege == 1) {      // For admin
                    System.out.println("""
                            7(*) - Delete Account
                            8(*) - Correct Info in employee's account
                            9(*) - Block Account""".indent(3));
                }
                System.out.println("     0 - End Session\n");
                System.out.println("|-----------------------------------------------------|");
                input = new Scanner(System.in);
                decision = input.nextLine();

                // Cases

                switch (decision) {

                    case "1":

                        // Main Info for employee about his account

                        EmployeeDAO employeeDAO = new EmployeeDAO(c);
                        employeeDAO.infoOfEmployee(idEMPLOYEE, c);
                        break;

                    case "2":

                        // Make a log statement

                        System.out.println("|-----------------------------------------------------|");
                        System.out.println();

                        System.out.println("   Who is your boss?");
                        Scanner inputs = new Scanner(System.in);
                        long answerCh = inputs.nextLong();
                        employeeDAO = new EmployeeDAO(c);
                        Employee employee = employeeDAO.findById(answerCh);
                        long buff = employee.getId();
                        if (answerCh == buff && buff != 0) {
                            logStatementDAO = new LogStatementDAO(c);
                            logStatement = new LogStatement();
                            logStatement.setIdApprover(answerCh);
                            logStatement.setIdEmployee(idEMPLOYEE);
                            String decisionCase;
                            boolean vac = true;
                            do {
                                System.out.println();
                                System.out.println(""" 
                                        What type of leave do you interested in?
                                          1 - sick leave
                                          2 - vacation
                                          3 - at your own expense
                                          4 - dismissal
                                          5 - else""".indent(3));
                                input = new Scanner(System.in);
                                decisionCase = input.nextLine();
                                switch (decisionCase) {
                                    case "1" -> {
                                        logStatement.setTypeLeave(1);
                                        vac = false;
                                    }
                                    case "2" -> {
                                        logStatement.setTypeLeave(2);
                                        vac = false;
                                    }
                                    case "3" -> {
                                        logStatement.setTypeLeave(3);
                                        vac = false;
                                    }
                                    case "4" -> {
                                        logStatement.setTypeLeave(4);
                                        vac = false;
                                    }
                                    case "5" -> {
                                        logStatement.setTypeLeave(5);
                                        vac = false;
                                    }
                                    default -> System.out.println("   Incorrect");
                                }
                            } while (vac);

                            System.out.println("   Any comments? y/n");
                            input = new Scanner(System.in);
                            decisionCase = input.nextLine();
                            boolean com = true;
                            do {
                                if (decisionCase.equals("y")) {
                                    input = new Scanner(System.in);
                                    comment = input.nextLine();
                                    logStatement.setCommentLs(comment);       // Comment
                                    com = false;
                                    break;
                                } else if (decisionCase.equals("n")) {
                                    System.out.println("   Okay");
                                    logStatement.setCommentLs(null);         // Comment
                                    com = false;
                                    break;
                                } else System.out.println("   Wrong decision. Try again ");
                            } while (com);
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

                        System.out.println("|-----------------------------------------------------|");
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

                        System.out.println("|-----------------------------------------------------|");
                        System.out.println();
                        System.out.println("""
                                Lets create a event. First we need to create event,
                                then send them to the employeers.
                                Okay, write type of event:""".indent(3));
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
                        System.out.println("""   
                                Now we need to send these to employees
                                Choose for whom""".indent(3));
                        System.out.println("""
                                1 - by department
                                2 - by privilege
                                3 - to all employees""".indent(3));
                        System.out.println();
                        input = new Scanner(System.in);
                        String ans = input.nextLine();
                        boolean com = true;
                        do {
                            switch (ans) {
                                case "1" -> {
                                    System.out.println("""
                                            0 - новенький
                                            1 - ректорат
                                            2 - пресс-служба
                                            3 - сектор предупреждения и ликвидации чрезвычайных ситуацийn  
                                            4 - юридический отдел
                                            5 - центр кадровой работы
                                            6 - отдел по работе с персоналом
                                            7 - отдел документационного обеспечения  
                                            8 - сектор по работе со студентами
                                            9 - бухгалтерия
                                            10 - библиотека
                                            11 - учебно-методическое управление
                                            12 - центр информатизации и инновационных разработок БГУИР
                                            13 - отдел вахтовой службы
                                            14 - центр продвижения образовательных услуг
                                            15 - факультет информационных технологий и управления
                                            16 - факультет радиотехники и электроники
                                            17 - факультет компьютерных систем и сетей
                                            18 - факультет информационной безопасности
                                            19 - инженерно-экономический факультет
                                            20 - факультет доуниверситетской подготовки и профессиональной ориентации
                                            21 - центр материально-технического обеспечения""");
                                    System.out.println();
                                    inputs = new Scanner(System.in);
                                    answerCh = inputs.nextLong();
                                    employeeDAO = new EmployeeDAO(c);
                                    List<Long> employeesId = employeeDAO.findIDByDep(answerCh);
                                    for (Long aLong : employeesId) {
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(aLong);
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                }
                                case "2" -> {
                                    System.out.println("""
                                            1 - admin (can manipulate with another accounts)
                                            2 - common user (can manipulate only own account)
                                            3 - head (can manipulate own account and also makes approves on logs)""".indent(2));
                                    System.out.println();
                                    inputs = new Scanner(System.in);
                                    int priv = inputs.nextInt();
                                    employeeDAO = new EmployeeDAO(c);
                                    employee = new Employee();
                                    employeeDAO.findIdByPriv(priv);
                                    List<Long> employeesIds = employeeDAO.findIdByPriv(priv);
                                    for (Long id : employeesIds) {
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(id);
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                }
                                case "3" -> {
                                    employeeDAO = new EmployeeDAO(c);
                                    employee = new Employee();
                                    employeeDAO.findIdList();
                                    List<Long> employeesIdS = employeeDAO.findIdList();
                                    for (Long id : employeesIdS) {
                                        noticeEventDAO = new NoticeEventDAO(c);
                                        noticeEvent = new NoticeEvent();
                                        noticeEvent.setId(id);
                                        noticeEvent.setIdEvent(idEvent);
                                        noticeEvent.setIdEmployee(idEMPLOYEE);
                                        noticeEventDAO.create(noticeEvent);
                                        System.out.println("_Table Notice event created____");
                                    }
                                    com = false;
                                }
                                default -> System.out.println("   Incorrect decision, try else");
                            }
                        } while (com);
                        System.out.println("   We're done");
                        break;

                    case "5":

                        // See all employees

                        System.out.println("|-----------------------------------------------------|");
                        System.out.println();
                        System.out.println("   There is list of all employees :");
                        System.out.println();
                        employeeDAO = new EmployeeDAO(c);
                        MainInfoDAO mainInfoDAO = new MainInfoDAO(c);
                        ContactDAO contactDAO = new ContactDAO(c);
                        JobEmployeeDAO jobEmployeeDAO = new JobEmployeeDAO(c);
                        List<Employee> employees = employeeDAO.findAllInList();
                        List<Long> employeesID = employeeDAO.findIdList();
                        List<MainInfo> mainInfos = mainInfoDAO.findAllInList();
                        List<Contact> contacts = contactDAO.findAllInList();
                        for (int i = 0; i < employees.size(); i++) {
                            System.out.println("|------------------------------|");
                            System.out.println(employees.get(i));
                            System.out.println(mainInfos.get(i));
                            System.out.println(contacts.get(i));
                            System.out.println("___Job title:");
                            JobEmployee jobEmployee = jobEmployeeDAO.findComplicatedReqFJ(employeesID.get(i));
                            jobEmployee.getJobTitles().forEach(System.out::println);
                            System.out.println();
                            System.out.println("|------------------------------|");
                        }
                        System.out.println();
                        System.out.println("   This is end of list");
                        System.out.println();
                        break;

                    case "6":

                        // Change login\\password && contact's

                        System.out.println("|-----------------------------------------------------|");
                        System.out.println();
                        System.out.println("   You want to change login/password (1) or contacts (2)?");
                        input = new Scanner(System.in);
                        ans = input.nextLine();
                        switch (ans) {
                            case "1" -> {
                                LoginDAO loginDAO = new LoginDAO(c);
                                Login login = new Login();
                                String loginUser;
                                boolean check = true;
                                do {
                                    System.out.println("  Input new login:");
                                    loginUser = input.nextLine();
                                    login = loginDAO.checkLogin(loginUser);
                                    if (login.getLoginUser() == null) {
                                        System.out.println("   Okay, now password");
                                        System.out.println();
                                        check = false;
                                    } else {
                                        System.out.println("   Incorrect, this login is already taken ");
                                        System.out.println("   Try again");
                                    }
                                } while (check);
                                System.out.println("  New password:");
                                String passwordUser = input.nextLine();
                                login = new Login();
                                login.setLoginUser(loginUser);
                                login.setPasswordUser(passwordUser);
                                login.setId(idEMPLOYEE);
                                loginDAO.update(login);
                                System.out.println("___ Table login updated _____");
                            }
                            case "2" -> {
                                contactDAO = new ContactDAO(c);
                                Contact contact = new Contact();
                                System.out.println();
                                String mail;
                                String street;
                                boolean check = true;
                                do {
                                    System.out.println("  Input new mail:");
                                    mail = input.nextLine();
                                    contact = contactDAO.checkMail(mail);
                                    if (contact.getMail() == null) {
                                        System.out.println("   Okay, now work number");
                                        contact = contactDAO.findById(idEMPLOYEE);
                                        street = contact.getLocationStreet();
                                        input = new Scanner(System.in);
                                        long wNum = input.nextLong();
                                        System.out.println("  Personal number:");
                                        input = new Scanner(System.in);
                                        long pNum = input.nextLong();
                                        contact.setLocationStreet(street);
                                        contact.setWorkNumber(wNum);

                                        contact.setPersonalNumber(pNum);
                                        contact.setMail(mail);
                                        contact.setId(idEMPLOYEE);
                                        contactDAO.update(contact);
                                        System.out.println("___ Table contact updated _____");
                                        check = false;
                                    } else {
                                        System.out.println("   Incorrect, this mail is already taken ");
                                        System.out.println("   Try again");
                                    }
                                } while (check);
                            }
                            default -> {
                                System.out.println("  Incorrect decision");
                            }
                        }
                        break;

                    case "7":

                        // Delete employee's account (Admin)

                        System.out.println("|-----------------------------------------------------|");
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
                            if (yesNo.equals("y")) {
                                contactDAO = new ContactDAO(c);
                                Contact contact = contactDAO.findById(check);
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

                        System.out.println("|-----------------------------------------------------|");
                        System.out.println();
                        System.out.println("  First, which employee you want to update?");
                        inputs = new Scanner(System.in);
                        answerCh = inputs.nextLong(); // here id of employee
                        employeeDAO = new EmployeeDAO(c);
                        System.out.println("   So, there's info about him: ");
                        employeeDAO.infoOfEmployee(answerCh, c);
                        com = true;
                        do {
                            System.out.println("""
                                    |------------------------------------------------------|
                                    | What part of employee's account you want to update?  
                                    |  1 - Depertment && Privilege's
                                    |  2 - Job title
                                    |  3 - Password
                                    |  4 - Exit in main menu
                                    |------------------------------------------------------|
                                    """.indent(3));
                            ans = input.nextLine();
                            switch (ans) {
                                case "1" -> { //1 - Depertment
                                    DepartmentDAO departmentDAO = new DepartmentDAO(c);
                                    departmentDAO.findAll().forEach(System.out::println);
                                    System.out.println("""
                                               Here's examples of all departments at this moment. Choose id
                                               of department in which you want to define an employee""".indent(3));
                                    inputs = new Scanner(System.in);
                                    long ids = inputs.nextLong();
                                    System.out.println("""
                                            There's type of privilege's:
                                            0 - account blocked (can't do anything)
                                            1 - admin (can manipulate with another accounts)
                                            2 - common user (can manipulate only own account)
                                            3 - head (can manipulate own accont and also makes approves on logs
                                            
                                            Choose number of privileges, what you want to set:""".indent(3));
                                    inputs = new Scanner(System.in);
                                    int priv = inputs.nextInt();
                                    employee = employeeDAO.findById(answerCh);
                                    employee.setPrivilege(priv);
                                    employee.setId(answerCh);
                                    employee.setIdDep(ids);
                                    employeeDAO.update(employee);
                                    System.out.println("   Privileges and Department are changed");
                                }
                                case "2" -> { //2 - Job title


                                    // problem with few tables and unique key's

                                    jobEmployeeDAO = new JobEmployeeDAO(c);
                                    JobEmployee jobEmployee = new JobEmployee();
                                    System.out.println("   Here's ll Job title's, choose needed one:");
                                    JobTitleDAO jodTitleDAO = new JobTitleDAO(c);
                                    jodTitleDAO.findAll().forEach(System.out::println);
                                    com = true;
                                    do {
                                        System.out.println();
                                        inputs = new Scanner(System.in);
                                        long ids = inputs.nextLong();
                                        jobEmployeeDAO = new JobEmployeeDAO(c);
                                        jobEmployee = new JobEmployee();
                                        jobEmployee.setIdJobTitle(ids);
                                        jobEmployee.setId(answerCh);
                                        jobEmployeeDAO.update(jobEmployee);
                                        System.out.println("   Name of job are updated, do you need one more title? y/n");
                                        input = new Scanner(System.in);
                                        ans = input.nextLine();
                                        if(ans.equals("n")){  com = false; }
                                        else {
                                            System.out.println("   Okay");
                                        }
                                    } while (com);
                                }
                                case "3" -> { //3 - Password
                                    LoginDAO loginDAO = new LoginDAO(c);
                                    System.out.println("   Write new password:");
                                    Login login = loginDAO.findById(answerCh);
                                    String loginUs = login.getLoginUser();
                                    input = new Scanner(System.in);
                                    String passwordUs = input.nextLine();
                                    login.setPasswordUser(passwordUs);
                                    login.setLoginUser(loginUs);
                                    login.setId(answerCh);
                                    loginDAO.update(login);
                                    System.out.println("   Password updated");
                                }
                                case "4" -> com = false; //7 - Exit in main menu
                                default -> System.out.println("   Incorrect decision");
                            }
                        } while (com);
                        break;

                    case "9":

                        // Block employee's account (Admin)

                        System.out.println("|-----------------------------------------------------|");
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
            System.out.println("|-------------------Have a nice day-------------------|");
            System.out.println("|-----------------------------------------------------|");

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


