package org.launchcode.controllers;

import groovy.util.logging.Commons;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.launchcode.models.data.forms.AddMenuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @RequestMapping(value="", method=RequestMethod.GET)
    public String index(Model model){
        model.addAttribute("title","Menus");
        model.addAttribute("menus",menuDao.findAll());
        return"menu/index";
    }
    @RequestMapping(value="add",method= RequestMethod.GET)
    public String displayAddMenuForm(Model model){

        model.addAttribute("title","Add Menu");
        model.addAttribute(new Menu());
        return"menu/add";
    }
    @RequestMapping(value="add",method=RequestMethod.POST)
    public String processAddMenuForm(@ModelAttribute @Valid Menu menu,
                                     Errors errors, Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(menu);
        return "redirect:view/" + menu.getId();
    }
    @RequestMapping(value="view/{menuId}",method=RequestMethod.GET)
    public String viewMenu(@PathVariable int menuId,Model model){
    Menu menu=menuDao.findOne(menuId);
    model.addAttribute("cheeses",menu.getCheeses());
    model.addAttribute("title",menu.getName());
    model.addAttribute("menuId",menu.getId());
    return "menu/view";
    }

    @RequestMapping(value="add-item/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model,@PathVariable int menuId){

        Menu menu = menuDao.findOne(menuId);
        AddMenuItemForm form = new AddMenuItemForm(cheeseDao.findAll(),menu);

        model.addAttribute("title","Add item to menu: "+ menu.getName());
        model.addAttribute("form",form);
        return"menu/add-item";
    }

    @RequestMapping(value="add-item",method=RequestMethod.POST)
    public String addItem(Model model,@ModelAttribute @Valid AddMenuItemForm form,Errors errors){

        if(errors.hasErrors()){
            model.addAttribute("form",form);
            return "menu/add-item";
        }
        Cheese theCheese = cheeseDao.findOne(form.getCheeseId());
        Menu theMenu= menuDao.findOne(form.getMenuId());
        theMenu.addItem(theCheese);
        menuDao.save(theMenu);

        return "redirect:/menu/view" + theMenu.getId();

    }

}
