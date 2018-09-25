package com.cdtc.birthday.network;

/**
 * Created by Sweven on 2018/9/25.
 * Email:sweventears@Foxmail.com
 */
public class Api {

    /**
     * 服务器地址
     */
    private static final String HOME="http://47.106.85.0:10009";

    /**
     * 注册
     */
    public static final String REGISTER=HOME+"/user/registry";

    /**
     * 登录
     */
    public static final String LOGIN=HOME+"/user/login";

    /**
     * 生日
     */
    private static final String BIRTH=HOME+"/birth";

    /**
     * 删除生日
     */
    public static final String BIRTH_DELETE=BIRTH+"/delete";

    /**
     * 获取生日列表
     */
    public static final String BIRTH_LIST=BIRTH+"/list";

    /**
     * 保存生日
     */
    public static final String BIRTH_SAVE=BIRTH+"/save";

    /**
     * 更新生日信息
     */
    public static final String BIRTH_UPDATE=BIRTH+"/update";

}
