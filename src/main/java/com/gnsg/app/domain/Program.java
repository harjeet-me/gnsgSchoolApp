package com.gnsg.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import com.gnsg.app.domain.enumeration.EVENTTYPE;

import com.gnsg.app.domain.enumeration.EVENTLOCATION;

import com.gnsg.app.domain.enumeration.LANGARMENU;

import com.gnsg.app.domain.enumeration.EventStatus;

/**
 * not an ignored comment
 */
@ApiModel(description = "not an ignored comment")
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "program")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "program_type")
    private EVENTTYPE programType;

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private EVENTLOCATION location;

    @Column(name = "etime")
    private Instant etime;

    @Column(name = "family")
    private String family;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "with_langar")
    private Boolean withLangar;

    @Enumerated(EnumType.STRING)
    @Column(name = "langar_menu")
    private LANGARMENU langarMenu;

    @Column(name = "langar_time")
    private Instant langarTime;

    @Column(name = "due_amt", precision = 21, scale = 2)
    private BigDecimal dueAmt;

    @Column(name = "paid_amt", precision = 21, scale = 2)
    private BigDecimal paidAmt;

    @Column(name = "bal_amt", precision = 21, scale = 2)
    private BigDecimal balAmt;

    @Column(name = "reciept_number")
    private Long recieptNumber;

    @Column(name = "remark")
    private String remark;

    @Column(name = "booking_date")
    private Instant bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToOne
    @JsonIgnoreProperties(value = "programs", allowSetters = true)
    private Sevadar sevadar;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EVENTTYPE getProgramType() {
        return programType;
    }

    public Program programType(EVENTTYPE programType) {
        this.programType = programType;
        return this;
    }

    public void setProgramType(EVENTTYPE programType) {
        this.programType = programType;
    }

    public EVENTLOCATION getLocation() {
        return location;
    }

    public Program location(EVENTLOCATION location) {
        this.location = location;
        return this;
    }

    public void setLocation(EVENTLOCATION location) {
        this.location = location;
    }

    public Instant getEtime() {
        return etime;
    }

    public Program etime(Instant etime) {
        this.etime = etime;
        return this;
    }

    public void setEtime(Instant etime) {
        this.etime = etime;
    }

    public String getFamily() {
        return family;
    }

    public Program family(String family) {
        this.family = family;
        return this;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Program phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public Program address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean isWithLangar() {
        return withLangar;
    }

    public Program withLangar(Boolean withLangar) {
        this.withLangar = withLangar;
        return this;
    }

    public void setWithLangar(Boolean withLangar) {
        this.withLangar = withLangar;
    }

    public LANGARMENU getLangarMenu() {
        return langarMenu;
    }

    public Program langarMenu(LANGARMENU langarMenu) {
        this.langarMenu = langarMenu;
        return this;
    }

    public void setLangarMenu(LANGARMENU langarMenu) {
        this.langarMenu = langarMenu;
    }

    public Instant getLangarTime() {
        return langarTime;
    }

    public Program langarTime(Instant langarTime) {
        this.langarTime = langarTime;
        return this;
    }

    public void setLangarTime(Instant langarTime) {
        this.langarTime = langarTime;
    }

    public BigDecimal getDueAmt() {
        return dueAmt;
    }

    public Program dueAmt(BigDecimal dueAmt) {
        this.dueAmt = dueAmt;
        return this;
    }

    public void setDueAmt(BigDecimal dueAmt) {
        this.dueAmt = dueAmt;
    }

    public BigDecimal getPaidAmt() {
        return paidAmt;
    }

    public Program paidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
        return this;
    }

    public void setPaidAmt(BigDecimal paidAmt) {
        this.paidAmt = paidAmt;
    }

    public BigDecimal getBalAmt() {
        return balAmt;
    }

    public Program balAmt(BigDecimal balAmt) {
        this.balAmt = balAmt;
        return this;
    }

    public void setBalAmt(BigDecimal balAmt) {
        this.balAmt = balAmt;
    }

    public Long getRecieptNumber() {
        return recieptNumber;
    }

    public Program recieptNumber(Long recieptNumber) {
        this.recieptNumber = recieptNumber;
        return this;
    }

    public void setRecieptNumber(Long recieptNumber) {
        this.recieptNumber = recieptNumber;
    }

    public String getRemark() {
        return remark;
    }

    public Program remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public Program bookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
        return this;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public EventStatus getStatus() {
        return status;
    }

    public Program status(EventStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public Program createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Program createdBy(String createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Program lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Program lastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Sevadar getSevadar() {
        return sevadar;
    }

    public Program sevadar(Sevadar sevadar) {
        this.sevadar = sevadar;
        return this;
    }

    public void setSevadar(Sevadar sevadar) {
        this.sevadar = sevadar;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Program)) {
            return false;
        }
        return id != null && id.equals(((Program) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", programType='" + getProgramType() + "'" +
            ", location='" + getLocation() + "'" +
            ", etime='" + getEtime() + "'" +
            ", family='" + getFamily() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", address='" + getAddress() + "'" +
            ", withLangar='" + isWithLangar() + "'" +
            ", langarMenu='" + getLangarMenu() + "'" +
            ", langarTime='" + getLangarTime() + "'" +
            ", dueAmt=" + getDueAmt() +
            ", paidAmt=" + getPaidAmt() +
            ", balAmt=" + getBalAmt() +
            ", recieptNumber=" + getRecieptNumber() +
            ", remark='" + getRemark() + "'" +
            ", bookingDate='" + getBookingDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            "}";
    }
}
