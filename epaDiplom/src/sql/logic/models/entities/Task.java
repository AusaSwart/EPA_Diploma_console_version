package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;
import java.sql.Date;
import java.util.List;

public class Task implements DataTransferObject {
    private long id;
    private Date date_task;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Date getDateTask() {
        return date_task;
    }
    public void setDateTask(Date date_task) {
        this.date_task = date_task;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        sb.append("№ ").append(id);
        sb.append("\n   Date of task: '").append(date_task).append('\'');
        sb.append("\n");
        return sb.toString();
    }
}
