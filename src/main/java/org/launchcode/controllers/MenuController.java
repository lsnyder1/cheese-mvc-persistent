package org.launchcode.controllers;

import groovy.util.logging.Commons;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by lonny on 6/27/2017.
 */
@Controller
@RequestMapping(value="menu")
public class MenuController {

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private CheeseDao cheeseDao;

    @RequestMapping(value="")
    public String index(Model model){
        model.addAttribute("title","Menus");
        model.addAttribute("menus",menuDao.findAll());
        return"menu/index";
    }
    @RequestMapping(value="add",method= RequestMethod.GET)
    public String displayAddMenuForm(Model model){
        Menu menu=new Menu();
        model.addAttribute("title","Add Menu")
        model.addAttribute("menu",menu);
        return"menu/add";
    }
}
