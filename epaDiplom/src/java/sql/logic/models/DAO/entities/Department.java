package java.sql.logic.models.DAO.entities;

import java.sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

import java.util.List;

public class Department implements DataTransferObject{
    private long id;
    private String name_dep;
    List<Department> departments;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNameDep() {
        return name_dep;
    }

    public void setNameDep(String firstName) {
        this.name_dep = firstName;
    }
    public List<Department> getDepartments() { return departments; }
    public void setDepartments(List<Department> departments) { this.departments = departments; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(" id");
        sb.append(" ").append(id);
        sb.append(";  name of department: \'").append(name_dep).append('\'');
        sb.append("\n");
        return sb.toString();
    }

}
