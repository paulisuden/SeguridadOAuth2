package com.oauth.implementation.repositories;

import java.util.List;

import com.oauth.implementation.entities.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface AutorRepository extends JpaRepository<Autor, String> {
    

    @Query ("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Autor buscarAutorPorNombre (@Param ("nombre") String nombre) ;
    
    @Query("SELECT a FROM Autor a WHERE "
                    + "CONCAT(a.id, a.nombre)"
                    + "LIKE %?1% ")
    public List<Autor> listarAutorPorFiltro(@Param("filtro") String filtro);
    
    
}
