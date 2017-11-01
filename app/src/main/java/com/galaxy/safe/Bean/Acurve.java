package com.galaxy.safe.Bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Dell on 2016/12/9.
 */
public class Acurve {


    /**
     * card_batch : {"0":"card_batch001","1":"1","2":"2016-12-05 00:00:00","3":"1","4":"2016-12-13 00:00:00","5":"0","6":"0"}
     * card_type : {"0":"测试卡","1":"2","2":"50","3":"50","4":"55","5":"0","6":"0","7":"0","8":"0"}
     * curve : {"0":"1","1":"1","10":"0","11":"1","12":"100","13":"1","14":"0","15":"20","16":"10","17":"5","18":"5","2":"1","3":"2","4":"1","5":"阴性","6":"疑似阳性","7":"阳性","8":"10","9":"5"}
     * point : [{"0":"1","1":"20000","2":"20"},{"0":"1","1":"30000","2":"30"},{"0":"1","1":"40000","2":"40"}]
     * type : curve
     */

    private CardBatchBean card_batch;
    private CardTypeBean card_type;
    private CurveBean curve;
    private String type;
    private List<PointBean> point;

    public CardBatchBean getCard_batch() {
        return card_batch;
    }

    public void setCard_batch(CardBatchBean card_batch) {
        this.card_batch = card_batch;
    }

    public CardTypeBean getCard_type() {
        return card_type;
    }

    public void setCard_type(CardTypeBean card_type) {
        this.card_type = card_type;
    }

    public CurveBean getCurve() {
        return curve;
    }

    public void setCurve(CurveBean curve) {
        this.curve = curve;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<PointBean> getPoint() {
        return point;
    }

    public void setPoint(List<PointBean> point) {
        this.point = point;
    }

    public static class CardBatchBean {
        /**
         * 0 : card_batch001
         * 1 : 1
         * 2 : 2016-12-05 00:00:00
         * 3 : 1
         * 4 : 2016-12-13 00:00:00
         * 5 : 0
         * 6 : 0
         */

        @SerializedName("0")
        private String value0;
        @SerializedName("1")
        private String value1;
        @SerializedName("2")
        private String value2;
        @SerializedName("3")
        private String value3;
        @SerializedName("4")
        private String value4;
        @SerializedName("5")
        private String value5;
        @SerializedName("6")
        private String value6;

        public String getValue0() {
            return value0;
        }

        public void setValue0(String value0) {
            this.value0 = value0;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

        public String getValue5() {
            return value5;
        }

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public String getValue6() {
            return value6;
        }

        public void setValue6(String value6) {
            this.value6 = value6;
        }
    }

    public static class CardTypeBean {
        /**
         * 0 : 测试卡
         * 1 : 2
         * 2 : 50
         * 3 : 50
         * 4 : 55
         * 5 : 0
         * 6 : 0
         * 7 : 0
         * 8 : 0
         */

        @SerializedName("0")
        private String value0;
        @SerializedName("1")
        private String value1;
        @SerializedName("2")
        private String value2;
        @SerializedName("3")
        private String value3;
        @SerializedName("4")
        private String value4;
        @SerializedName("5")
        private String value5;
        @SerializedName("6")
        private String value6;
        @SerializedName("7")
        private String value7;
        @SerializedName("8")
        private String value8;

        public String getValue0() {
            return value0;
        }

        public void setValue0(String value0) {
            this.value0 = value0;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

        public String getValue5() {
            return value5;
        }

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public String getValue6() {
            return value6;
        }

        public void setValue6(String value6) {
            this.value6 = value6;
        }

        public String getValue7() {
            return value7;
        }

        public void setValue7(String value7) {
            this.value7 = value7;
        }

        public String getValue8() {
            return value8;
        }

        public void setValue8(String value8) {
            this.value8 = value8;
        }
    }

    public static class CurveBean {
        /**
         * 0 : 1
         * 1 : 1
         * 10 : 0
         * 11 : 1
         * 12 : 100
         * 13 : 1
         * 14 : 0
         * 15 : 20
         * 16 : 10
         * 17 : 5
         * 18 : 5
         * 2 : 1
         * 3 : 2
         * 4 : 1
         * 5 : 阴性
         * 6 : 疑似阳性
         * 7 : 阳性
         * 8 : 10
         * 9 : 5
         */

        @SerializedName("0")
        private String value0;
        @SerializedName("1")
        private String value1;
        @SerializedName("10")
        private String value10;
        @SerializedName("11")
        private String value11;
        @SerializedName("12")
        private String value12;
        @SerializedName("13")
        private String value13;
        @SerializedName("14")
        private String value14;
        @SerializedName("15")
        private String value15;
        @SerializedName("16")
        private String value16;
        @SerializedName("17")
        private String value17;
        @SerializedName("18")
        private String value18;
        @SerializedName("2")
        private String value2;
        @SerializedName("3")
        private String value3;
        @SerializedName("4")
        private String value4;
        @SerializedName("5")
        private String value5;
        @SerializedName("6")
        private String value6;
        @SerializedName("7")
        private String value7;
        @SerializedName("8")
        private String value8;
        @SerializedName("9")
        private String value9;

        public String getValue0() {
            return value0;
        }

        public void setValue0(String value0) {
            this.value0 = value0;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue10() {
            return value10;
        }

        public void setValue10(String value10) {
            this.value10 = value10;
        }

        public String getValue11() {
            return value11;
        }

        public void setValue11(String value11) {
            this.value11 = value11;
        }

        public String getValue12() {
            return value12;
        }

        public void setValue12(String value12) {
            this.value12 = value12;
        }

        public String getValue13() {
            return value13;
        }

        public void setValue13(String value13) {
            this.value13 = value13;
        }

        public String getValue14() {
            return value14;
        }

        public void setValue14(String value14) {
            this.value14 = value14;
        }

        public String getValue15() {
            return value15;
        }

        public void setValue15(String value15) {
            this.value15 = value15;
        }

        public String getValue16() {
            return value16;
        }

        public void setValue16(String value16) {
            this.value16 = value16;
        }

        public String getValue17() {
            return value17;
        }

        public void setValue17(String value17) {
            this.value17 = value17;
        }

        public String getValue18() {
            return value18;
        }

        public void setValue18(String value18) {
            this.value18 = value18;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public String getValue3() {
            return value3;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public String getValue4() {
            return value4;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

        public String getValue5() {
            return value5;
        }

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public String getValue6() {
            return value6;
        }

        public void setValue6(String value6) {
            this.value6 = value6;
        }

        public String getValue7() {
            return value7;
        }

        public void setValue7(String value7) {
            this.value7 = value7;
        }

        public String getValue8() {
            return value8;
        }

        public void setValue8(String value8) {
            this.value8 = value8;
        }

        public String getValue9() {
            return value9;
        }

        public void setValue9(String value9) {
            this.value9 = value9;
        }
    }

    public static class PointBean {
        /**
         * 0 : 1
         * 1 : 20000
         * 2 : 20
         */

        @SerializedName("0")
        private String value0;
        @SerializedName("1")
        private String value1;
        @SerializedName("2")
        private String value2;

        public String getValue0() {
            return value0;
        }

        public void setValue0(String value0) {
            this.value0 = value0;
        }

        public String getValue1() {
            return value1;
        }

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public String getValue2() {
            return value2;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }
    }
}
