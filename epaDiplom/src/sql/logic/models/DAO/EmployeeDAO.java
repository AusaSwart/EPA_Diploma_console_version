package sql.logic.models.DAO;

import sql.logic.models.DAO.entities.*;
import sql.logic.models.DAO.utilDAO.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO extends DataAccessObject<Employee> {

    public EmployeeDAO(Connection connection) {
        super(connection);
    }

    private static final String GET_ONE = "SELECT id, privilege, id_dep " +
            "FROM employee WHERE id=?";
    private static final String GET_ONE_BY_ONE = "SELECT * FROM employee ORDER BY id";
    private static final String DELETE = "DELETE FROM employee WHERE id = ?";
    private static final String UPDATE = "UPDATE employee SET privilege = ?, " +
            "id_dep = ? WHERE id = ?";
    private static final String INSERT = "INSERT INTO employee (privilege, id_dep)" +
            "VALUES (?, ?)";
    private static final String GET_LAST_VALUE = "SELECT MAX(id) FROM employee";
    private static final String GET_BY_DEP = "SELECT id FROM employee WHERE id_dep = ?";
    private static final String GET_BY_PRIV = "SELECT id FROM employee WHERE privilege = ?";
    private static final String GET_ID = "SELECT id FROM employee ORDER BY id";

    public List<Long> findIdList() {
        List<Long> employeeIDs = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ID);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employeeIDs.add(employee.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employeeIDs;
    }

    public List<Long> findIdByPriv(int priv) {
        List<Long> employeeIDs = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_BY_PRIV);) {
            statement.setLong(1, priv);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employeeIDs.add(employee.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employeeIDs;
    }

    public List<Long> findIDByDep(long idDep) {
        List<Long> employeeIDs = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_BY_DEP);) {
            statement.setLong(1, idDep);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employeeIDs.add(employee.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employeeIDs;
    }

    public Employee findMaxIdEmp(Employee employee) {
        try (PreparedStatement statement = this.connection.prepareStatement(GET_LAST_VALUE);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employee.setId(rs.getLong("max"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    public List<Employee> findAllInList() {
        List<Employee> employees = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_BY_ONE);) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setPrivilege(rs.getInt("privilege"));
                employee.setIdDep(rs.getLong("id_dep"));
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee findById(long id) {
        Employee employee = new Employee();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                employee.setId(rs.getLong("id"));
                employee.setPrivilege(rs.getInt("privilege"));
                employee.setIdDep(rs.getLong("id_dep"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public List<Employee> findAll() {
        return null;
    }

    @Override
    public Employee update(Employee dto) {
        Employee employee = null;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
            statement.setInt(1, dto.getPrivilege());
            statement.setLong(2, dto.getIdDep());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();
            employee = this.findById(dto.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public Employee create(Employee dto) {
        Employee employee = null;
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {
            statement.setInt(1, dto.getPrivilege());
            statement.setLong(2, dto.getIdDep());
            statement.execute();
            this.connection.commit();
            employee = this.findById(dto.getId());

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public void delete(long id) {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE);) {
            statement.setLong(1, id);
            statement.executeUpdate();
            this.connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void infoOfEmployee (long idEMPLOYEE, Connection c,int privilege) {
        System.out.println("|-----------------------------------------------------|");
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
        LoginDAO loginDAO = new LoginDAO(c);
        Login login = loginDAO.findById(idEMPLOYEE);
        System.out.println("___ Main Info ___");
        System.out.println("_Employee " + mainInfo.getFirstName() +
                " " + mainInfo.getMiddleName() + " " +
                mainInfo.getLastName() + ",");
        System.out.println(" №" + idEMPLOYEE);
        System.out.println(" Privileges stage: " + employee.getPrivilege());
        System.out.println();
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
        System.out.println("_Date of entry: " + mainInfo.getEntryD());
        System.out.println("_Birth day: " + mainInfo.getBirthD());
        System.out.println();
        if(privilege == 1) {
            System.out.println("_Login____");
            System.out.println("   Login: " + login.getLoginUser());
            System.out.println("   Password: " + login.getPasswordUser());
            System.out.println();
        }
    }

}


