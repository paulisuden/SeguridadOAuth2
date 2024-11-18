package com.oauth.implementation.controller;
import java.util.List;

import com.oauth.implementation.entities.Editorial;
import com.oauth.implementation.service.EditorialService;
import com.oauth.implementation.service.error.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialService ediotrialService;

    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////// VIEW: CREAR EDITORIAL //////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/registrar")
    public String irEditAlta() {
        return "editorial_form";
    }

    @PostMapping("/registro")
    public String aceptarEditAlta(ModelMap modelo, @RequestParam String nombre)  {

        try {
        	
        	ediotrialService.crearEditorial(nombre);
            modelo.put("exito", "La accion fue realizada correctamente");
            
            return "redirect:/regresoPage";

        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "editorial_form";  
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "editorial_form";
        }    
    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ////////// VIEW: LISTA EDITORIAL /////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

      try {	
    	  
        List<Editorial> editorialesLista = ediotrialService.listarEditorial();
        modelo.addAttribute("editoriales", editorialesLista);

      } catch (ErrorServiceException ex) {
          modelo.put("error", ex.getMessage());
      }catch (Exception e) {
      	  modelo.put("error", "Error de Sistemas");
      }
      
      return "editorial_list";
    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////// VIEW: MODIFICAR EDITORIAL ///////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/modificar/{id}")
    public String irEditModificar(ModelMap modelo, @PathVariable String id) throws ErrorServiceException{
        
        try {
        	
            Editorial editorial = ediotrialService.buscarEditorial(id);
            modelo.put("editorial", editorial);
        
            return "editorial_modificar";
            
        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "editorial_list";
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "editorial_list";
        }

    }

    @PostMapping("/modificar/{id}")
    public String aceptarEditModificar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        
        try {
        	
            Editorial editorial = ediotrialService.modificarEditorial(id, nombre);
            modelo.put("editorial", editorial);
            modelo.put("exito", "La accion fue realizada correctamente");
            
            return "redirect:/regresoPage";
            
        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "editorial_modificar";  
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "editorial_modificar"; 
        } 
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    /////////// VIEW: BAJA EDITORIAL /////////
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/baja/{id}")
    public String eliminarEditorial(ModelMap modelo, @PathVariable String id) {

        try {
        	
        	ediotrialService.eliminarEditorial(id);
        	
        	return "redirect:/regresoPage";
            
        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "editorial_list";
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "editorial_list";
        }

    }

}
