package com.oauth.implementation.service;


import java.util.Optional;
import java.util.UUID;

import com.oauth.implementation.entities.Imagen;
import com.oauth.implementation.repositories.ImagenRepository;
import com.oauth.implementation.service.error.ErrorServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImagenService {
	
    @Autowired
    private ImagenRepository repo;
    
    public void validar(MultipartFile archivo)throws ErrorServiceException {
        
        try {		
      	  
          if(archivo == null || archivo.isEmpty()){
              throw new ErrorServiceException("Debe indicar el nombre");
          }
          
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }   
      }
    
    @Transactional
    public Imagen crearImagen(MultipartFile archivo)throws ErrorServiceException{
    	
        try {	
    	  
    	  validar(archivo);
      
    	  Imagen imagen = new Imagen();
    	  imagen.setId(UUID.randomUUID().toString());
    	  imagen.setMime(archivo.getContentType());
    	  imagen.setNombre(archivo.getName());
    	  imagen.setContenido(archivo.getBytes());
    	  imagen.setEliminado(false);
            
          return repo.save(imagen);
            
            
        } catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }  
 
    }
    
    @Transactional
    public Imagen modificarImagen(String idImagen, MultipartFile archivo)throws ErrorServiceException{
    	
    	try {
    		
    		validar(archivo);

    		Imagen imagen = buscarImagen(idImagen);
    		imagen.setMime(archivo.getContentType());
    		imagen.setNombre(archivo.getName());
    		imagen.setContenido(archivo.getBytes());
            
            return repo.save(imagen);
            
    	} catch (ErrorServiceException e) {
            throw e;
        } catch (Exception ex){
            ex.printStackTrace();
            throw new ErrorServiceException("Error de Sistemas");
        }      

    }
    
    public Imagen buscarImagen(String idImagen) throws ErrorServiceException{

        try {
            
            if (idImagen == null || idImagen.trim().isEmpty()) {
                throw new ErrorServiceException("Debe indicar una imagen");
            }

            Optional<Imagen> optional = repo.findById(idImagen);
            Imagen imagen = null;
            if (optional.isPresent()) {
            	imagen= optional.get();
    			if (imagen.isEliminado()){
                    throw new ErrorServiceException("No se encuentra la imagen indicada");
                }
    		}
            
            return imagen;
            
        } catch (ErrorServiceException ex) {  
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ErrorServiceException("Error de sistema");
        }
    }
    
}
