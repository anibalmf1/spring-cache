package br.com.anibal.armory.springcache.controllers;

import br.com.anibal.armory.springcache.models.User;
import br.com.anibal.armory.springcache.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = service.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) throws InterruptedException {
        Optional<User> user = Optional.ofNullable(service.getUserById(id));
        return user.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@RequestBody User user) {
        Optional<User> created = Optional.of(service.saveUser(user));
        service.putCacheUserAnnotationMethod(created.get());
        return created.map(ResponseEntity::ok).get();
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Optional<User> created = Optional.of(service.saveUserUpdatingCache(user));
        return created.map(ResponseEntity::ok).get();
    }

    @GetMapping("/cache/{key}")
    public ResponseEntity<?> getCache(@PathVariable String key) {
        return ResponseEntity.ok(service.getDirectFromCache(Integer.valueOf(key)));
    }
}
