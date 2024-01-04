package com.vashchenko.project.services;

import com.vashchenko.project.models.entities.Image;
import com.vashchenko.project.repositories.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    public void saveImage(Image image){
        imageRepository.save(image);
    }

    public void deleteImagesByRoomTypeId(Long id) {
        imageRepository.deleteByTypeId(id);
    }

    public List<String> getImagesByRoomTypeId(Long id){
        List<String> paths = new ArrayList<>();
       for(Image image: imageRepository.findImagesByRoomTypeId(id)){
           paths.add(image.getUrl());
       }
       return paths;
    }
}
