package java.com.epadiplom.logic.models.DAO.entities;

import java.com.epadiplom.logic.models.DAO.entities.utilDTO.DataTransferObject;

public class Document implements DataTransferObject {
    private long id;
    private long id_ls;
    private String body_doc;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_LS() {
        return id_ls;
    }

    public void setId_LS(long id_ls) {
        this.id_ls = id_ls;
    }

    public String getBodyDoc() {return body_doc;}

    public void setBodyDoc(String body_doc) {
        this.body_doc = body_doc;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("___document ");
        sb.append(" № ").append(id);
        sb.append("___\n For № ").append(id_ls);
        sb.append(" of statement\n Path to doc '").append(body_doc).append('\'');
        sb.append("\n");
        return sb.toString();
    }
}
