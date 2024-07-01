package com.cibertec.demo.repository;

import com.cibertec.demo.model.Empleado;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
}


