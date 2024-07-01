package com.cibertec.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.demo.model.Producto;
import com.cibertec.demo.repository.ProductoRepository;

@Service
public class ProductoServiceImp implements ProductoService{

	@Autowired
	private ProductoRepository productoRepository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Producto> findAll() {
		return (List<Producto>) productoRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Producto> findAll(Pageable pageable) {
		return productoRepository.findAll(pageable);
	}
	
	@Override
    @Transactional
    public void save(Producto producto) {
		productoRepository.save(producto);
    }

	@Override
	@Transactional
	public void delete(Long id) {
		productoRepository.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public Producto findOne(Long id) {
		return productoRepository.findById(id).orElse(null);
	}
}
