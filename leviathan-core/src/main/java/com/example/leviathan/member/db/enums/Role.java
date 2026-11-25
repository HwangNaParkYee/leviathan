package com.example.leviathan.member.db.enums;

import java.util.List;

public enum Role {
    ROLE_ADMIN, ROLE_MEMBER;

    public List<String> getAuthorityNames() {
        if (this == ROLE_ADMIN) {
            return List.of(ROLE_ADMIN.name(), ROLE_MEMBER.name());
        }
        return List.of(name());
    }
}
