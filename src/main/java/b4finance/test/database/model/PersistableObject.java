package b4finance.test.database.model;

import javax.persistence.*;
import java.util.Date;

/**
 * A domain object which handles columns common to all entities.
 */
@MappedSuperclass
@Cacheable(false)
public abstract class PersistableObject {
    
    private static final String SEQUENCE_NAME = "SEQUENCE";
    private static final String SEQUENCE_GENERATOR_NAME = "Sequence_Generator";

    @Id
    @GeneratedValue(generator = SEQUENCE_GENERATOR_NAME)
    @SequenceGenerator(name = SEQUENCE_GENERATOR_NAME, sequenceName = SEQUENCE_NAME, allocationSize=1)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", nullable = false)
    private Date creationDate;

    @Version // Used for the optimistic concurrency management
    @Column(name = "VERSION", nullable = false)
    private int version;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATE_DATE", nullable = false)
    private Date updateDate;

    public PersistableObject() {
    }

    @PrePersist
    void onCreate() {
        if (null == creationDate) {
            creationDate = new Date();
        }
        if (updateDate == null) {
        	updateDate = creationDate;
        }
    }

    @PreUpdate
    void onPersist() {
        updateDate = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected int getVersion() {
        return version;
    }

    protected void setVersion(int version) {
        this.version = version;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setUpdateDate(final Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

}
