package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

import java.util.Date;

public class Event implements DataTransferObject {

    private long id;
    private String type_of_event;
    private String comment_fe;
    private Date date_of_event;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTypeOfEvent() {return type_of_event;}

    public void setTypeOfEvent(String type_of_event) {
        this.type_of_event = type_of_event;
    }

    public String getCommentFE() {return comment_fe;}

    public void setCommentFE(String comment_fe) { this.comment_fe = comment_fe; }

    public Date getDateOfEvent() {
        return date_of_event;
    }

    public void setDateOfEvent(Date date_of_event) {
        this.date_of_event = date_of_event;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("|_Event__");
        sb.append(" № ").append(id);
        sb.append("_|\n Type of event: '").append(type_of_event).append('\'');
        sb.append("\n Comment: '").append(comment_fe).append('\'');
        sb.append("\n Date of event: ").append(date_of_event);
        sb.append("\n");
        return sb.toString();
    }
}
