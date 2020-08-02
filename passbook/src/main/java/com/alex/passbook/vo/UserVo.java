package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVo {

    /** 用户id **/
    private Long id;

    /** 用户基本信息（HBase的'b'列族） **/
    private BasicInfo basicInfo;

    /** 用户额外信息（HBase的'o'列族） **/
    private OtherInfo otherInfo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicInfo {
        private String name;
        private Integer age;
        private String sex;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OtherInfo {
        private String phone;
        private String address;
    }
}
