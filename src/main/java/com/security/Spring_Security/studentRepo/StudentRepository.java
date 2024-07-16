package com.security.Spring_Security.studentRepo;

import com.security.Spring_Security.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student ,Long> {
    Optional<Student> findStudentByEmail(String email);
}
