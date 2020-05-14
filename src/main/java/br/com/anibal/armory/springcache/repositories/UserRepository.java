package br.com.anibal.armory.springcache.repositories;

import br.com.anibal.armory.springcache.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findAll();
}

