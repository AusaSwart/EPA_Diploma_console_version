package sql.logic.models.DAO;

import sql.logic.models.entities.EmployeeTask;
import sql.logic.models.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class EmployeeTaskDAO extends DataAccessObject<EmployeeTask> {

    public EmployeeTaskDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_executor, id_employee, id_task," +
            " comment_te FROM emp_task WHERE id_executor=?";
    private static final String DELETE = "DELETE FROM emp_task WHERE id_executor = ?";
    private static final String UPDATE = "UPDATE emp_task SET id_employee = ?, id_task = ?, " +
            "comment_te = ? WHERE id_executor = ?";
    private static final String INSERT = "INSERT INTO emp_task (id_employee, id_task," +
        " comment_te) VALUES (?)";

    @Override
    public EmployeeTask findById(long id) {
        EmployeeTask employeeTask = new EmployeeTask();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                employeeTask.setId(rs.getLong("id_executor"));
                employeeTask.setIdEmployee(rs.getLong("id_employee"));
                employeeTask.setIdTask(rs.getLong("id_task"));
                employeeTask.setCommentTE(rs.getString("comment_te"));

            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return employeeTask;
    }

    @Override
    public List<EmployeeTask> findAll() {
        return null;
    }

    @Override
    public EmployeeTask update(EmployeeTask dto) {
        EmployeeTask employeeTask = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setLong(1, dto.getIdEmployee());
            statement.setLong(2, dto.getIdTask());
            statement.setString(2, dto.getCommentTE());
            statement.setLong(2, dto.getId());
            statement.execute();
            this.connection.commit();
            employeeTask = this.findById(dto.getId());
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
        return employeeTask;
    }


    @Override
    public EmployeeTask create(EmployeeTask dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setLong(1, dto.getIdEmployee());
            statement.setLong(2, dto.getIdTask());
            statement.setString(3, dto.getCommentTE());
            statement.execute();
            int id = this.getLastVal(EMPLOYEE_SEQUENCE);
            return this.findById(id);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, id);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
