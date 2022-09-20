package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

public class JobEmployee implements DataTransferObject {

    private long id_employee;
    private long id_job_title;

    public long getId() {
        return id_employee;
    }

    public void setId(long id_employee) {
        this.id_employee = id_employee;
    }

    public long getIdJobTitle() {
        return id_job_title;
    }

    public void setIdJobTitle(long id_job_title) {
        this.id_job_title = id_job_title;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("job_employee{");
        sb.append("id_employee=").append(id_employee);
        sb.append(", id_job_title='").append(id_job_title).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
