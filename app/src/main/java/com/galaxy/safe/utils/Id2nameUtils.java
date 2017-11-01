package com.galaxy.safe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Dell on 2016/4/20.
 */
public class Id2nameUtils {

    static Cursor cursor2;

    public static String sampletype2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_sample_type", new String[]{"type_id", "type_name"}, "type_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String type_name = cursor2.getString(1);
                cursor2.close();
                return type_name;
            }

        }
        return null;
    }

    public static String sampletype2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_sample_type", new String[]{"type_id", "type_name"}, "type_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String type_id = cursor2.getString(0);
                cursor2.close();
                return type_id;
            }
        }
        return null;
    }

    public static String u2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_unit", new String[]{"limited_unit_id", "limited_unit"}, "limited_unit_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String limited_unit = cursor2.getString(1);
                cursor2.close();
                return limited_unit;
            }

        }
        return null;
    }

    /**
     * Id转名称
     *
     * @param s
     * @return
     */
    public static String sample2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_sample_typedetail", new String[]{"sample_id", "sample_name"}, "sample_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String sample_name = cursor2.getString(1);
                cursor2.close();
                return sample_name;
            }
        }
        return null;
    }

    public static String sample2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_sample_typedetail", new String[]{"sample_id", "sample_name"}, "sample_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String sample_id = cursor2.getString(0);
                cursor2.close();
                return sample_id;
            }
        }
        return null;
    }

    public static String item2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_item_id = cursor2.getString(0);
                cursor2.close();
                return detection_item_id;
            }
        }
        return null;
    }

    public static String item2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_item", new String[]{"detection_item_id", "detection_item_name"}, "detection_item_id=?", new String[]{s}, null, null, null);
            try {
                while (cursor2.moveToNext()) {
                    String detection_item_name = cursor2.getString(1);
                    return detection_item_name;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            cursor2.close();
        }
        return null;
    }

    public static String cardBatch2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_card_batch", new String[]{"card_batch_id", "card_batch"}, "card_batch_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_batch = cursor2.getString(1);
                cursor2.close();
                return card_batch;
            }
        }
        return null;
    }

    public static String cardBatch2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_card_batch", new String[]{"card_batch_id", "card_batch"}, "card_batch=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_batch_id = cursor2.getString(0);
                cursor2.close();
                return card_batch_id;
            }
        }
        return null;
    }

    /**
     * 由ID转换为名称
     *
     * @param company
     * @return
     */
    public static String company2name(String company, SQLiteDatabase sdb) {
        if (company != null) {
            cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_id"}, "company_id=?", new String[]{company}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_name = cursor2.getString(0);
                cursor2.close();
                return company_name;
            }

        }
        return null;
    }

    public static String company2id(String company, SQLiteDatabase sdb) {
        if (company != null) {
            cursor2 = sdb.query("galaxy_detection_company", new String[]{"company_name", "company_id"}, "company_name=?", new String[]{company}, null, null, null);
            while (cursor2.moveToNext()) {
                String company_id = cursor2.getString(1);
                cursor2.close();
                return company_id;
            }

        }
        return null;
    }

    public static String person2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_person", new String[]{"person_id", "person_name"}, "person_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String person_id = cursor2.getString(0);
                cursor2.close();
                return person_id;
            }

        }
        return null;

    }

    public static String user2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_person", new String[]{"person_id", "user_name"}, "user_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String person_id = cursor2.getString(0);
                cursor2.close();
                return person_id;
            }

        }
        return null;

    }

    public static String person2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_person", new String[]{"person_id", "person_name"}, "person_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String person_name = cursor2.getString(1);
                cursor2.close();
                return person_name;
            }

        }
        return null;

    }

    public static String provience2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_province", new String[]{"province_name", "province_id"}, "province_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String province_name = cursor2.getString(0);
                cursor2.close();
                return province_name;
            }
        }
        return null;
    }

    public static String provience2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_province", new String[]{"province_name", "province_id"}, "province_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String province_id = cursor2.getString(1);
                cursor2.close();
                return province_id;
            }

        }
        return null;
    }

    public static String city2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_city", new String[]{"city_name", "city_id"}, "city_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String city_id = cursor2.getString(1);
                cursor2.close();
                return city_id;
            }

        }
        return null;
    }

    public static String city2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_city", new String[]{"city_name", "city_id"}, "city_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String city_name = cursor2.getString(0);
                cursor2.close();
                return city_name;
            }

        }
        return null;
    }

    public static String county2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_county", new String[]{"county_name", "county_id"}, "county_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String county_name = cursor2.getString(0);
                cursor2.close();
                return county_name;
            }

        }
        return null;
    }

    public static String county2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_county", new String[]{"county_name", "county_id"}, "county_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String county_id = cursor2.getString(1);
                cursor2.close();
                return county_id;
            }

        }
        return null;
    }

    public static String town2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_town", new String[]{"town_name", "town_id"}, "town_name=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String town_id = cursor2.getString(1);
                cursor2.close();
                return town_id;
            }

        }
        return null;
    }

    public static String town2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_position_town", new String[]{"town_name", "town_id"}, "town_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String town_name = cursor2.getString(0);
                cursor2.close();
                return town_name;
            }
        }
        return null;
    }

    public static String part2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_part", new String[]{"detection_part_id", "detection_part"}, "detection_part=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_part_id = cursor2.getString(0);
                cursor2.close();
                return detection_part_id;
            }
        }
        return null;
    }

    public static String part2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_part", new String[]{"detection_part_id", "detection_part"}, "detection_part_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_part = cursor2.getString(1);
                cursor2.close();
                return detection_part;
            }

        }
        return null;
    }

    public static String con2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_conclusion", new String[]{"detection_conclusion_id", "detection_conclusion"}, "detection_conclusion_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_conclusion = cursor2.getString(1);
                cursor2.close();
                return detection_conclusion;
            }

        }
        return null;
    }

    public static String con2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_detection_conclusion", new String[]{"detection_conclusion_id", "detection_conclusion"}, "detection_conclusion=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String detection_conclusion_id = cursor2.getString(0);
                cursor2.close();
                return detection_conclusion_id;
            }

        }
        return null;
    }

    public static String type2name(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_card_type", new String[]{"card_type_id", "card_type"}, "card_type_id=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_type = cursor2.getString(1);
                cursor2.close();
                return card_type;
            }
            cursor2.close();
        }

        return null;
    }

    public static String type2id(String s, SQLiteDatabase sdb) {
        if (s != null) {
            cursor2 = sdb.query("galaxy_card_type", new String[]{"card_type_id", "card_type"}, "card_type=?", new String[]{s}, null, null, null);
            while (cursor2.moveToNext()) {
                String card_type_id = cursor2.getString(0);
                cursor2.close();
                return card_type_id;
            }
            cursor2.close();
        }

        return null;
    }
}
