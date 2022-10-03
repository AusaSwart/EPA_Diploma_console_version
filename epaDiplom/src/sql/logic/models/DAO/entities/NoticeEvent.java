package sql.logic.models.DAO.entities;

import sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

import java.util.List;

public class NoticeEvent implements DataTransferObject {
    private long id_recipient;
    private long id_event;
    private long id_employee;
    private List<Event> events;
    private List<NoticeEvent> noticeEvents;

    public long getId() {
        return id_recipient;
    }

    public void setId(long id_recipient) {
        this.id_recipient = id_recipient;
    }
    public long getIdEvent() {
        return id_event;
    }
    public void setIdEvent(long id_event) { this.id_event = id_event; }
    public long getIdEmployee() {
        return id_employee;
    }
    public void setIdEmployee(long id_employee) {
        this.id_employee = id_employee;
    }
    public List<Event> getEvents (){ return events; }
    public void setEvents(List<Event> events){ this.events = events;}
    public List<NoticeEvent> getNoticeEvents (){ return noticeEvents; }
    public void setNoticeEvents(List<NoticeEvent> noticeEvents){ this.noticeEvents = noticeEvents;}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("___Notice of event !!!___ \n");
        sb.append(" Recipient: №").append(id_recipient);
        sb.append(", event: №").append(id_event);
        sb.append(", from employee: №").append(id_employee);
        return sb.toString();
    }

}
