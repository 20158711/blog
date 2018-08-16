package org.wingstudio.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wingstudio.blog.common.CatalogVo;
import org.wingstudio.blog.common.Response;
import org.wingstudio.blog.util.SecurityUtil;
import org.wingstudio.blog.po.Catalog;
import org.wingstudio.blog.po.User;
import org.wingstudio.blog.service.CatalogService;
import org.wingstudio.blog.service.UserService;
import org.wingstudio.blog.util.ResponseUtil;

import java.util.List;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String listComments(
            @RequestParam(value = "username",required = true)String username,
            Model model){
        User user = userService.getUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);
        boolean isOwner=SecurityUtil.isOwner(username);
        model.addAttribute("isCatalogsOwner", isOwner);
        model.addAttribute("catalogs", catalogs);
        return "/userspace/u :: #catalogRepleace";
    }

    @PostMapping
    @PreAuthorize("authentication.name.equals(#catalogVo.username)")
    public ResponseEntity<Response> create(@RequestBody CatalogVo catalogVo){
         String username=catalogVo.getUsername();
        Catalog catalog = catalogVo.getCatalog();
        User user = userService.getUserByUsername(username);
        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> delete(@PathVariable("id")Long id,@RequestParam("username") String username){
        try {
            catalogService.removeCatalog(id);
        }catch (Exception e){
            return ResponseUtil.error(e);
        }
        return ResponseUtil.success();
    }

    @GetMapping("/edit")
    public String getCatalogEdit(Model model){
        model.addAttribute("catalog",new Catalog());
        return "/userspace/catalogedit";
    }

    @GetMapping("/edit/{id}")
    public String getCatalogById(@PathVariable("id")Long id,Model model){
        Catalog catalog = catalogService.getCatalogById(id);
        model.addAttribute("catalog",catalog);
        return "/userspace/catalogedit";
    }
}
