package com.zihai.h2Client.dao.ds2;

import com.zihai.h2Client.core.entity.Oplog;

public interface OplogMapper {
    int deleteByPrimaryKey(Integer opId);

    int insert(Oplog record);

    int insertSelective(Oplog record);

    Oplog selectByPrimaryKey(Integer opId);

    int updateByPrimaryKeySelective(Oplog record);

    int updateByPrimaryKey(Oplog record);
}