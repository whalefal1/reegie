package com.example.reggie.controller;


import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")

    public R<String> upload(MultipartFile file){
        //file是一个临时文件，转存后这个文件就会被删除
    log.info("文件上传已启动");
    String originalFilename = file.getOriginalFilename();
        String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + substring;
        File dir = new File(basePath);
        if(!dir.exists()){
            log.info("新文件创建");
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("文件上传成功");
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        log.info("文件回显开始");
        //输入流读取文件内容
        FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
        //输出流，将文件写回浏览器
        ServletOutputStream outputStream = response.getOutputStream();
        //设置文件类型
        response.setContentType("/image/jpeg");
        //文件输入流读取文件，将文件保存到bytes数组，文件输出流读取bytes数组，传递给前端页面
        int length = 0;
        byte[] bytes = new byte[1024];
        while((length = fileInputStream.read(bytes)) != -1){
                        outputStream.write(bytes,0,length);
                        outputStream.flush();
        }
        outputStream.close();
        fileInputStream.close();
    }
}
