package com.example.booking.repositories;

import com.example.booking.entities.ApplicationUser;
import org.springframework.data.repository.CrudRepository;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, String> {
}
