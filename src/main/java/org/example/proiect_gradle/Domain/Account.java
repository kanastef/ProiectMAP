package org.example.proiect_gradle.Domain;

import java.util.Objects;

public abstract class Account {
    String userName;
    String password;
    String email;
    String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(userName, account.userName) && Objects.equals(password, account.password) && Objects.equals(email, account.email) && Objects.equals(phone, account.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, email, phone);
    }
}
