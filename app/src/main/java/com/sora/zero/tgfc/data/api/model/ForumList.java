package com.sora.zero.tgfc.data.api.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zsy on 3/23/17.
 */

public class ForumList {
    public static Forum[] datas = new Forum[]{
            new Forum(10, "完全数码讨论区", 2),
            new Forum(85,"Apple 专区"),
            new Forum(5,"手机和掌机游戏讨论区"),
            new Forum(97,"旅行度假"),
            new Forum(99,"舌尖上的TG"),
            new Forum(41,"摄影区"),
            new Forum(69,"Sony俱乐部"),
            new Forum(59,"汽车版", 4),
            new Forum(33, "游戏业界综合讨论区", 3),
            new Forum(29,"主机游戏讨论区"),
            new Forum(101,"经典游戏怀旧专区"),
            new Forum(45,"硬件维修区"),
            new Forum(67,"网游业界讨论区"),
            new Forum(98,"英雄联盟"),
            new Forum(40,"暴雪游戏专区"),
            new Forum(94,"龙之谷"),
            new Forum(81,"AION永恒之塔"),
            new Forum(20,"梦幻之星专区"),
            new Forum(65,"ChinaJoy 2013"),
            new Forum(25, "灌水与情感", 1),
            new Forum(72,"神鬼奇谈"),
            new Forum(6,"体育运动专区"),
            new Forum(48,"体育赛事直喷专区"),
            new Forum(95,"宠物乐园"),
            new Forum(12, "影视专区", 5),
            new Forum(11,"动漫模型"),
            new Forum(93,"文史春秋"),
            new Forum(86,"TGFC招聘求职"),
            new Forum(47,"资源分享区"),
            new Forum(42,"三国志大战专区"),
            new Forum(54,"无双专区"),
            new Forum(88,"写真摄影服务"),
            new Forum(90,"二手交易区", 6),
            new Forum(17,"新品贩卖区"),
            new Forum(63,"交易调解区"),
            new Forum(26,"公共工作区"),
            new Forum(35,"已处理版务"),
    };

    private static List<Forum> forumList = null;

    @NonNull
    public static List<Forum> getForumList(){
        if (forumList == null) {
            forumList = Arrays.asList(datas);
            Collections.sort(forumList);
        }
        return forumList;
    }

    @Nullable
    public static Forum getForumByFid(@NonNull int fid) {
        List<Forum> list = getForumList();
        for(int i = 0; i < list.size(); i++){
            if (list.get(i).fid == fid){
                return list.get(i);
            }
        }
        return null;
    }

    @NonNull
    public static List<Forum> getPinnedForumList(){
        List<Forum> list = new ArrayList<Forum>();
        list.add(getForumByFid(10));
        list.add(getForumByFid(33));
        list.add(getForumByFid(25));
        return list;
    }


    @NonNull
    public static Forum getDefaultForum() {
        return getForumByFid(10);
    }

}
