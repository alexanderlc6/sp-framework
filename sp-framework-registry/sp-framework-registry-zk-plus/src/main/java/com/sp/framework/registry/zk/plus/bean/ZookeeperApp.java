/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.sp.framework.registry.zk.plus.bean;

import java.io.Serializable;
import java.util.Date;

public class ZookeeperApp implements Serializable{

    private static final long serialVersionUID = -8082482990110419202L;

    private Long id;

    private String appName;

    private String appKey;

    private String secret;

    private String zkAddr;

    private Date createDate;

    private String ipwriteList;

    private String dealUserEmail;

    private Integer lifecycle;

    private String createBy;

    private String modifyBy;

    private java.util.Date modifyDate;

    public ZookeeperApp(){
    }

    public ZookeeperApp(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setAppName(String value){
        this.appName = value;
    }

    public String getAppName(){
        return this.appName;
    }

    public void setAppKey(String value){
        this.appKey = value;
    }

    public String getAppKey(){
        return this.appKey;
    }

    public void setSecret(String value){
        this.secret = value;
    }

    public String getSecret(){
        return this.secret;
    }

    public void setZkAddr(String value){
        this.zkAddr = value;
    }

    public String getZkAddr(){
        return this.zkAddr;
    }

    public void setCreateDate(java.util.Date value){
        this.createDate = value;
    }

    public java.util.Date getCreateDate(){
        return this.createDate;
    }

    public void setIpwriteList(String value){
        this.ipwriteList = value;
    }

    public String getIpwriteList(){
        return this.ipwriteList;
    }

    public void setDealUserEmail(String value){
        this.dealUserEmail = value;
    }

    public String getDealUserEmail(){
        return this.dealUserEmail;
    }

    public void setLifecycle(Integer value){
        this.lifecycle = value;
    }

    public Integer getLifecycle(){
        return this.lifecycle;
    }

    public void setCreateBy(String value){
        this.createBy = value;
    }

    public String getCreateBy(){
        return this.createBy;
    }

    public void setModifyBy(String value){
        this.modifyBy = value;
    }

    public String getModifyBy(){
        return this.modifyBy;
    }

    public void setModifyDate(java.util.Date value){
        this.modifyDate = value;
    }

    public java.util.Date getModifyDate(){
        return this.modifyDate;
    }
}
