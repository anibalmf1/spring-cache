package br.com.anibal.armory.springcache.services;

import br.com.anibal.armory.springcache.models.User;
import br.com.anibal.armory.springcache.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Cacheable(value = "users", key = "#id")
    public User getUserById(Integer id) throws InterruptedException {
        Thread.sleep(1500);
        Optional<User> loaded = userRepository.findById(id);
        return loaded.orElse(null);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @CachePut(value = "users", key = "#user.id")
    public User putCacheUserAnnotationMethod(User user) {
        return user;
    }

    public User saveUserUpdatingCache(User user) {
        User created = saveUser(user);
        Cache cache = cacheManager.getCache("users");
        cache.put(user.getId(), created);
        return created;
    }

    public User getDirectFromCache(Integer key) {
        Cache cache = cacheManager.getCache("users");
        return (User) Optional.ofNullable(cache.get(key))
                .map(Cache.ValueWrapper::get)
                .orElse(null);
    }
}
