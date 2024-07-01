package com.cibertec.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.demo.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
