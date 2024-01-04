package com.vashchenko.project.repositories;

import com.vashchenko.project.models.entities.Image;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ImageRepository extends JpaRepository<Image,Long> {
    @Query(value = "select * from image t where type_id=:id", nativeQuery = true)
    List<Image> findImagesByRoomTypeId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "delete from image where type_id=:id",nativeQuery = true)
    void deleteByTypeId(@Param("id") Long id);
}
