package com.studyroom.controller;

import com.studyroom.tools.dto.FileResultDto;
import com.studyroom.tools.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * 文件上传接口
 */
@RestController()
@RequestMapping("/File")
public class FileController {

    /**
     * 批量文件上传
     * @param files 上传的文件
     * @return
     */
    @PostMapping("/BatchUpload")
    public ArrayList<FileResultDto> uploadFile(@RequestParam("file") MultipartFile[] files,HttpServletRequest request) {
        //定义一个存储文件的列表
        ArrayList<FileResultDto> fileResultDtos = new ArrayList<>();

        //获取项目运行的绝对路径
        String filePath = System.getProperty("user.dir");

        //循环处理一下文件 是否满足格式,不满足直接报错给前端
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new CustomException("文件不能为空");
            }
            if (file.getSize() <= 0) {
                throw new CustomException("上传的文件不能为空!请重新上传");
            }
        }
        //循环保存文件到项目的src\main\resource\static路径下
        for (MultipartFile file : files) {
            //获取文件原始的名称
            String originFileName = file.getOriginalFilename();
           //随机生成一个时间
            Long time = new Date().getTime();

            //声明一个保存目录的路径
            String dirPath = filePath + "\\src\\main\\resources\\static\\" + time;

            //创建一个文件或者文件夹的操作对象
            File dirFile = new File(dirPath);

            //判断文件是否存在 不存在的话执行下面的代码
            if (!dirFile.exists()) {
               //创建这个目录
                dirFile.mkdirs();
            }
            //定义一个文件输出流
            FileOutputStream fileOutputStream = null;
            //try catch处理流  防止报错导致系统崩掉
            try {
                //创建一个文件
                fileOutputStream = new FileOutputStream(dirPath +"\\"+ originFileName);
                //把前端传入的内容以byte是格式写入到流里面
                fileOutputStream.write(file.getBytes());
                //结束流
                fileOutputStream.flush();
                //关闭流
                fileOutputStream.close();
                //定义一个返回给前端的路径地址
                String url = "http://localhost:8088/" + time + "/" + originFileName;
                //加入到返回的列表中
                fileResultDtos.add(new FileResultDto(url, originFileName));

            } catch (java.io.IOException e) {
                e.printStackTrace();
            }

        }

        return fileResultDtos;
    }
}
