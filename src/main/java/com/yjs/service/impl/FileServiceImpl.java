package com.yjs.service.impl;


import com.yjs.service.FileService;
import com.yjs.utils.FTPUtil;
import com.yjs.utils.ImgCompressUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Override
    public String upload(MultipartFile file, String path, String width, String height, String isCompress) {
        //获取文件名
        String fileName = file.getOriginalFilename();
        //获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        //上传成功后保存原文件名
        String uploadFileName = UUID.randomUUID().toString() + "." + fileExtensionName;
        //上传成功后保存压缩文件名
        String uploadFileNameCompress = UUID.randomUUID().toString() + "_compress." + fileExtensionName;
        log.info("开始上传文件，上传的文件名：{}，上传的路径：{}，新文件名：{}", fileName, path, uploadFileName);

        File fileDir = new File(path);
        if(!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path, uploadFileName);
        File compressFile = new File(path, uploadFileNameCompress);
        try {
            file.transferTo(targetFile);
            //这时文件上传已成功。现在将targetFile上传到我们的ftp服务器上，上传完后删除upload下面的文件

            //判断是否需要压缩
            if(StringUtils.equals("true", isCompress)){
                String compressPath = path + "/" + uploadFileNameCompress;//压缩后文件
                String compressPathBefore = path + "/" + uploadFileName;//压缩原文件
                if(!StringUtils.isEmpty(width) && !StringUtils.isEmpty(height)){
                    ImgCompressUtil.compress(compressPathBefore,Integer.valueOf(width),Integer.valueOf(height),compressPath);
                } else {
                    //默认压缩为400*400
                    ImgCompressUtil.compress(compressPathBefore,400,400,compressPath);
                }
                //这是压缩文件已经上传成功。现在将compressFile上传到我们的ftp服务器上，上传完后删除upload下面的文件
                FTPUtil.uploadFile(Lists.newArrayList(compressFile));
                compressFile.delete();
            }

            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            //已经上传到ftp服务器上

            targetFile.delete();
        } catch (IOException e) {
            log.error("上传文件异常", e);
            return null;
        }
        return (StringUtils.equals("true", isCompress))
                ?targetFile.getName() + "," + compressFile.getName():
                targetFile.getName();
    }

}
