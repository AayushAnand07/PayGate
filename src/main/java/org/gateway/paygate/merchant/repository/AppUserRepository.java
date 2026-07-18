package org.gateway.paygate.merchant.repository;

import org.gateway.paygate.merchant.entity.AppUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUsers, UUID> {
}
