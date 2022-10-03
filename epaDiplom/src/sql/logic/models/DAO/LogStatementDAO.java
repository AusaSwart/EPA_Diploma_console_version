package sql.logic.models.DAO;

import sql.logic.models.DAO.entities.Document;
import sql.logic.models.DAO.entities.LogStatement;
import sql.logic.models.DAO.utilDAO.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LogStatementDAO extends DataAccessObject<LogStatement> {
    public LogStatementDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id, id_approver, id_employee, comment_ls, days_sum, " +
            "type_leave, approve, date_leave, date_of_ls FROM log_statement WHERE id_approver = ?";
    private static final String DELETE = "DELETE FROM log_statement WHERE id = ?";
    private static final String UPDATE = "UPDATE log_statement SET id_approver = ?, id_employee = ?, comment_ls = ?," +
            " days_sum = ?, type_leave = ?, approve = ?, date_leave = ?, date_of_ls = ?  WHERE id = ?";
    private static final String INSERT = "INSERT INTO log_statement (id_approver, id_employee, comment_ls, days_sum, " +
            "type_leave, approve, date_leave, date_of_ls ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_COMPLICATED_LS = " SELECT ls.id, ls.id_approver, ls.id_employee, ls.comment_ls," +
            " ls.days_sum, ls.type_leave, ls.approve, ls.date_leave, ls.date_of_ls, d.body_doc, d.id " +
            "FROM public.log_statement ls JOIN public.document d ON ls.id = d.id_ls WHERE id_approver = ? " +
            "AND approve = 3 ORDER BY ls.id ASC";
    private static final String GET_ONE_APPR = "SELECT id, id_approver, approve FROM log_statement " +
            "WHERE id_approver = ? AND approve = 3 ORDER BY id DESC";
    private static final String UPDATE_APPROVE = "UPDATE log_statement SET approve = ?  WHERE id = ?";
    private static final String GET_ID_FROM_DATE = "SELECT MAX(id) FROM log_statement WHERE" +
            " id_employee = ? AND date_of_ls = ?";

    public LogStatement findComplicatedReqLS(long id) {
        LogStatement logStatement = new LogStatement();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_COMPLICATED_LS);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<LogStatement> logStatements = new ArrayList<>();
            List<Document> documents = new ArrayList<>();
            while (rs.next()) {
                LogStatement logStatement1 = new LogStatement();
                Document document = new Document();
                logStatement1.setId(rs.getLong(1));
                logStatement1.setIdApprover(rs.getLong(2));
                logStatement1.setIdEmployee(rs.getLong(3));
                logStatement1.setCommentLs(rs.getString(4));
                logStatement1.setDaysSum(rs.getInt(5));
                logStatement1.setTypeLeave(rs.getInt(6));
                logStatement1.setApprove(rs.getInt(7));
                logStatement1.setDateLeave(rs.getDate(8));
                logStatement1.setDateOfLs(rs.getDate(9));
                document.setBodyDoc(rs.getString(10));
                document.setId(rs.getLong(11));
                document.setId_LS(rs.getLong(1));
                documents.add(document);
                logStatements.add(logStatement1);

            }
            logStatement.setLogStatements(logStatements);
            logStatement.setDocuments(documents);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
    }

    public LogStatement getIDLS( java.sql.Date date, long idEMPLOYEE) {
        LogStatement logStatement = new LogStatement();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ID_FROM_DATE);) {
            statement.setLong(1, idEMPLOYEE);
            statement.setDate(2, date);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                logStatement.setId(rs.getLong("max"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
    }
    public LogStatement findByIdForApprove(long id_approver) {
        LogStatement logStatement = new LogStatement();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE_APPR);) {
            statement.setLong(1, id_approver);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                logStatement.setId(rs.getLong("id"));
                logStatement.setIdApprover(rs.getLong("id_approver"));
                logStatement.setApprove(rs.getInt("approve"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
    }

    @Override
    public LogStatement findById(long id_approver) {
        LogStatement logStatement = new LogStatement();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id_approver);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
    }

    public LogStatement updateApprove(LogStatement dto) {
        LogStatement logStatement = null;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_APPROVE);) {
            statement.setInt(1, dto.getApprove());
            statement.setLong(2, dto.getId());
            statement.execute();
            this.connection.commit();
            logStatement = this.findById(dto.getId());
        } catch (SQLException e) {
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
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
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
        } catch (SQLException e) {
            try {
                this.connection.rollback();
            } catch (SQLException sqle) {
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
        LogStatement logStatement = null;
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {
            statement.setLong(1, dto.getIdApprover());
            statement.setLong(2, dto.getIdEmployee());
            statement.setString(3, dto.getCommentLs());
            statement.setInt(4, dto.getDaysSum());
            statement.setInt(5, dto.getTypeLeave());
            statement.setInt(6, dto.getApprove());
            statement.setDate(7, (Date) dto.getDateLeave());
            statement.setDate(8, (Date) dto.getDateOfLs());
            statement.execute();
            this.connection.commit();
            logStatement = this.findById(dto.getId());
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return logStatement;
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
