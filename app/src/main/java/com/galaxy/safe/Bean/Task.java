package com.galaxy.safe.Bean;

import java.util.List;

/**
 * Created by Dell on 2016/6/3.
 */
public class Task {


    /**
     * state : 1
     * datas : [{"task_id":"1","task_bh":"201312022222222222 ","detection_item":"三聚氰胺","detection_sample_type":"奶制品","detection_sample_name":"鲜牛奶","batch_num":"50","completed_num":"0","publish_date":"2017-01-09","publisher":"上级监管部门镇府部门","deadline":"2017-01-24"}]
     * page : 0
     * count : 0
     * size : 0
     */

    private int state;
    private int page;
    private int count;
    private int size;
    private List<DatasBean> datas;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<DatasBean> getDatas() {
        return datas;
    }

    public void setDatas(List<DatasBean> datas) {
        this.datas = datas;
    }

    public static class DatasBean {
        /**
         * task_id : 1
         * task_bh : 201312022222222222
         * detection_item : 三聚氰胺
         * detection_sample_type : 奶制品
         * detection_sample_name : 鲜牛奶
         * batch_num : 50
         * completed_num : 0
         * publish_date : 2017-01-09
         * publisher : 上级监管部门镇府部门
         * deadline : 2017-01-24
         */

        private String task_id;
        private String task_bh;
        private String detection_item;
        private String detection_sample_type;
        private String detection_sample_name;
        private String batch_num;
        private String completed_num;
        private String publish_date;
        private String publisher;
        private String deadline;

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTask_bh() {
            return task_bh;
        }

        public void setTask_bh(String task_bh) {
            this.task_bh = task_bh;
        }

        public String getDetection_item() {
            return detection_item;
        }

        public void setDetection_item(String detection_item) {
            this.detection_item = detection_item;
        }

        public String getDetection_sample_type() {
            return detection_sample_type;
        }

        public void setDetection_sample_type(String detection_sample_type) {
            this.detection_sample_type = detection_sample_type;
        }

        public String getDetection_sample_name() {
            return detection_sample_name;
        }

        public void setDetection_sample_name(String detection_sample_name) {
            this.detection_sample_name = detection_sample_name;
        }

        public String getBatch_num() {
            return batch_num;
        }

        public void setBatch_num(String batch_num) {
            this.batch_num = batch_num;
        }

        public String getCompleted_num() {
            return completed_num;
        }

        public void setCompleted_num(String completed_num) {
            this.completed_num = completed_num;
        }

        public String getPublish_date() {
            return publish_date;
        }

        public void setPublish_date(String publish_date) {
            this.publish_date = publish_date;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getDeadline() {
            return deadline;
        }

        public void setDeadline(String deadline) {
            this.deadline = deadline;
        }
    }
}



