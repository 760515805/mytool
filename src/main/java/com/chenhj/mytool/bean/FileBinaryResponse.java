package com.chenhj.mytool.bean;

/**
 * 
* Copyright: Copyright (c) 2018 Montnets
* 
* @ClassName: FileBinaryResponse.java
* @Description: 该类的功能描述
*
* @version: v1.0.0
* @author: chenhj
* @date: 2018年6月15日 上午9:58:16 
*
* Modification History:
* Date         Author          Version            Description
*---------------------------------------------------------*
* 2018年6月15日     chenhj          v1.0.0               修改原因
 */
public class FileBinaryResponse {
    /***
     * id
     */
    private String id;
    /***
     * 在线url地址
     */
    private String url;
    /**
     * 存储路径名称
     */
    private String store;

    public FileBinaryResponse(String id, String url, String store) {
        this.id = id;
        this.url = url;
        this.store = store;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
