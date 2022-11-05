package com.habit.thehabit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.habit.thehabit.app.dao.repository")
//@EnableJpaRepositories(basePackages = "com.habit.thehabit.member.command.domain.repository")
public class JpaRepositoryConfig {
}
