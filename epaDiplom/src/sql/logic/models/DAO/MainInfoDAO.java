package sql.logic.models.DAO;

import sql.logic.models.entities.Employee;
import sql.logic.models.entities.MainInfo;
import sql.logic.models.util.DataAccessObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MainInfoDAO extends DataAccessObject<MainInfo> {

    public MainInfoDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_main_info, first_name, middle_name, last_name, " +
            "cabinet_office, birth_d, entry_d FROM main_info WHERE id_main_info = ?";
    private static final String DELETE = "DELETE FROM main_info WHERE id_main_info = ?";
    private static final String UPDATE = "UPDATE main_info SET first_name = ?, middle_name = ?, last_name = ?," +
            "cabinet_office = ?, birth_d = ?, entry_d = ?  WHERE id_main_info = ?";
    private static final String INSERT = "INSERT INTO main_info (first_name, middle_name, last_name, " +
            "cabinet_office, birth_d, entry_d, id_main_info ) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String GET_ONE_BY_ONE = "SELECT * FROM main_info";


    @Override
    public MainInfo findById(long id) {
        MainInfo mainInfo = new MainInfo();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                mainInfo.setId(rs.getLong("id_main_info"));
                mainInfo.setFirstName(rs.getString("first_name"));
                mainInfo.setMiddleName(rs.getString("middle_name"));
                mainInfo.setLastName(rs.getString("last_name"));
                mainInfo.setCabinetOffice(rs.getString("cabinet_office"));
                mainInfo.setBirthD(rs.getDate("birth_d"));
                mainInfo.setEntryD(rs.getDate("entry_d"));

            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mainInfo;
    }

    public List<MainInfo> findAllInList(){
        List<MainInfo> mainInfos = new ArrayList<>();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE_BY_ONE);){
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                MainInfo mainInfo = new MainInfo();
                mainInfo.setId(rs.getLong("id_main_info"));
                mainInfo.setFirstName(rs.getString("first_name"));
                mainInfo.setMiddleName(rs.getString("middle_name"));
                mainInfo.setLastName(rs.getString("last_name"));
                mainInfo.setCabinetOffice(rs.getString("cabinet_office"));
                mainInfo.setBirthD(rs.getDate("birth_d"));
                mainInfo.setEntryD(rs.getDate("entry_d"));
                mainInfos.add(mainInfo);
            }
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mainInfos;
    }

    @Override
    public List<MainInfo> findAll() {
        return null;
    }

    @Override
    public MainInfo update(MainInfo dto) {
        MainInfo mainInfo = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getMiddleName());
            statement.setString(3, dto.getLastName());
            statement.setString(4, dto.getCabinetOffice());
            statement.setDate(5, dto.getBirthD());
            statement.setDate(6, dto.getEntryD());
            statement.setLong(7, dto.getId());
            statement.execute();
            this.connection.commit();
            mainInfo = this.findById(dto.getId());
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
        return mainInfo;
    }

    @Override
    public MainInfo create(MainInfo dto) {
        MainInfo mainInfo = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getMiddleName());
            statement.setString(3, dto.getLastName());
            statement.setString(4, dto.getCabinetOffice());
            statement.setDate(5, (Date) dto.getBirthD());
            statement.setDate(6, (Date) dto.getEntryD());
            statement.setLong(7, dto.getId());
            statement.execute();
            this.connection.commit();
            mainInfo = this.findById(dto.getId());
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return mainInfo;
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
