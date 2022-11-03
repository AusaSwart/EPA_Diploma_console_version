package java.sql.logic.models.DAO.entities;

import java.sql.logic.models.DAO.entities.utilDTO.DataTransferObject;
import java.sql.Date;

public class Task implements DataTransferObject {
    private long id;
    private Date date_task;
    private String name_of_task;
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
    public String getNameOfTask() {
        return name_of_task;
    }
    public void setNameOfTask(String name_of_task) {
        this.name_of_task = name_of_task;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        sb.append("№ ").append(id);
        sb.append("   Date of task: '").append(date_task).append('\'');
        sb.append("\n   Name of task: '").append(name_of_task).append('\'');
        sb.append("\n");
        return sb.toString();
    }
}
