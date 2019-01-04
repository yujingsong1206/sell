package com.yjs.controller;

import com.yjs.dataobject.ProductInfo;
import com.yjs.service.FileService;
import org.assertj.core.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
public class FIleController {

    @Autowired
    private FileService fileService;

    @GetMapping("/list")
    public ModelAndView list(){
        return new ModelAndView("file/list");
    }

    @PostMapping("/upload")
    public String uoload(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request){
        Map fileMap = new HashMap();

        if(file.isEmpty()){
            fileMap.put("uri","");
            fileMap.put("url", "");
            fileMap.put("msg", "失败");
            return fileMap.toString();
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
//        String url = "http://image.yjs.com/" + targetFileName;
        String url = "http://image.yjs.com/" + targetFileName;
        fileMap.put("uri",targetFileName);
        fileMap.put("url", url);
        fileMap.put("msg", "成功");
        return fileMap.toString();
    }

}
