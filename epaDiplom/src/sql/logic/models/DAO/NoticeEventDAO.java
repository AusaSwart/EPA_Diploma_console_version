package sql.logic.models.DAO;

import sql.logic.models.entities.NoticeEvent;
import sql.logic.models.util.DataAccessObject;

import java.sql.*;
import java.util.List;

public class NoticeEventDAO extends DataAccessObject<NoticeEvent> {

    public NoticeEventDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_recipient, id_event, id_employee " +
            " FROM notice_event WHERE id_recipient=?";
    private static final String DELETE = "DELETE FROM notice_event WHERE id_recipient = ?";
    private static final String UPDATE = "UPDATE notice_event SET id_event = ?, id_employee = ?" +
            " WHERE id_recipient = ?";
    private static final String INSERT = "INSERT INTO notice_event (id_event, id_employee) VALUES (?, ?)";

    @Override
    public NoticeEvent findById(long id) {
        NoticeEvent noticeEvent = new NoticeEvent();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                noticeEvent.setId(rs.getLong("id_recipient"));
                noticeEvent.setIdEvent(rs.getLong("id_event"));
                noticeEvent.setIdEmployee(rs.getLong("id_employee"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return noticeEvent;
    }

    @Override
    public List<NoticeEvent> findAll() {
        return null;
    }

    @Override
    public NoticeEvent update(NoticeEvent dto) {
        NoticeEvent noticeEvent = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setLong(1, dto.getIdEvent());
            statement.setLong(2, dto.getIdEmployee());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();
            noticeEvent = this.findById(dto.getId());
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
        return noticeEvent;
    }


    @Override
    public NoticeEvent create(NoticeEvent dto) {
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setLong(1, dto.getIdEvent());
            statement.setLong(2, dto.getIdEmployee());

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
