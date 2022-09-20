package sql.logic.models.entities;

import sql.logic.models.util.DataTransferObject;

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
        final StringBuilder sb = new StringBuilder("document{");
        sb.append("id=").append(id);
        sb.append(", id_ls='").append(id_ls).append('\'');
        sb.append(", body_doc='").append(body_doc).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
