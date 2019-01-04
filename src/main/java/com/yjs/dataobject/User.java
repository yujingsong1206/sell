package com.yjs.dataobject;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 用户表
 */
@Entity
@Data
@DynamicUpdate
public class User {

    @Id
    private String userId;
    private String username;
    private String password;
    private Date createTime;
    private Date updateTime;

}
