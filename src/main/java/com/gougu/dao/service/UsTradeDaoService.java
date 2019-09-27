package com.gougu.dao.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gougu.dao.mapper.UsTradeMapper;
import com.gougu.dao.model.UsTrade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsTradeDaoService {

    @Autowired
    private UsTradeMapper usTradeMapper;

    public List<UsTrade> find(String start, String end) {
        EntityWrapper<UsTrade> ew = new EntityWrapper<>();
        ew.between("d1", start, end);
        return usTradeMapper.selectList(ew);
    }
}
