package com.edi3.core.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.categories.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Objects;

/*
* Document Message (Сообщение)
*
* One user sends message to another user(s)
* (simply than Memorandum)
*
*/

@Entity
@Table(name = "DOC_MESSAGE")
public class Message extends AbstractDocumentEdi {

    @Column(name = "message_field")
    private String messageField;

    public Message() {
        super();
    }

    public Message(Timestamp date, boolean deletionMark, String number, boolean posted, User author, String theme, String text, String whomString) {
        super(date, deletionMark, number, posted, author, theme, text, whomString);
    }

    public DocumentProperty getDocumentProperty() {
        return DocumentProperty.MESSAGE;
    }

    @SuppressWarnings("unused")
    public String getMessageField() {
        return messageField;
    }

    @SuppressWarnings("unused")
    public void setMessageField(String messageField) {
        this.messageField = messageField;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Message message = (Message) o;
        return Objects.equals(messageField, message.messageField);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), messageField);
    }
}
