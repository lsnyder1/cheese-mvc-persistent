package org.launchcode.controllers;

import org.launchcode.models.Category;
import org.launchcode.models.Cheese;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping("cheese")
public class CheeseController {

    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CheeseDao cheeseDao;

    // Request path: /cheese
    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "My Cheeses");

        return "cheese/index";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddCheeseForm(Model model) {
        model.addAttribute("title", "Add Cheese");

        model.addAttribute("categories", categoryDao.findAll());
        model.addAttribute(new Cheese());

        return "cheese/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddCheeseForm(@ModelAttribute  @Valid Cheese newCheese,
                                       Errors errors,@RequestParam int categoryId, Model model) {
        Category cat = categoryDao.findOne(categoryId);
        newCheese.setCategory(cat);
        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Cheese");
            return "cheese/add";
        }

        cheeseDao.save(newCheese);
        return "redirect:";
    }

    @RequestMapping(value = "remove", method = RequestMethod.GET)
    public String displayRemoveCheeseForm(Model model) {
        model.addAttribute("cheeses", cheeseDao.findAll());
        model.addAttribute("title", "Remove Cheese");
        return "cheese/remove";
    }

    @RequestMapping(value = "remove", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@RequestParam int[] cheeseIds) {

        for (int cheeseId : cheeseIds) {
            cheeseDao.delete(cheeseId);
        }

        return "redirect:";
    }
    @RequestMapping(value="category/{categoryId}",method = RequestMethod.GET)
    public String category(Model model, @PathVariable Integer categoryId){
        Category category=categoryDao.findOne(categoryId);
        model.addAttribute(category);
        model.addAttribute("cheeses",cheeseDao.findAll());
        model.addAttribute("title","Cheeses with category "+category.getName());
        return"cheese/category";
    }
    @RequestMapping(value="edit/{cheeseId}",method=RequestMethod.GET)
    public String displayEditForm(Model model,@PathVariable Integer cheeseId){
        Cheese cheese=cheeseDao.findOne(cheeseId);
        model.addAttribute("title","Edit "+cheese.getName());
        model.addAttribute(cheese);
        model.addAttribute("categories",categoryDao.findAll());
        return"cheese/edit";
    }

}
