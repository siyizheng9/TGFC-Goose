package com.sora.zero.tgfc.data.api.model;

import java.io.Serializable;

/**
 * Created by zsy on 3/23/17.
 */

public class Forum implements Comparable<Forum>, Serializable{

    static int counter = 0;

    public int fid;
    public String name;
    public int order;

    public Forum(int fid, String name) {
        this.fid = fid;
        this.name = name;
        counter += 100;
        this.order = counter;
    }

    public Forum(int fid, String name, int order) {
        this.fid = fid;
        this.name = name;
        this.order = order;
    }

    @Override
    public int compareTo(Forum forum) {
        return this.order - forum.order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Forum forum = (Forum) o;
        return this.fid == forum.fid;

    }

    @Override
    public int hashCode(){
        return this.fid;
    }
}
