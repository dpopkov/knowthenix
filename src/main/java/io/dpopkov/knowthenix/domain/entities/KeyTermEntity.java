package io.dpopkov.knowthenix.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "keyterm")
public class KeyTermEntity extends ModifiableEntity {

    @NotEmpty
    @Column(nullable = false, unique = true)
    private String name;
    private String description;

    public KeyTermEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "KeyTermEntity{" +
                "name='" + name + '\'' +
                '}' + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyTermEntity)) return false;
        KeyTermEntity keyTerm = (KeyTermEntity) o;
        return getName().equals(keyTerm.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
