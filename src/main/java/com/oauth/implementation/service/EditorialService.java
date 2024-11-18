package com.oauth.implementation.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.oauth.implementation.entities.Editorial;
import com.oauth.implementation.repositories.EditorialRepository;
import com.oauth.implementation.service.error.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import jakarta.persistence.NoResultException;


@Service
public class EditorialService {

    @Autowired
    private EditorialRepository repo;

    public void validar(String nombre) throws ErrorServiceException {

      try {		
    	  
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServiceException("Debe indicar el nombre");
        }
        
      }catch(ErrorServiceException e) {  
		   throw e;  
	  }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	  }    
    }
    
    @Transactional
    public Editorial crearEditorial(String nombre) throws ErrorServiceException {

      try {		
    	  
        validar(nombre);
        
        try {
        	Editorial editorialAux = repo.buscarEditorialPorNombre(nombre);
        	if (editorialAux != null && !editorialAux.isEliminado()) {
             throw new ErrorServiceException("Existe una editorial con el nombre indicado");
        	} 
        } catch (NoResultException ex) {}
        
        Editorial editorial = new Editorial();
        editorial.setId(UUID.randomUUID().toString());
        editorial.setNombre(nombre);
        editorial.setEliminado(false);

        return repo.save(editorial);
        
      }catch(ErrorServiceException e) {  
		   throw e;  
	  }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	  }   
    }
    
    @Transactional
    public Editorial modificarEditorial(String idEditorial, String nombre) throws ErrorServiceException {
 
      try {		
         
    	  validar(nombre);
            
          Editorial editorial = buscarEditorial(idEditorial);
          
          try {
            Editorial editorialAux = repo.buscarEditorialPorNombre(nombre);
            if (editorialAux != null && !editorialAux.getId().equals(idEditorial) && !editorialAux.isEliminado()) {
               throw new ErrorServiceException("Existe una editorial con el nombre indicado");
            } 
          } catch (NoResultException ex) {}
          
          editorial.setNombre(nombre);
          
          return repo.save(editorial);
            
        }catch(ErrorServiceException e) {  
 		   throw e;  
 	  }catch(Exception e) {
 		   e.printStackTrace();
 		   throw new ErrorServiceException("Error de Sistemas");
 	  } 
    }
    
    @Transactional
    public void eliminarEditorial(String idEditorial) throws ErrorServiceException {

    	 try {		

            Editorial editorial = buscarEditorial(idEditorial);
            editorial.setEliminado(true);

             repo.save(editorial);
            
          }catch(ErrorServiceException e) {  
    		   throw e;  
    	  }catch(Exception e) {
    		   e.printStackTrace();
    		   throw new ErrorServiceException("Error de Sistemas");
    	  } 

    }

    @Transactional
    public Editorial buscarEditorial(String idEditorial) throws ErrorServiceException {
        
    	try {
            
            if (idEditorial == null || idEditorial.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar la editorial");
            }

            Optional<Editorial> optional = repo.findById(idEditorial);
            Editorial editorial = null;
            if (optional.isPresent()) {
            	editorial= optional.get();
    			if (editorial.isEliminado()){
                    throw new ErrorServiceException("No se encuentra la editorial indicada");
                }
    		}
            
            return editorial;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public List<Editorial> listarEditorial() throws ErrorServiceException {
    	
      try {
    	  
        return repo.findAll();
        
      }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	  }   
    }
    
    public List<Editorial> listarEditorialPorFiltro(String filtro) throws ErrorServiceException{
    	
    	try {	
    	
    		if (filtro == null || filtro.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el criterio de búsqueda");
            }
    		
    		return repo.listarEditorialPorFiltro(filtro);
          
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }  
    }
    
    

}
