package com.galaxy.safe.utils;

/**
 * @author Administrator
 *         全局常量
 */
public class Constants {

    //    public static int DX = 104;//矩形定框的x坐标
    public static int DX = 80 + 56;//矩形定框的x坐标
    public static int DY = 152 - 13;//矩形定框的y坐标
    public static int W = 336;//宽
    public static int H = 166;//高
    public static final String SERVICE_URL = "http://121.41.38.46:9998";
    public static final String SERVICER_URL = "http://120.237.110.182:8080";
    public static final String UPDATE_URL = SERVICER_URL + "/update.json";//软件更新
    public static final String TASK_URL = SERVICER_URL + "/sql_server/servlet/TasknServlet";//最新任务
    public static final String ISOK_URL = SERVICER_URL + "/sql_server/servlet/UpdateTaskServlet";//完成任务+1
    public static final String DEVICE_URL = SERVICER_URL + "/sql_server/servlet/InsertDeviceServlet";//增加设备
    public static final String WORKING_URL = SERVICER_URL + "/sql_server/servlet/DeviceStateServlet";//工作状态
    public static final String OFF_URL = SERVICER_URL + "/sql_server/servlet/OffStateServlet";//关机状态
    public static final String INSERT_RECORD = SERVICER_URL + "/sql_server/servlet/RecordServlet";//上传记录
    public static final String NEWS = SERVICER_URL + "/sql_server/servlet/GetNewServlet";//消息列表


}
