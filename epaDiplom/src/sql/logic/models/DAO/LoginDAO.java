package sql.logic.models.DAO;

import sql.logic.models.entities.Login;
import sql.logic.models.entities.MainInfo;
import sql.logic.models.util.DataAccessObject;

import java.sql.*;
import java.util.List;

public class LoginDAO extends DataAccessObject<Login> {

    public LoginDAO(Connection connection) {
        super(connection);
    }
    private static final String GET_ONE = "SELECT id_main_info_login, login_user, password_user " +
            "FROM login WHERE id_main_info_login = ?";
    private static final String DELETE = "DELETE FROM login WHERE id_main_info_login = ?";
    private static final String UPDATE = "UPDATE login SET login_user = ?, " +
            "password_user = ?  WHERE id_main_info_login = ?";
    private static final String INSERT = "INSERT INTO login (login_user, password_user, " +
            " id_main_info_login) VALUES (?, ?, ?)";
    private static final String GET_LOGIN_N_PASSWORD = "SELECT login_user, password_user, id_main_info_login " +
            "FROM login WHERE login_user = ? AND password_user = ? ";
    private static final String GET_LOGIN = "SELECT login_user FROM login WHERE login_user = ?";

    @Override
    public Login findById(long id) {
        Login login = new Login();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_ONE);){
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                login.setId(rs.getLong("id_main_info_login"));
                login.setLoginUser(rs.getString("login_user"));
                login.setPasswordUser(rs.getString("password_user"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return login;
    }

    public Login checkPassLog(String loginUser, String passwordUser) {
        Login login = new Login();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_LOGIN_N_PASSWORD);){
            statement.setString(1, loginUser);
            statement.setString(2, passwordUser);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                login.setId(rs.getLong("id_main_info_login"));
                login.setLoginUser(rs.getString("login_user"));
                login.setPasswordUser(rs.getString("password_user"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return login;
    }

    public Login checkLogin(String loginUser) {
        Login login = new Login();
        try(PreparedStatement statement = this.connection.prepareStatement(GET_LOGIN);){
            statement.setString(1, loginUser);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                login.setLoginUser(rs.getString("login_user"));
            }
        }catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return login;
    }

    @Override
    public List<Login> findAll() {
        return null;
    }

    @Override
    public Login update(Login dto) {
        Login login = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(UPDATE);){
            statement.setString(1, dto.getLoginUser());
            statement.setString(2, dto.getPasswordUser());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();            //
            login = this.findById(dto.getId());  // разобраться в этом куске
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
        return login;
    }

    @Override
    public Login create(Login dto) {
        Login login = null;
        try{
            this.connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        try(PreparedStatement statement = this.connection.prepareStatement(INSERT);){
            statement.setString(1, dto.getLoginUser());
            statement.setString(2, dto.getPasswordUser());
            statement.setLong(3, dto.getId());
            statement.execute();
            this.connection.commit();
            login = this.findById(dto.getId());
        }catch(SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return login;
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
