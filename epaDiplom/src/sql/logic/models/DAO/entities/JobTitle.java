package sql.logic.models.DAO.entities;

import sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

import java.util.List;

public class JobTitle implements DataTransferObject {
    private long id;
    private String job_title_name;
    private List<JobTitle> jobTitles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobTitleName() {
        return job_title_name;
    }
    public void setJobTitleName(String job_title_name) {
        this.job_title_name = job_title_name;
    }
    public List<JobTitle> getJobTitles (){ return jobTitles; }
    public void setJobTitles(List<JobTitle> jobTitles){ this.jobTitles = jobTitles;}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("_");
        sb.append("Job title: '").append(job_title_name).append('\'');
        sb.append(" - ").append(id).append(" ");

        return sb.toString();
    }
}
