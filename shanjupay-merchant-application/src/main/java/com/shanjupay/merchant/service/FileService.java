package com.shanjupay.merchant.service;

import java.sql.BatchUpdateException;

/**
 * @Classname FileService
     * @Description 文件服务
 * @Date 2020/5/8 16:32
 * @Created by 小二哥
 */
public interface FileService {
    /**
     * 上传文件
     * @param bytes
     * @param fileName
     * @return
     * @throws BatchUpdateException
     */
     String upload(byte[] bytes,String fileName) throws BatchUpdateException;
}
