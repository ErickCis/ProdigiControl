package com.cibertec.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.demo.model.Cliente;
import com.cibertec.demo.repository.ClienteRepository;

@Service
public class ClienteServiceImp implements ClienteService{

	@Autowired
	private ClienteRepository clienteRepository; 
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		return clienteRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public void save(Cliente cliente) {
		clienteRepository.save(cliente);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		clienteRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        return optionalCliente.orElse(null);
	}

}
