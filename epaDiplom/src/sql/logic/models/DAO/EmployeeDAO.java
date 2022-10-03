package sql.logic.models.DAO;

import sql.logic.models.entities.Employee;
import sql.logic.models.entities.EmployeeTask;
import sql.logic.models.util.DataAccessObject;

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
    private static final String GET_ONE_BY_ONE = "SELECT * FROM employee";
    private static final String DELETE = "DELETE FROM employee WHERE id = ?";
    private static final String UPDATE = "UPDATE employee SET privilege = ?, " +
            "id_dep = ? WHERE id = ?";
    private static final String INSERT = "INSERT INTO employee (privilege, id_dep)" +
            "VALUES (?, ?)";
    private static final String GET_LAST_VALUE = "SELECT MAX(id) FROM employee";


    public Employee findMaxIdEmp(Employee employee) {
        try(PreparedStatement statement = this.connection.prepareStatement(GET_LAST_VALUE);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
            employee.setId(rs.getLong("max"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    public List<Employee> findAllInList(){
        List<Employee> employees = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE_BY_ONE);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Employee employee = new Employee();
                employee.setId(rs.getLong("id"));
                employee.setPrivilege(rs.getInt("privilege"));
                employee.setIdDep(rs.getLong("id_dep"));
                employees.add(employee);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employees;
    }

    @Override
    public Employee findById(long id) {
        Employee employee = new Employee();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                employee.setId(rs.getLong("id"));
                employee.setPrivilege(rs.getInt("privilege"));
                employee.setIdDep(rs.getLong("id_dep"));
            }
        }catch (SQLException e){
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
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setInt(1, dto.getPrivilege());
            statement.setLong(2, dto.getIdDep());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();
            employee = this.findById(dto.getId());
        }catch(SQLException e){
            try{
                this.connection.rollback();
            }catch (SQLException sqle){
                e.printStackTrace();
                throw new RuntimeException(sqle);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public Employee create(Employee dto) {
        Employee employee = null;
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setInt(1, dto.getPrivilege());
            statement.setLong(2, dto.getIdDep());
            statement.execute();
            this.connection.commit();
            employee = this.findById(dto.getId());

        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employee;
    }

    @Override
    public void delete(long id) {
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, id);
            statement.executeUpdate();
            this.connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
