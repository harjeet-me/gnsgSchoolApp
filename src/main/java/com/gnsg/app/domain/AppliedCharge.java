package com.gnsg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A AppliedCharge.
 */
@Entity
@Table(name = "applied_charge")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "appliedcharge")
public class AppliedCharge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "ammount")
    private Double ammount;

    @Column(name = "due_interval")
    private Integer dueInterval;

    @ManyToMany(mappedBy = "charges")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public AppliedCharge type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmmount() {
        return ammount;
    }

    public AppliedCharge ammount(Double ammount) {
        this.ammount = ammount;
        return this;
    }

    public void setAmmount(Double ammount) {
        this.ammount = ammount;
    }

    public Integer getDueInterval() {
        return dueInterval;
    }

    public AppliedCharge dueInterval(Integer dueInterval) {
        this.dueInterval = dueInterval;
        return this;
    }

    public void setDueInterval(Integer dueInterval) {
        this.dueInterval = dueInterval;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public AppliedCharge students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public AppliedCharge addStudent(Student student) {
        this.students.add(student);
        student.getCharges().add(this);
        return this;
    }

    public AppliedCharge removeStudent(Student student) {
        this.students.remove(student);
        student.getCharges().remove(this);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppliedCharge)) {
            return false;
        }
        return id != null && id.equals(((AppliedCharge) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AppliedCharge{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", ammount=" + getAmmount() +
            ", dueInterval=" + getDueInterval() +
            "}";
    }
}
