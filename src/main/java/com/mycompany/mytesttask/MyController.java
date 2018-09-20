package com.mycompany.mytesttask;

import com.mycompany.mytesttask.domain.MyEntity;
import com.mycompany.mytesttask.repos.MyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
public class MyController {
    @Autowired
    private MyRepository myRepo;

    //фильтр по необходимости
    @GetMapping("/main")
    public String find(
            @RequestParam(name="need", required=false, defaultValue="all") String need,
            Model model
    ) {
        int countEntity = (int) myRepo.count();
        int countPage = (countEntity-1)/10+1;
        Pageable page =  PageRequest.of(countPage-1, 10);

        Iterable<MyEntity> listPartsNeed;
        if (need.equals("y")) {
            listPartsNeed = myRepo.findByNeed(true, page);
        }
        else {
            if (need.equals("n")) {
                listPartsNeed = myRepo.findByNeed(false, page);
            }
            else {
                listPartsNeed = myRepo.findAll(page);
            }
        }

        model.addAttribute("n",myRepo.compCount());
        model.addAttribute("listParts", listPartsNeed);
        pagesMethod(model);
        return "main.html";
    }


    @GetMapping
    public String main(Model model) {
        pagesMethod(model);
        Iterable<MyEntity> listParts = myRepo.findAll();

        model.addAttribute("listParts", listParts);
        model.addAttribute("n",myRepo.compCount());


        return "main.html";
    }

    public void pagesMethod(Model model) {
        Integer numberPage = Math.toIntExact((myRepo.count() -1)/ 10)+1;

        Integer[] pages = new Integer[(numberPage)];
        for (int i = 0; i < numberPage; i ++) {
            pages[i] = i+1;
        }
        model.addAttribute("pages", pages);
    }



    @GetMapping("pagination")
    public String pagination (
            @RequestParam Integer numberPage, Model model
    ) {
        System.out.println(numberPage);
        Iterable<MyEntity> messages = myRepo.findPartsPage((numberPage-1)*10);
        model.addAttribute("listParts", messages);
        pagesMethod(model);
        return "main.html";
    }



    //добавление детали
    @PostMapping
    public String add(
            @RequestParam String name, @RequestParam Boolean need, @RequestParam Integer count,
                      Model model
    ){

        MyEntity myEntity = new MyEntity(name, need, count);
        myRepo.save(myEntity);

        int countEntity = (int) myRepo.count();
        int countPage = (countEntity-1)/10+1;
        Pageable page =  PageRequest.of(countPage-1, 10);
        Iterable<MyEntity> messages = myRepo.findAll(page);

        model.addAttribute("listParts", messages);
        model.addAttribute("n",myRepo.compCount());
        pagesMethod(model);

        return "main.html";
    }

    //удаление детали
    @GetMapping("delete")
    public String delete(
            @RequestParam Integer id, Model model
    ) {

        myRepo.deleteById(id);
        Pageable page =  PageRequest.of(0, 10);
        Iterable<MyEntity> messages = myRepo.findAll(page);
        model.addAttribute("n",myRepo.compCount());
        model.addAttribute("listParts", messages);
        pagesMethod(model);
        return "main.html";
    }

    //открытие формуы для редактирования, передача параметров в модель
    @GetMapping("edit")
    public String edit(
            @RequestParam Integer id, Model model
    ){
        Optional<MyEntity> myEntity = myRepo.findById(id);
            model.addAttribute("nameEdit", myEntity.get().getName());
            model.addAttribute("needEdit", myEntity.get().getNeed());
            model.addAttribute("countEdit", myEntity.get().getCount());
           model.addAttribute("n",myRepo.compCount());
        myRepo.deleteById(id);

        return "edit.html";
    }


    //редактирование
    @PostMapping("edit.html")
    public String editHTML (
            @RequestParam Integer id, @RequestParam String name, @RequestParam Boolean need,
            @RequestParam Integer count,
            Model model
    ) {
        myRepo.findById(id).get().setCount(count);
        myRepo.findById(id).get().setName(name);
        myRepo.findById(id).get().setNeed(need);
        Iterable<MyEntity> messages = myRepo.findAll();
        model.addAttribute("listParts", messages);
        model.addAttribute("n",myRepo.compCount());
        pagesMethod(model);
        return "main.html";
    }


    //фильтр по имени
    @PostMapping("/filter")
    public String filter (
            @RequestParam String filter, Model model
    ) {
        int countEntity = (int) myRepo.count();
        int countPage = (countEntity-1)/10+1;
        Pageable page =  PageRequest.of(countPage-1, 10);

        List<MyEntity> listPartsFilter =  myRepo.findByName(filter, page);

        model.addAttribute("listParts", listPartsFilter);
        model.addAttribute("n",myRepo.compCount());
        pagesMethod(model);
        return "main.html";
    }


}