package com.alex.passbook.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>商户对象模型</h1>
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "merchants")
public class Merchants {

    @Id
    @NotNull
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Basic
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    //商户logo
    @Basic
    @Column(name = "logo_url", nullable = false)
    private String logUrl;

    //商户营业执照
    @Basic
    @Column(name = "business_license_url", nullable = false)
    private String buinessLicenseUrl;

    @Basic
    @Column(name = "phone", nullable = false)
    private String phone;

    @Basic
    @Column(name = "address", nullable = false)
    private String address;

    @Basic
    @Column(name = "is_audit", nullable = false)
    private Boolean isAudit;
}
