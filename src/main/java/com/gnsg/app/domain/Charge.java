package com.gnsg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

import com.gnsg.app.domain.enumeration.ChargeStatus;

/**
 * A Charge.
 */
@Entity
@Table(name = "charge")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "charge")
public class Charge implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "amt")
    private Double amt;

    @Column(name = "month")
    private String month;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "payment_date")
    private LocalDate paymentDate;

    @Column(name = "amt_paid")
    private Double amtPaid;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ChargeStatus status;

    @Column(name = "ref")
    private String ref;

    @ManyToOne
    @JsonIgnoreProperties(value = "charges", allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Charge name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmt() {
        return amt;
    }

    public Charge amt(Double amt) {
        this.amt = amt;
        return this;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public String getMonth() {
        return month;
    }

    public Charge month(String month) {
        this.month = month;
        return this;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public Charge dueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
        return this;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public Charge paymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getAmtPaid() {
        return amtPaid;
    }

    public Charge amtPaid(Double amtPaid) {
        this.amtPaid = amtPaid;
        return this;
    }

    public void setAmtPaid(Double amtPaid) {
        this.amtPaid = amtPaid;
    }

    public ChargeStatus getStatus() {
        return status;
    }

    public Charge status(ChargeStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ChargeStatus status) {
        this.status = status;
    }

    public String getRef() {
        return ref;
    }

    public Charge ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public Student getStudent() {
        return student;
    }

    public Charge student(Student student) {
        this.student = student;
        return this;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Charge)) {
            return false;
        }
        return id != null && id.equals(((Charge) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Charge{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", amt=" + getAmt() +
            ", month='" + getMonth() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", amtPaid=" + getAmtPaid() +
            ", status='" + getStatus() + "'" +
            ", ref='" + getRef() + "'" +
            "}";
    }
}
