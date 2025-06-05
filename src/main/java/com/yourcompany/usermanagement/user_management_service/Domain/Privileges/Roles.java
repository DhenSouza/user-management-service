package com.yourcompany.usermanagement.user_management_service.Domain.Privileges;

import com.yourcompany.usermanagement.user_management_service.Domain.model.User;

public interface Roles {
    void applyPrivileges(User user);
}
