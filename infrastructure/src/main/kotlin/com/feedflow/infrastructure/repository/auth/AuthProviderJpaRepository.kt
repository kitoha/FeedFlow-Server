package com.feedflow.infrastructure.repository.auth

import com.feedflow.infrastructure.entity.auth.AuthProviderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AuthProviderJpaRepository : JpaRepository<AuthProviderEntity, Long>{
}