package com.oauth.implementation.service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.oauth.implementation.entities.Autor;
import com.oauth.implementation.repositories.AutorRepository;
import com.oauth.implementation.service.error.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import jakarta.persistence.NoResultException;

@Service
public class AutorService {
	
    @Autowired
    private AutorRepository repo;

    public void validar(String nombre) throws ErrorServiceException {

      try {	
    	  
        if (nombre == null || nombre.trim().isEmpty()) {
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
    public Autor crearAutor(String nombre) throws ErrorServiceException {
    	
      try {	
    	  
        validar(nombre);
        
        try {
        	Autor autorAux = repo.buscarAutorPorNombre(nombre);
        	if (autorAux != null && !autorAux.isEliminado()) {
             throw new ErrorServiceException("Existe un autor con el nombre indicado");
        	} 
        } catch (NoResultException ex) {}
        
        Autor autor = new Autor();
        autor.setId(UUID.randomUUID().toString());
        autor.setNombre(nombre);
        autor.setEliminado(false);

        return repo.save(autor);
        
      }catch(ErrorServiceException e) {  
		   throw e;  
	  }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	  } 
    }
    
    @Transactional
    public Autor modificarAutor(String idAutor, String nombre) throws ErrorServiceException {
    
       try {
    	   
    	   validar(nombre);
    	   
    	   Autor autor = buscarAutor(idAutor);
    	   
    	   try {
           	Autor autorAux = repo.buscarAutorPorNombre(nombre);
           	if (autorAux != null && !autorAux.getId().equals(idAutor) && !autorAux.isEliminado()) {
                throw new ErrorServiceException("Existe un autor con el nombre indicado");
           	} 
           } catch (NoResultException ex) {}
    	   
    	   autor.setNombre(nombre);

           repo.save(autor);
    	   
    	   return autor;
        
       }catch(ErrorServiceException e) {  
		   throw e;  
	   }catch(Exception e) {
		   e.printStackTrace();
		   throw new ErrorServiceException("Error de Sistemas");
	   } 
       
    }
    
    @Transactional
    public void eliminarAutor(String idAutor) throws ErrorServiceException {

    	try {
    		
    		Autor autor = buscarAutor(idAutor);
    		autor.setEliminado(true);
            repo.save(autor);
    		
    	} catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }

    }

    public Autor buscarAutor(String idAutor) throws ErrorServiceException{

        try {
            
            if (idAutor == null || idAutor.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el autor");
            }

            Optional<Autor> optional = repo.findById(idAutor);
            Autor autor = null;
            if (optional.isPresent()) {
            	autor= optional.get();
    			if (autor.isEliminado()){
                    throw new ErrorServiceException("No se encuentra el autor indicado");
                }
    		}
            
            return autor;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }

    public List<Autor> listarAutor() throws ErrorServiceException{
    	
    	try {	
    	
          return repo.findAll();
          
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }  
    }
    
    public List<Autor> listarAutorPorFiltro(String filtro) throws ErrorServiceException{
    	
    	try {	
    	
    		if (filtro == null || filtro.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar el criterio de búsqueda");
            }
    		
    		return repo.listarAutorPorFiltro(filtro);
          
    	} catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }  
    }
    

}
