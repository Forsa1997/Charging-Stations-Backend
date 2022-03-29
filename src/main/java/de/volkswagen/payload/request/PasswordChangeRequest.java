package de.volkswagen.payload.request;

import java.util.Set;

import javax.persistence.Id;
import javax.validation.constraints.*;

public class PasswordChangeRequest {

    private long id;

    @Size(min = 6, max = 40)
    private String oldPassword;

    @Size(min = 6, max = 40)
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public long getId(){
        return this.id;
    }

    public void setId(long id){
        this.id = id;
    }

}