package sql.logic.models.DAO.entities;

import sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

public class Login implements DataTransferObject {
    private long id_main_info_login;
    private String login_user;
    private String password_user;

    public long getId() {
        return id_main_info_login;
    }

    public void setId(long id_main_info_login) {
        this.id_main_info_login = id_main_info_login;
    }
    public String getLoginUser() {return login_user;}
    public void setLoginUser(String login_user) {
        this.login_user = login_user;
    }
    public String getPasswordUser() {return password_user;}
    public void setPasswordUser(String password_user) {
        this.password_user = password_user;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("login{");
        sb.append("id_main_info_login=").append(id_main_info_login);
        sb.append(", login_user='").append(login_user).append('\'');
        sb.append(", password_user='").append(password_user).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
