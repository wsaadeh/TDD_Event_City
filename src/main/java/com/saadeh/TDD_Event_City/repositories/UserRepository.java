package com.saadeh.TDD_Event_City.repositories;

import com.saadeh.TDD_Event_City.entities.User;
import com.saadeh.TDD_Event_City.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);

    @Query(nativeQuery = true,value = """
            SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId, tb_role.authority
            FROM tb_user
            INNER JOIN tb_user_role on tb_user.id = tb_user_role.user_id
            INNER JOIN tb_role on tb_role.id = tb_user_role.role_id
            WHERE tb_user.email = :email
            """)
    List<UserDetailsProjection> searchUserAndRoleByEmail(String email);
}
