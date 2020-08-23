package com.alex.passbook.constant;

/**
 * @author wsh
 * @date 2020-06-08
 * <h1>常量定义</h1>
 */
public class Constants {

    //商户优惠券投放的 kafka Topic
    public static final String TEMPLATE_TOPIC = "merchants-template";

    //token文件存储目录
    public static final String TOKEN_DIR = "/tmp/token/";

    //已使用的token文件后缀
    public static final String USED_TOKEN_SUFFIX = "_";

    //用户数的redis key
    public static final String USE_COUNT_REDIS_KEY = "user-count";

    // HBase User相关常量
    public static byte[] USER_FAMILY_B = Constants.UserTable.FAMILY_B.getBytes();
    public static byte[] USER_NAME = Constants.UserTable.NAME.getBytes();
    public static byte[] USER_AGE = Constants.UserTable.AGE.getBytes();
    public static byte[] USER_SEX = Constants.UserTable.SEX.getBytes();
    public static byte[] USER_FAMILY_O = Constants.UserTable.FAMILY_O.getBytes();
    public static byte[] USER_PHONE = Constants.UserTable.PHONE.getBytes();
    public static byte[] USER_ADDRESS = Constants.UserTable.ADDRESS.getBytes();

    //HBase PassTemplate相关常量
    public static byte[] PT_FAMILY_B = Constants.PassTemplateTable.FAMILY_B.getBytes();
    public static byte[] PT_ID = Constants.PassTemplateTable.ID.getBytes();
    public static byte[] PT_TITLE = Constants.PassTemplateTable.TITLE.getBytes();
    public static byte[] PT_SUMMARY = Constants.PassTemplateTable.SUMMARY.getBytes();
    public static byte[] PT_DESC = Constants.PassTemplateTable.DESC.getBytes();
    public static byte[] PT_HAS_TOKEN = Constants.PassTemplateTable.HAS_TOKEN.getBytes();
    public static byte[] PT_BACKGROUND = Constants.PassTemplateTable.BACKGROUND.getBytes();
    public static byte[] PT_FAMILY_C = Constants.PassTemplateTable.FAMILY_C.getBytes();
    public static byte[] PT_LIMIT = Constants.PassTemplateTable.LIMIT.getBytes();
    public static byte[] PT_START = Constants.PassTemplateTable.START.getBytes();
    public static byte[] PT_END = Constants.PassTemplateTable.END.getBytes();

    //HBase Pass相关常量
    public static byte[] P_FAMILY_I = Constants.PassTable.FAMILY_I.getBytes();
    public static byte[] P_USER_ID = Constants.PassTable.USER_ID.getBytes();
    public static byte[] P_TEMPLATE_ID = Constants.PassTable.TEMPLATE_ID.getBytes();
    public static byte[] P_TOKEN = Constants.PassTable.TOKEN.getBytes();
    public static byte[] P_ASSIGNED_DATE = Constants.PassTable.ASSIGNED_DATE.getBytes();
    public static byte[] P_CON_DATE = Constants.PassTable.CON_DATE.getBytes();

    //HBase Feedback相关常量
    public static byte[] F_FAMILY_I = Constants.FeedbackTable.FAMILY_I.getBytes();
    public static byte[] F_USER_ID = Constants.FeedbackTable.USER_ID.getBytes();
    public static byte[] F_TEMPLATE_ID = Constants.FeedbackTable.TEMPLATE_ID.getBytes();
    public static byte[] F_TYPE = Constants.FeedbackTable.TYPE.getBytes();
    public static byte[] F_COMMENT = Constants.FeedbackTable.COMMENT.getBytes();


    /**
     * <h2>User HBase Table</h2>
     */
    public class UserTable{
        //User HBase的表名
        public static final String TABLE_NAME = "pb:user";

        //基本信息列族
        public static final String FAMILY_B = "b";

        //姓名
        public static final String NAME = "name";

        //年龄
        public static final String AGE = "age";

        //性别
        public static final String SEX = "sex";

        //额外信息列族
        public static final String FAMILY_O = "o";

        //手机
        public static final String PHONE = "phone";

        //地址
        public static final String ADDRESS = "address";
    }

    /**
     * <h2>PassTemplate HBase Table</h2>
     */
    public class PassTemplateTable{
        //PassTemplate HBase表名
        public static final String TABLE_NAME = "pb:passtemplate";

        //基本信息列族
        public static final String FAMILY_B = "b";

        //商户id
        public static final String ID = "id";

        //优惠券标题
        public static final String TITLE = "title";

        //优惠券摘要
        public static final String SUMMARY = "summary";

        //优惠券的详细信息
        public static final String DESC = "desc";

        //优惠券是否有token
        public static final String HAS_TOKEN = "has_token";

        //优惠券背景色
        public static final String BACKGROUND = "background";

        //约束信息列族
        public static final String FAMILY_C = "c";

        //最大个数限制
        public static final String LIMIT = "limit";

        //优惠券开始时间
        public static final String START = "start";

        //优惠券结束时间
        public static final String END = "end";
    }

    /**
     * <h2>Pass HBase Table</h2>
     */
    public class PassTable {

        //Pass HBase 表名
        public static final String TABLE_NAME = "pb:pass";

        //信息列族
        public static final String FAMILY_I = "i";

        //用户id
        public static final String USER_ID = "user_id";

        //优惠券id
        public static final String TEMPLATE_ID = "template_id";

        //优惠券识别码
        public static final String TOKEN = "token";

        //领取日期
        public static final String ASSIGNED_DATE = "assigned_date";

        //消费日期
        public static final String CON_DATE = "con_date";
    }

    /**
     * <h2>Feedback HBase Table</h2>
     */
    public class FeedbackTable {

        //Feedback HBase 表名
        public static final String TABLE_NAME = "pb:feedback";

        //信息列族
        public static final String FAMILY_I = "i";

        //用户id
        public static final String USER_ID = "user_id";

        //评论类型
        public static final String TYPE = "type";

        //PassTemplate RowKey；如果是app评论则是-1
        public static final String TEMPLATE_ID = "template_id";

        //评论内容
        public static final String COMMENT = "comment";
    }



}
