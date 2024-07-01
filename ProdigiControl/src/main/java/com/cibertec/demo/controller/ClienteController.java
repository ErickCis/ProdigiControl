package com.cibertec.demo.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.demo.model.Cliente;
import com.cibertec.demo.service.ClienteService;
import com.cibertec.demo.util.pagination.PageRender;
import com.cibertec.demo.util.report.ClienteExporterExcel;
import com.cibertec.demo.util.report.ClienteExporterPDF;
import com.lowagie.text.DocumentException;

public class ClienteController {

	@Autowired
    private ClienteService clienteService;
    
    @GetMapping("/ver/{id}")
    public String verDetallesDelCliente(@PathVariable(value = "id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
        Cliente cliente = clienteService.findOne(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar_cliente";
        }
        
        modelo.put("cliente", cliente);
        modelo.put("titulo", "Detalles del cliente " + cliente.getNombre());
        return "ver_cliente";
    }
    
    @GetMapping({"/", "/listar", ""})
    public String listarClientes(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Cliente> cliente = clienteService.findAll(pageRequest);
        PageRender<Cliente> pageRender = new PageRender<>("/listar_cliente", cliente);
        
        modelo.addAttribute("titulo", "Listado de cliente");
        modelo.addAttribute("cliente", cliente);
        modelo.addAttribute("page", pageRender);
        
        return "listar_cliente";
    }
    
    @GetMapping("/form")
    public String mostrarFormularioDeRegistrarCliente(Map<String, Object> modelo) {
    	Cliente cliente = new Cliente();
        modelo.put("cliente", cliente);
        modelo.put("titulo", "Registro de clientes");
        return "form_cliente";
    }
    
    @PostMapping("/form")
    public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de cliente");
            return "form_cliente";
        }
        
        String mensaje = (cliente.getId() != null) ? "El cliente ha sido editato con exito" : "Cliente registrado con exito";
        
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success", mensaje);
        return "redirect:/listar_cliente";
    }
   
    @GetMapping("/form/{id}")
    public String editarCliente(@PathVariable(value = "id") Long id, Map<String, Object> modelo, RedirectAttributes flash) {
    	Cliente cliente = null;
        if (id > 0) {
        	cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addFlashAttribute("error", "El ID del cliente no existe en la base de datos");
                return "redirect:/listar_cliente";
            }
        } else {
            flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");
            return "redirect:/listar_cliente";
        }
        
        modelo.put("cliente", cliente);
        modelo.put("titulo", "Edición de cliente");
        return "form_cliente";
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
        	clienteService.delete(id);
            flash.addFlashAttribute("success", "Cliente eliminado con exito");
        }
        return "redirect:/listar_cliente";
    }
    
    @GetMapping("/exportarPDF")
    public void exportarListadoDeClientesEnPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".pdf";
        
        response.setHeader(cabecera, valor);
        
        List<Cliente> clientes = clienteService.findAll();
        
        ClienteExporterPDF exporter = new ClienteExporterPDF(clientes);
        exporter.exportar(response);
    }
    
    @GetMapping("/exportarExcel")
    public void exportarListadoDeClientesEnExcel(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/octet-stream");
        
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String fechaActual = dateFormatter.format(new Date());
        
        String cabecera = "Content-Disposition";
        String valor = "attachment; filename=Clientes_" + fechaActual + ".xlsx";
        
        response.setHeader(cabecera, valor);
        
        List<Cliente> clientes = clienteService.findAll();
        
        ClienteExporterExcel exporter = new ClienteExporterExcel(clientes);
        exporter.exportar(response);
    }
}
