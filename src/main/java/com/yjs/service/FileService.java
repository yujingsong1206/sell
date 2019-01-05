package com.yjs.service;

import org.springframework.web.multipart.MultipartFile;


public interface FileService {

    String upload(MultipartFile file, String path, String width, String height, String isCompress);

}
