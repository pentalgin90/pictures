package ru.home.aws.pictures.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Subsciber implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String subsciberArn;
    private Boolean confirmed = false;

    public Subsciber(){}

    public Subsciber(String email, String subsciberArn){
        this.email = email;
        this.subsciberArn = subsciberArn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubsciberArn() {
        return subsciberArn;
    }

    public void setSubsciberArn(String subsciberArn) {
        this.subsciberArn = subsciberArn;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subsciber subsciber = (Subsciber) o;

        if (id != null ? !id.equals(subsciber.id) : subsciber.id != null) return false;
        if (email != null ? !email.equals(subsciber.email) : subsciber.email != null) return false;
        if (subsciberArn != null ? !subsciberArn.equals(subsciber.subsciberArn) : subsciber.subsciberArn != null)
            return false;
        return confirmed != null ? confirmed.equals(subsciber.confirmed) : subsciber.confirmed == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (subsciberArn != null ? subsciberArn.hashCode() : 0);
        result = 31 * result + (confirmed != null ? confirmed.hashCode() : 0);
        return result;
    }
}
