package java.com.epadiplom.logic.models.DAO;

import java.com.epadiplom.logic.models.DAO.entities.JobTitle;
import java.com.epadiplom.logic.models.DAO.utilDAO.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobTitleDAO extends DataAccessObject<JobTitle> {

    public JobTitleDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id, job_title_name FROM job_title WHERE id=?";
    private static final String DELETE = "DELETE FROM job_title WHERE id_job_title = ?";
    private static final String UPDATE = "UPDATE job_title SET job_title_name = ?  WHERE id = ?";
    private static final String INSERT = "INSERT INTO job_title (job_title_name) VALUES (?)";
    private static final String GET_NAMES = "SELECT job_title_name, id FROM public.job_title " +
            "ORDER BY id";

    public List<JobTitle> findAll() {
        List<JobTitle> jobTitles = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_NAMES);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                JobTitle jobTitle = new JobTitle();
                jobTitle.setJobTitleName(rs.getString(1));
                jobTitle.setId(rs.getLong(2));
                jobTitles.add(jobTitle);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobTitles;
    }

    @Override
    public JobTitle findById(long id) {
        JobTitle jobTitle = new JobTitle();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                jobTitle.setId(rs.getLong("id_job_title"));
                jobTitle.setJobTitleName(rs.getString("job_title_name"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobTitle;
    }

    @Override
    public JobTitle update(JobTitle dto) {
        JobTitle jobTitle = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1, dto.getJobTitleName());
            statement.setLong(2, dto.getId());
            statement.execute();
            this.connection.commit();
            jobTitle = this.findById(dto.getId());
        }catch(SQLException e){
            try{
                this.connection.rollback();
            }catch (SQLException sqle){
                e.printStackTrace();
                throw new RuntimeException(sqle);
            }
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobTitle;
    }


    @Override
    public JobTitle create(JobTitle dto) {
        JobTitle jobTitle = new JobTitle();
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getJobTitleName());
            statement.execute();
            this.connection.commit();
            jobTitle = this.findById(dto.getId());
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobTitle;
    }

    @Override
    public void delete(long id) {
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, id);
            statement.executeUpdate();
            this.connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
