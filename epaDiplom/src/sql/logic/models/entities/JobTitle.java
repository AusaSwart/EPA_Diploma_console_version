package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

public class JobTitle implements DataTransferObject {
    private long id_job_title;
    private String job_title_name;

    public long getId() {
        return id_job_title;
    }

    public void setId(long id) {
        this.id_job_title = id_job_title;
    }

    public String getJobTitleName() {
        return job_title_name;
    }
    public void setJobTitleName(String job_title_name) {
        this.job_title_name = job_title_name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("_");
        sb.append("Job title: '").append(job_title_name).append('\'');
        return sb.toString();
    }
}
