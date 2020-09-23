package com.in28minutes.springboot.web.controllers;

import com.in28minutes.springboot.web.model.Todo;
import com.in28minutes.springboot.web.services.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class TodoController {

    @Autowired
    TodoService service;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        binder.registerCustomEditor(Date.class,
                new CustomDateEditor(simpleDateFormat,false));
    }

    @RequestMapping(value = "/list-todos",method = RequestMethod.GET)
    public String showTodos(ModelMap modelMap) {
        String name = getLoggedInUserName();
        modelMap.put("todos",service.retrieveTodos(name));
        return "list-todos";
    }

    private String getLoggedInUserName() {
        Object principal =
                SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }

    @RequestMapping(value="/delete-todo", method = RequestMethod.GET)
    public String deleteTodo(@RequestParam int id){
        service.deleteTodo(id);
        return "redirect:/list-todos";
    }

    @RequestMapping(value = "/add-todo",method = RequestMethod.GET)
    public String showAddTodosPage(ModelMap model) {
        model.addAttribute("todo", new Todo(0, getLoggedInUserName(), "",
                new Date(), false));
        return "todo";
    }
    @RequestMapping(value = "/add-todo",method = RequestMethod.POST)
    public String addTodos ( ModelMap modelMap,@Valid Todo todo ,BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }
        service.addTodo(getLoggedInUserName(),todo.getDesc(),todo.getTargetDate(),false);
        return "redirect:/list-todos";
    }

    @RequestMapping(value = "/update-todo",method = RequestMethod.GET)
    public String showUpdatePage(@RequestParam int id,ModelMap model) {
        Todo todo = service.retrieveTodo(id);
        model.put("todo",todo);
        return "todo";
    }

    @RequestMapping(value = "/update-todo",method = RequestMethod.POST)
    public String updateTodo(ModelMap modelMap,@Valid Todo todo ,BindingResult result) {
        if (result.hasErrors()) {
            return "todo";
        }
        todo.setUser(getLoggedInUserName());

        service.updateTodo(todo);
        return "redirect:/list-todos";
    }

}
