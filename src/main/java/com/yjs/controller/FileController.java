package com.yjs.controller;

import com.yjs.VO.ResultVO;
import com.yjs.config.FileUrlConfig;
import com.yjs.service.FileService;
import com.yjs.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private FileUrlConfig fileUrlConfig;

    @GetMapping("/list")
    public ModelAndView list(){
        return new ModelAndView("file/list");
    }

    /**
     * 图片上传
     * @param file 图片
     * @param width 压缩宽度
     * @param height 压缩高度
     * @param isCompress 是否需要压缩，默认为true，压缩为400*400
     * @return
     */
    @PostMapping("/upload")
    public ResultVO uoload(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(value = "width", required = false) String width,
                           @RequestParam(value = "height", required = false) String height,
                           @RequestParam(value = "isCompress", defaultValue = "true") String isCompress,
                           HttpServletRequest request){
        Map fileMap = new HashMap();

        if(file.isEmpty()){
            return ResultVOUtil.error(60, "文件为空");
        }
        log.info("进入文件上传");
        String path = request.getSession().getServletContext().getRealPath("upload");
        log.info("获取path={}", path);
        String targetFileName = fileService.upload(file, path, width, height, isCompress);
        String[] fileName = targetFileName.split(",");
        fileMap.put("url", fileUrlConfig.getPrefix() + fileName[0]);
        if(fileName.length == 2){
            fileMap.put("compressUrl", fileUrlConfig.getPrefix() + fileName[1]);
        }
        return ResultVOUtil.success(0, "成功", fileMap);
    }

}
