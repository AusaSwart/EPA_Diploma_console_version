package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

public class Department implements DataTransferObject{
    private long id;
    private String name_dep;

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("department{");
        sb.append("id=").append(id);
        sb.append(", name_dep='").append(name_dep).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
