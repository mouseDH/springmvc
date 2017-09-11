package com.spring_demo.controller;

import com.spring_demo.model.BlogEntity;
import com.spring_demo.model.UserEntity;
import com.spring_demo.repository.BlogRepository;
import com.spring_demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by hank on 17-9-11.
 */
@Controller
public class BlogController {

    @Autowired
    BlogRepository blogRepository;
    @Autowired
    UserRepository userRepository;


    /**
     * 查询blog
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "/admin/blogs",method = RequestMethod.GET)
    public String showBlogs(ModelMap modelMap){
        List<BlogEntity> blogList = blogRepository.findAll();
        modelMap.addAttribute("blogList",blogList);
        return "admin/blogs";
    }


    /**
     * 添加博文界面
     * @param modelMap
     * @return
     */
    @RequestMapping(value = "admin/blogs/add",method = RequestMethod.GET)
    public String addBlog(ModelMap modelMap){
        List<UserEntity> userList = userRepository.findAll();
        modelMap.addAttribute("userList",userList);
        return "admin/addBlog";
    }


    /**
     * 添加博文转到博文展现界面
     * @param blogEntity
     * @return
     */
    @RequestMapping(value = "admin/blogs/addP",method = RequestMethod.POST)
    public String addBlogPost(@ModelAttribute("blog") BlogEntity blogEntity){
        System.out.println(blogEntity.getTitle());
        System.out.println(blogEntity.getUserByUserId().getNickname());
        blogRepository.saveAndFlush(blogEntity);
        return "redirect:/admin/blogs";
    }


    // 查看博文详情，默认使用GET方法时，method可以缺省
    @RequestMapping("/admin/blogs/show/{id}")
    public String showBlog(@PathVariable("id") int id, ModelMap modelMap) {
        BlogEntity blog = blogRepository.findOne(id);
        modelMap.addAttribute("blog", blog);
        return "admin/blogDetail";
    }


    @RequestMapping("/admin/blogs/update/{id}")
    public String updateBlog(@PathVariable("id") int id, ModelMap modelMap) {
        // 是不是和上面那个方法很像
        BlogEntity blog = blogRepository.findOne(id);
        List<UserEntity> userList = userRepository.findAll();
        modelMap.addAttribute("blog", blog);
        modelMap.addAttribute("userList", userList);
        return "admin/updateBlog";
    }

    // 修改博客内容，POST请求
    @RequestMapping(value = "/admin/blogs/updateP", method = RequestMethod.POST)
    public String updateBlogP(@ModelAttribute("blogP") BlogEntity blogEntity) {
        // 更新博客信息
        blogRepository.updateBlog(blogEntity.getTitle(), blogEntity.getUserByUserId().getId(),
                blogEntity.getContent(), blogEntity.getPubDate(), blogEntity.getId());
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }

    // 删除博客文章
    @RequestMapping("/admin/blogs/delete/{id}")
    public String deleteBlog(@PathVariable("id") int id) {
        blogRepository.delete(id);
        blogRepository.flush();
        return "redirect:/admin/blogs";
    }

}
