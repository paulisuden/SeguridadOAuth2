package com.oauth.implementation.repositories;

import java.util.List;

import com.oauth.implementation.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



public interface EditorialRepository extends JpaRepository<Editorial, String> {
    
    @Query ("SELECT e FROM Editorial e WHERE e.nombre = :nombre")
    public Editorial buscarEditorialPorNombre (@Param ("nombre") String nombre) ;
   
    @Query("SELECT e FROM Editorial e WHERE "
                    + "CONCAT(e.id, e.nombre)"
                    + "LIKE %?1% ")
    public List<Editorial> listarEditorialPorFiltro(@Param("filtro") String filtro);

}
