package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

import java.util.List;

public class NoticeEvent implements DataTransferObject {
    private long id_recipient;
    private long id_event;
    private long id_employee;
    private List<Task> tasks;
    private List<EmployeeTask> employeeTasks;

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
    public List<Task> getTasks (){ return tasks; }
    public void setTasks(List<Task> tasks){ this.tasks = tasks;}
    public List<EmployeeTask> getEmployeeTasks (){ return employeeTasks; }
    public void setEmployeeTasks(List<EmployeeTask> employeeTasks){ this.employeeTasks = employeeTasks;}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("___Notice of event !!!___ \n");
        sb.append(" id of recipient: №").append(id_recipient);
        sb.append(", id of event: №").append(id_event);
        sb.append(", id of employee: №").append(id_employee);
        return sb.toString();
    }

}
