package com.edi3.core.documents;

import com.edi3.core.abstract_entity.AbstractDocumentEdi;
import com.edi3.core.categories.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Objects;

/*
* Document Memorandum (Служебная записка)
*
* Document contains main topic
* different business processes (and different tasks)
* are created around this topic
*
*
*/

@Entity
@Table(name = "DOC_MEMORANDUM")
public class Memorandum extends AbstractDocumentEdi {

    @Column(name = "memorandum_field")
    private String memorandumField;

    @NotNull(message = "Поле 'Кому' должно быть заполнено")
    @ManyToOne
    @JoinColumn(name = "whom_id")
    private User whom;

    public Memorandum() {
        super();
    }

    public Memorandum(Timestamp date, boolean deletionMark, String number, boolean posted, User author, String theme, String text, String whomString, User whom) {
        super(date, deletionMark, number, posted, author, theme, text, whomString);
        this.whom = whom;
    }

    public DocumentProperty getDocumentProperty() {
        return DocumentProperty.MEMORANDUM;
    }

    @SuppressWarnings("unused")
    public String getMemorandumField() {
        return memorandumField;
    }

    @SuppressWarnings("unused")
    public void setMemorandumField(String memorandumField) {
        this.memorandumField = memorandumField;
    }

    public User getWhom() {
        return whom;
    }

    @SuppressWarnings("unused")
    public void setWhom(User whom) {
        this.whom = whom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Memorandum that = (Memorandum) o;
        return Objects.equals(memorandumField, that.memorandumField) && Objects.equals(whom, that.whom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), memorandumField, whom);
    }
}
