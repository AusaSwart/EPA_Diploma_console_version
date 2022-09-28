package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

import java.util.List;

public class JobEmployee implements DataTransferObject {

    private long id_employee;
    private long id_job_title;
    private List<JobEmployee> jobEmployees;
    private List<JobTitle> jobTitles;

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
    public List<JobEmployee> getJobEmployees (){ return jobEmployees; }
    public void setJobEmployees(List<JobEmployee> jobEmployees){ this.jobEmployees = jobEmployees;}
    public List<JobTitle> getJobTitles (){ return jobTitles; }
    public void setJobTitles(List<JobTitle> jobTitles){ this.jobTitles = jobTitles;}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" ");
        sb.append("Employee №").append(id_employee);
        sb.append(" id of job title № ").append(id_job_title);
        sb.append("\n");
        return sb.toString();
    }

}
