package sql.logic.models.DAO;

import sql.logic.models.entities.LogStatement;
import sql.logic.models.util.DataAccessObject;

import java.sql.*;
import java.util.List;

public class LogStatementDAO extends DataAccessObject<LogStatement> {

    public LogStatementDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id, id_approver, id_employee, comment_ls, days_sum, " +
            "type_leave, approve, date_leave, date_of_ls FROM log_statement WHERE id=?";
    private static final String DELETE = "DELETE FROM log_statement WHERE id = ?";
    private static final String UPDATE = "UPDATE log_statement SET id_approver = ?, id_employee = ?, comment_ls = ?," +
            " days_sum = ?, type_leave = ?, approve = ?, date_leave = ?, date_of_ls = ?  WHERE id = ?";
    private static final String INSERT = "INSERT INTO log_statement (id_approver, id_employee, comment_ls, days_sum, " +
            "type_leave, approve, date_leave, date_of_ls ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Override
    public LogStatement findById(long id) {
        LogStatement logStatement = new LogStatement();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                logStatement.setId(rs.getLong("id"));
                logStatement.setIdApprover(rs.getLong("id_approver"));
                logStatement.setIdEmployee(rs.getLong("id_employee"));
                logStatement.setCommentLs(rs.getString("comment_ls"));
                logStatement.setDaysSum(rs.getInt("days_sum"));
                logStatement.setTypeLeave(rs.getInt("type_leave"));
                logStatement.setApprove(rs.getInt("approve"));
                logStatement.setDateLeave(rs.getDate("date_leave"));
                logStatement.setDateOfLs(rs.getDate("date_of_ls"));

            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
    }

    @Override
    public List<LogStatement> findAll() {
        return null;
    }

    @Override
    public LogStatement update(LogStatement dto) {
        LogStatement logStatement = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setLong(1, dto.getIdApprover());
            statement.setLong(2, dto.getIdEmployee());
            statement.setString(3, dto.getCommentLs());
            statement.setInt(4, dto.getDaysSum());
            statement.setInt(5, dto.getTypeLeave());
            statement.setInt(6, dto.getApprove());
            statement.setDate(7, (Date) dto.getDateLeave());
            statement.setDate(8, (Date) dto.getDateOfLs());
            statement.setLong(9, dto.getId());
            statement.execute();
            this.connection.commit();
            logStatement = this.findById(dto.getId());
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
        return logStatement;
    }

    @Override
    public LogStatement create(LogStatement dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setLong(1, dto.getIdApprover());
            statement.setLong(2, dto.getIdEmployee());
            statement.setString(3, dto.getCommentLs());
            statement.setInt(4, dto.getDaysSum());
            statement.setInt(5, dto.getTypeLeave());
            statement.setInt(6, dto.getApprove());
            statement.setDate(7, (Date) dto.getDateLeave());
            statement.setDate(8, (Date) dto.getDateOfLs());
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
