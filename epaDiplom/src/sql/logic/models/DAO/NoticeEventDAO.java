package sql.logic.models.DAO;

import sql.logic.models.entities.EmployeeTask;
import sql.logic.models.entities.Event;
import sql.logic.models.entities.NoticeEvent;
import sql.logic.models.entities.Task;
import sql.logic.models.util.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoticeEventDAO extends DataAccessObject<NoticeEvent> {

    public NoticeEventDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_recipient, id_event, id_employee " +
            " FROM notice_event WHERE id_recipient = ?";
    private static final String DELETE = "DELETE FROM notice_event WHERE id_recipient = ?";
    private static final String UPDATE = "UPDATE notice_event SET id_event = ?, id_employee = ?" +
            " WHERE id_recipient = ?";
    private static final String INSERT = "INSERT INTO notice_event (id_recipient, id_event, id_employee) " +
            "VALUES (?, ?, ?)";
    private static final String GET_COMPLICATED_FE = "SELECT ne.id_recipient, ne.id_employee, ne.id_event," +
            "e.type_of_event, e.date_of_event, e.comment_fe FROM public.notice_event ne " +
            "JOIN public.event e ON ne.id_event = e.id WHERE id_recipient = ? ";
    public NoticeEvent findComplicatedReqFE(long id) {
        NoticeEvent noticeEvent = new NoticeEvent();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_COMPLICATED_FE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<NoticeEvent> noticeEvents = new ArrayList<>();
            List<Event> events = new ArrayList<>();
            while(rs.next()){
                NoticeEvent noticeEvent1 = new NoticeEvent();
                Event event = new Event();
                noticeEvent1.setId(rs.getLong(1));
                noticeEvent1.setIdEmployee(rs.getLong(2));
                noticeEvent1.setIdEvent(rs.getLong(3));
                event.setId(rs.getLong("id_event"));
                event.setTypeOfEvent(rs.getString(4));
                event.setDateOfEvent(rs.getDate(5));
                event.setCommentFE(rs.getString(6));
                events.add(event);
                noticeEvents.add(noticeEvent1);
            }
            noticeEvent.setNoticeEvents(noticeEvents);
            noticeEvent.setEvents(events);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return noticeEvent;
    }

    public List<NoticeEvent> findByIdList(long id){
        List<NoticeEvent> noticeEvents = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                NoticeEvent noticeEvent = new NoticeEvent();
                noticeEvent.setId(rs.getLong("id_recipient"));
                noticeEvent.setIdEvent(rs.getLong("id_event"));
                noticeEvent.setIdEmployee(rs.getLong("id_employee"));
                noticeEvents.add(noticeEvent);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return noticeEvents;
    }

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
        NoticeEvent noticeEvent = new NoticeEvent();
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setLong(1, dto.getId());
            statement.setLong(2, dto.getIdEvent());
            statement.setLong(3, dto.getIdEmployee());
            statement.execute();
            this.connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return noticeEvent;
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
