package vn.com.fptshop.fmusic.sync;

import java.util.List;

/**
 * Created by MinhDH on 12/24/15.
 */
public class DataContacts {
    List<ContactManager.PhoneContact> contacts;

    public DataContacts(List<ContactManager.PhoneContact> contacts) {
        this.contacts = contacts;
    }

    public List<ContactManager.PhoneContact> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactManager.PhoneContact> contacts) {
        this.contacts = contacts;
    }
}
