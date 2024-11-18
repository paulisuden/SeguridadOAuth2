package com.oauth.implementation.controller;
import java.util.List;

import com.oauth.implementation.entities.Autor;
import com.oauth.implementation.service.AutorService;
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
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    private AutorService autorServicio;

 
    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////// VIEW: CREAR AUTOR /////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/registrar")
    public String irEditAlta() {
        return "autor_form";
    }
    
    @PostMapping("/registro")
    public String aceptarEditAlta(ModelMap modelo,  @RequestParam String nombre )  {

        try {
        	
        	autorServicio.crearAutor(nombre);
            modelo.put("exito", "La acción fue realizada correctamente");
            
            return "redirect:/regresoPage";

        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "autor_form";  
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "autor_form";
        }

    }
    
    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////////// VIEW: LISTA AUTOR ////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

      try {	
    	  
        List<Autor> autores = autorServicio.listarAutor();
        modelo.addAttribute("autores", autores);
       
      } catch (ErrorServiceException e) {
          modelo.put("error", e.getMessage()); 
      } catch (Exception e) {
          modelo.put("error", "Error de Sistemas");
      }
      
      return "autor_list";
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    ///////// VIEW: MODIFICAR AUTOR ////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/modificar/{id}")
    public String irEditModificar(ModelMap modelo, @PathVariable String id) throws ErrorServiceException{
        
        try {
        	
            Autor autor = autorServicio.buscarAutor(id);
            modelo.put("autor", autor);
         
            return "autor_modificar";
            
        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "autor_list";
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "autor_list";
        }
     
    }
    
    @PostMapping("/modificar/{id}")
    public String aceptarEditModificar(ModelMap modelo, @PathVariable String id, @RequestParam String nombre) {
        
        try {
        	
            Autor autor = autorServicio.buscarAutor(id);
            autor = autorServicio.modificarAutor(autor.getId(), nombre);
            modelo.put("autor", autor);
            modelo.put("exito", "La acción fue realizada correctamente");
            
            return "redirect:/regresoPage";
            
        } catch (ErrorServiceException e) {
            modelo.put("error", e.getMessage());
            return "autor_modificar";  
        } catch (Exception e) {
            modelo.put("error", "Error de Sistemas");
            return "autor_modificar"; 
        }    
           
    }

    //////////////////////////////////////////
    //////////////////////////////////////////
    //////////// VIEW: BAJA AUTOR //////////// 
    //////////////////////////////////////////
    //////////////////////////////////////////
    
    @GetMapping("/baja/{id}")
    public String eliminarAutor(ModelMap modelo, @PathVariable String id) {

    	try {
    		
    		autorServicio.eliminarAutor(id);
        	return "redirect:/regresoPage";
        
    	} catch (ErrorServiceException e) {
    		modelo.put("error", "Error de sistemas");
            return "autor_modificar";
    	} catch (Exception e) {
    		modelo.put("error", "Error de sistemas");
            return "autor_modificar";
    	}

    }
    
    


}