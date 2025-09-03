package com.saadeh.TDD_Event_City.projections;

public interface UserDetailsProjection {
    String getUserName();
    String getPassword();
    Long getRoleId();
    String getAuthority();
}
