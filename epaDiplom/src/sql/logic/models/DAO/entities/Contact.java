package sql.logic.models.DAO.entities;

import sql.logic.models.DAO.entities.utilDTO.DataTransferObject;

public class Contact implements DataTransferObject {

    private long id_main_info_contact;
    private String location_street;
    private long work_number;
    private long personal_number;
    private String mail;

    public long getId() {
        return id_main_info_contact;
    }

    public void setId(long id_main_info_contact) {
        this.id_main_info_contact = id_main_info_contact;
    }

    public String getLocationStreet() {return location_street;}

    public void setLocationStreet(String location_street) {
        this.location_street = location_street;
    }

    public long getWorkNumber() {return work_number;}

    public void setWorkNumber(long work_number) {
        this.work_number = work_number;
    }

    public long getPersonalNumber() {return personal_number;}

    public void setPersonalNumber(long personal_number) {
        this.personal_number = personal_number;
    }

    public String getMail() {return mail;}

    public void setMail(String mail) {
        this.mail = mail;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("____Contact");
        sb.append("\n Location street is '").append(location_street).append('\'');
        sb.append("\n Work number: ").append(work_number);
        sb.append("\n Personal number: ").append(personal_number);
        sb.append("\n Mail: ").append(mail);
        sb.append("\n");
        return sb.toString();
    }
}
