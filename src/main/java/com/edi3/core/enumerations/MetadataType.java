package com.edi3.core.enumerations;

import com.edi3.core.categories.CategoryProperty;
import com.edi3.core.documents.DocumentProperty;

import javax.persistence.*;

@Table(name = "ENUM_METADATA_TYPE")
public enum MetadataType {

    // ----- Categories -----
    CONTRACTOR(0, Metadata.CATEGORY, CategoryProperty.CONTRACTOR.name()),
    COST_ITEM(1, Metadata.CATEGORY, CategoryProperty.COST_ITEM.name()),
    CURRENCY(2, Metadata.CATEGORY, CategoryProperty.CURRENCY.name()),
    DEPARTMENT(3, Metadata.CATEGORY, CategoryProperty.DEPARTMENT.name()),
    LEGAL_ORGANIZATION(4, Metadata.CATEGORY, CategoryProperty.LEGAL_ORGANIZATION.name()),
    PLANNING_PERIOD(5, Metadata.CATEGORY, CategoryProperty.PLANNING_PERIOD.name()),
    POSITION(6, Metadata.CATEGORY, CategoryProperty.POSITION.name()),
    PROPOSAL_TEMPLATE(7, Metadata.CATEGORY, CategoryProperty.PROPOSAL_TEMPLATE.name()),
    UPLOADED_FILE(8, Metadata.CATEGORY, CategoryProperty.UPLOADED_FILE.name()),
    USER(9, Metadata.CATEGORY, CategoryProperty.USER.name()),
    // ----- Documents -----
    MEMORANDUM(10, Metadata.DOCUMENT, DocumentProperty.MEMORANDUM.name()),
    MESSAGE(11, Metadata.DOCUMENT, DocumentProperty.MESSAGE.name()

    );

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;

    @Column(name = "METADATA")
    private Metadata metadata;

    @Column(name = "EN_NAME")
    private String enName;

    MetadataType() {
    }

    MetadataType(long id, Metadata metadata, String enName) {
        this.id = id;
        this.metadata = metadata;
        this.enName = enName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }
}
