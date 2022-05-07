package io.dpopkov.knowthenix.domain.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class ModifiableEntity extends BaseEntity {

    @Column(name = "CREATED_ON", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    protected LocalDateTime createdOn;

    @Column(name = "MODIFIED_ON")
    @org.hibernate.annotations.UpdateTimestamp
    protected LocalDateTime modifiedOn;

    @Override
    public String toString() {
        return super.toString() + "{" +
                "createdOn=" + createdOn +
                ", modifiedOn=" + modifiedOn +
                "} ";
    }
}
