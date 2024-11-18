package com.oauth.implementation.repositories;

import com.oauth.implementation.entities.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface ImagenRepository extends JpaRepository<Imagen, String>{

}
