/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.client.impl;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ContentType;

import com.chenjw.logger.Logger;
import com.chenjw.logger.LoggerFactory;

/**
 * 请求结果模型
 * @author chao.xiuc
 * @version $Id: HttpRequestResult.java, v 0.1 2013-8-27 下午10:32:49 chao.xiuc Exp $
 */
public class HttpRequestResult {

    /** log */
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestResult.class);

    /** 邮件类型 */
    ContentType                 contentType;
    /** 内容 */
    byte[]                      bytes;

    String                      name;

    /**
     * Getter method for property <tt>contentType</tt>.
     * 
     * @return property value of contentType
     */
    public ContentType getContentType() {
        return contentType;
    }

    /**
     * Setter method for property <tt>contentType</tt>.
     * 
     * @param contentType value to be assigned to property contentType
     */
    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    /**
     * Getter method for property <tt>bytes</tt>.
     * 
     * @return property value of bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Setter method for property <tt>bytes</tt>.
     * 
     * @param bytes value to be assigned to property bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * 取得字符串类型的内容
     * @return
     */
    public String getContent() {
        try {
            if (bytes == null) {
                return null;
            }
            if (contentType == null) {
                return new String(bytes, "utf-8");
            }
            if (contentType.getCharset() != null) {
                return new String(bytes, contentType.getCharset().name());
            } else {
                return new String(bytes, "utf-8");
            }
        } catch (UnsupportedEncodingException e1) {
            try {
                return new String(bytes, "utf-8");
            } catch (UnsupportedEncodingException e2) {
                LOGGER.error(e1.getMessage(), e1);
            }
        }
        return null;
    }

    /**
     * Getter method for property <tt>name</tt>.
     * 
     * @return property value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter method for property <tt>name</tt>.
     * 
     * @param name value to be assigned to property name
     */
    public void setName(String name) {
        this.name = name;
    }


}
