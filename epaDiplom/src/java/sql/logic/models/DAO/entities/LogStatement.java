package java.sql.logic.models.DAO.entities;

import java.sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

import java.util.Date;
import java.util.List;

public class LogStatement implements DataTransferObject {
    private long id;
    private long id_approver;
    private long id_employee;
    private String comment_ls;
    private int days_sum;
    private int type_leave;
    private int approve;
    private Date date_leave;
    private Date date_of_ls;
    private List<Document> documents;
    private List<LogStatement> logStatements;
    private List<Long> approves;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdApprover() {
        return id_approver;
    }

    public void setIdApprover(long id_approver) {
        this.id_approver = id_approver;
    }

    public long getIdEmployee() {
        return id_employee;
    }

    public void setIdEmployee(long id_employee) {
        this.id_employee = id_employee;
    }

    public String getCommentLs() {return comment_ls;}

    public void setCommentLs(String comment_ls) {
        this.comment_ls = comment_ls;
    }

    public int getDaysSum() {return days_sum;}

    public void setDaysSum(int days_sum) {
        this.days_sum = days_sum;
    }

    public int getTypeLeave() {return type_leave;}

    public void setTypeLeave(int type_leave) {
        this.type_leave = type_leave;
    }

    public int getApprove() {return approve;}

    public void setApprove(int approve) {
        this.approve = approve;
    }

    public Date getDateLeave() {
        return date_leave;
    }

    public void setDateLeave(Date date_leave) {
        this.date_leave = date_leave;
    }

    public Date getDateOfLs() {
        return date_of_ls;
    }

    public void setDateOfLs(Date date_of_ls) {
        this.date_of_ls = date_of_ls;
    }
    public List<Document> getDocuments (){ return documents; }
    public void setDocuments(List<Document> documents){ this.documents = documents;}
    public List<LogStatement> getLogStatements (){ return logStatements; }
    public void setLogStatements(List<LogStatement> logStatements){ this.logStatements = logStatements;}
    public List<Long> getApproves (){ return approves; }
    public void setApproves(List<Long> approves){ this.approves = approves;}
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("|___Statement___ ");
        sb.append("№ ").append(id);
        sb.append("\n To approver №").append(id_approver);
        sb.append(" from employee № ").append(id_employee);
        sb.append("\n Comment '").append(comment_ls).append('\'');
        sb.append("\n Sum of days: ").append(days_sum);
        sb.append(" type of leave: ");
            if (type_leave == 1){ System.out.println("Sick leave"); }
            else if (type_leave == 2){ System.out.println("Vacation"); }
            else if (type_leave == 3){ System.out.println("At own expense"); }
            else if (type_leave == 4){ System.out.println("Dismissal"); }
            else if (type_leave == 5){ System.out.println("Else"); }
        sb.append("\n Approve status ").append(approve);
        sb.append("\n Date of leave: '").append(date_leave).append('\'');
        sb.append("\n Date of ls: '").append(date_of_ls).append('\'');
        return sb.toString();
    }
}
