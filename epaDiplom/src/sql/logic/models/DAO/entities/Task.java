package sql.logic.models.DAO.entities;

import sql.logic.models.DAO.entities.utilDTO.DataTransferObject;
import java.sql.Date;

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
        sb.append("   Date of task: '").append(date_task).append('\'');
        sb.append("\n");
        return sb.toString();
    }
}
