package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

public class NoticeEvent implements DataTransferObject {
    private long id_recipient;
    private long id_event;
    private long id_employee;

    public long getId() {
        return id_recipient;
    }

    public void setId(long id_recipient) {
        this.id_recipient = id_recipient;
    }
    public long getIdEvent() {
        return id_event;
    }
    public void setIdEvent(long id_event) {
        this.id_event = id_event;
    }
    public long getIdEmployee() {
        return id_employee;
    }
    public void setIdEmployee(long id_employee) {
        this.id_employee = id_employee;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("notice_event{");
        sb.append("id_recipient=").append(id_recipient);
        sb.append(", id_event='").append(id_event).append('\'');
        sb.append(", id_employee='").append(id_employee).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
