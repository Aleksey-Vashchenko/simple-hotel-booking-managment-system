package com.vashchenko.project.services;

import com.vashchenko.project.models.entities.User;
import com.vashchenko.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    public void deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            try {
                userRepository.deleteById(id);
            } catch (DataIntegrityViolationException e) {
                throw new RuntimeException("Не удалось удалить пользователя по причине: " + e.getMessage(), e);
            }
        } else {
            throw new IllegalArgumentException("Пользователь с таким id не найден: " + id);
        }
    }

    public Iterable<User> findAllByLoginNot(String name) {
        return userRepository.findAllByLoginNot(name);
    }

    public User findById(Long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            return user.get();
        }
        else throw new IllegalArgumentException("Пользователь с таким id не найден: "+id);
    }
    public void save(User updated) {
        userRepository.save(updated);
    }

    public User getUserByLogin(String login){
        Optional<User> userOptional = userRepository.findUserByLogin(login);
        if(userOptional.isPresent()){
            return userOptional.get();
        }
        else return null;
    }
}
