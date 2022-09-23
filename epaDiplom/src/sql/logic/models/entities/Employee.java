package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

public class Employee implements DataTransferObject {
    private long id;
    private int privilege;
    private int status;
    private long id_dep;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getIdDep() {
        return id_dep;
    }

    public void setIdDep(long id_dep) {
        this.id_dep = id_dep;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" Employee №");
        sb.append(" ").append(id);
        sb.append("\n privilege ").append(privilege);
        sb.append("\n status ").append(status);
        sb.append("\n id of department ").append(id_dep);
        sb.append("\n ");
        return sb.toString();
    }
}
