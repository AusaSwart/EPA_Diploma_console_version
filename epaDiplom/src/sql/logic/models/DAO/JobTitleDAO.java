package sql.logic.models.DAO;

import sql.logic.models.entities.JobTitle;
import sql.logic.models.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JobTitleDAO extends DataAccessObject<JobTitle> {

    public JobTitleDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_job_title, job_title_name FROM job_title WHERE id_job_title=?";
    private static final String DELETE = "DELETE FROM job_title WHERE id_job_title = ?";
    private static final String UPDATE = "UPDATE job_title SET job_title_name = ?  WHERE id_job_title = ?";
    private static final String INSERT = "INSERT INTO job_title (job_title_name) VALUES (?)";

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
    public List<JobTitle> findAll() {
        return null;
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
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getJobTitleName());
            statement.execute();
            int id = this.getLastVal(EMPLOYEE_SEQUENCE);
            return this.findById(id);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, id);
            statement.execute();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
