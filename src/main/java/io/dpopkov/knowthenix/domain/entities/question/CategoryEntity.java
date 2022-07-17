package io.dpopkov.knowthenix.domain.entities.question;

import io.dpopkov.knowthenix.domain.entities.ModifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@Entity(name = "category")
public class CategoryEntity extends ModifiableEntity {

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    public CategoryEntity() {
    }

    public CategoryEntity(String name) {
        this.name = name;
    }

    public CategoryEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryEntity)) return false;
        CategoryEntity other = (CategoryEntity) o;
        return getName() != null ? getName().equals(other.getName()) : other.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Category{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
    }
}
