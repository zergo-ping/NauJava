package ru.Nikita.NauJava.transaction;

/**
 * DTO для регистрации пользователя
 */
public class RegistrationDto {
    
    private String email;
    private String password;
    private String passwordConfirm;
    private String fullName;

    public RegistrationDto() {}

    public RegistrationDto(String email, String password, String passwordConfirm, String fullName) {
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
