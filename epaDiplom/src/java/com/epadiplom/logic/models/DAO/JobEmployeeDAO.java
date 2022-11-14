package java.com.epadiplom.logic.models.DAO;

import java.com.epadiplom.logic.models.DAO.entities.JobEmployee;
import java.com.epadiplom.logic.models.DAO.entities.JobTitle;
import java.com.epadiplom.logic.models.DAO.utilDAO.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobEmployeeDAO extends DataAccessObject<JobEmployee> {

    public JobEmployeeDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_employee, id_job_title FROM job_employee WHERE id_employee=?";
    private static final String DELETE = "DELETE FROM job_employee WHERE id = ?";
    private static final String UPDATE = "UPDATE job_employee SET id_job_title = ? WHERE id_employee = ? AND id = ?";
    private static final String INSERT = "INSERT INTO job_employee (id_job_title, id_employee) VALUES (?, ?)";
    private static final String GET_COMPLICATED = "SELECT jt.job_title_name FROM public.job_employee je JOIN" +
            " public.job_title jt ON jt.id = je.id_job_title WHERE je.id_employee = ?";
    private static final String GET_ONE_EM = "SELECT id_employee, id_job_title, id FROM job_employee WHERE id_employee = ?";
    public List<JobEmployee> findByIdEmp (long id_employee) {
        List<JobEmployee> jobEmployees = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE_EM);){
            statement.setLong(1, id_employee);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                JobEmployee jobEmployee = new JobEmployee();
                jobEmployee.setIdEmployee(rs.getLong("id_employee"));
                jobEmployee.setIdJobTitle(rs.getLong("id_job_title"));
                jobEmployee.setId(rs.getLong("id"));
                jobEmployees.add(jobEmployee);
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobEmployees;
    }

    public JobEmployee findComplicatedReqFJ(long id_employee) {
        JobEmployee jobEmployee = new JobEmployee();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_COMPLICATED);){
            statement.setLong(1, id_employee);
            ResultSet rs = statement.executeQuery();
            List<JobTitle> jobTitles = new ArrayList<>();
            while(rs.next()){
                JobTitle jobTitle = new JobTitle();
                jobTitle.setJobTitleName(rs.getString(1));
                jobTitles.add(jobTitle);
            }
            jobEmployee.setJobTitles(jobTitles);
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobEmployee;
    }

    @Override
    public JobEmployee findById(long id_employee) {
        JobEmployee jobEmployee = new JobEmployee();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id_employee);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                jobEmployee.setId(rs.getLong("id_employee"));
                jobEmployee.setIdJobTitle(rs.getLong("id_job_title"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobEmployee;
    }

    @Override
    public List<JobEmployee> findAll() {
        return null;
    }

    @Override
    public JobEmployee update(JobEmployee dto) {
        JobEmployee jobEmployee = null;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setLong(1, dto.getIdJobTitle());
            statement.setLong(2, dto.getIdEmployee());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobEmployee;
    }


    @Override
    public JobEmployee create(JobEmployee dto) {
        JobEmployee jobEmployee = new JobEmployee();
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setLong(1, dto.getIdJobTitle());
            statement.setLong(2, dto.getIdEmployee());
            statement.execute();
            this.connection.commit();
            jobEmployee = this.findById(dto.getId());
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return jobEmployee;
    }

    public void deleteByEntitie(JobEmployee dto) {
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(DELETE);){
            statement.setLong(1, dto.getId());
            statement.executeUpdate();
            this.connection.commit();
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
