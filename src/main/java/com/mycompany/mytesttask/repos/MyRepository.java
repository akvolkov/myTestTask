package com.mycompany.mytesttask.repos;

import com.mycompany.mytesttask.domain.MyEntity;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.awt.print.Book;
import java.util.List;


public interface MyRepository extends CrudRepository<MyEntity, Integer> {
    List<MyEntity>findByName(String name, Pageable pageable);
    List<MyEntity>findByNeed(Boolean flag, Pageable pageable);
    Page<MyEntity> findAll(Pageable pageable);
    List<MyEntity> findById(Integer id, Sort sort);

    @Query(value = "select min(count) from my_entity where my_entity.need=1", nativeQuery = true)
    Integer compCount();

    @Query(value = "select * from my_entity limit :np, 10", nativeQuery = true)
    List<MyEntity>findPartsPage(@Param("np")Integer numberPage);


}
