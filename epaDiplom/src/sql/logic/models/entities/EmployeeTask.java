package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;
public class EmployeeTask implements DataTransferObject {
    private long id_executor;
    private long id_employee;
    private long id_task;
    private String comment_te;

    public long getId() {
        return id_executor;
    }
    public void setId(long id_executor) {
        this.id_executor = id_executor;
    }
    public long getIdEmployee() {
        return id_employee;
    }
    public void setIdEmployee(long id_employee) {
        this.id_employee = id_employee;
    }
    public long getIdTask() {
        return id_task;
    }
    public void setIdTask(long id_task) {
        this.id_task = id_task;
    }
    public String getCommentTE() {
        return comment_te;
    }
    public void setCommentTE(String id_task) {
        this.comment_te = comment_te;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("emp_task{");
        sb.append("id_executor=").append(id_executor);
        sb.append(", id_employee='").append(id_employee).append('\'');
        sb.append(", id_task='").append(id_task).append('\'');
        sb.append(", comment_te='").append(comment_te).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
