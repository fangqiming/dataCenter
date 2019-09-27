package com.gougu.dao.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.gougu.dao.mapper.*;
import com.gougu.dao.model.*;
import com.gougu.entity.vo.UsStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Performance {

    @Autowired
    private AieqMapper aieqMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private AssetUsMapper assetUsMapper;

    @Autowired
    private IndexUsMapper indexUsMapper;

    @Autowired
    private IndexValueMapper indexValueMapper;

    @Autowired
    private AiMapper aiMapper;

    public UsStock find3(LocalDate start, LocalDate end) {
        EntityWrapper<Asset> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<Asset> assets = assetMapper.selectList(ew);

        EntityWrapper<IndexValue> ew2 = new EntityWrapper<>();
        ew.between("date", start, end);
        List<IndexValue> indexValues = indexValueMapper.selectList(ew2);

        indexValues.sort(Comparator.comparing(IndexValue::getDate));
        assets.sort(Comparator.comparing(Asset::getDate));
        List<IndexValue> weekIndex = new ArrayList<>();
        List<Asset> weekAsset = new ArrayList<>();
        for (int i = 0; i < indexValues.size() - 1; i++) {
            //加入初始值
            if (i == 0) {
                weekIndex.add(indexValues.get(i));
            }
            //加入结束值
            if (i + 2 == indexValues.size()) {
                weekIndex.add(indexValues.get(i + 1));
            }
            LocalDate s1 = indexValues.get(i).getDate();
            LocalDate e1 = indexValues.get(i + 1).getDate();
            if (s1.plusDays(1L).compareTo(e1) < 0) {
                weekIndex.add(indexValues.get(i));
            }
        }

        for (int i = 0; i < assets.size() - 1; i++) {
            //加入初始值
            if (i == 0) {
                weekAsset.add(assets.get(i));
            }
            //加入结束值
            if (i + 2 == assets.size()) {
                weekAsset.add(assets.get(i + 1));
            }
            LocalDate s1 = assets.get(i).getDate();
            LocalDate e1 = assets.get(i + 1).getDate();
            if (s1.plusDays(1L).compareTo(e1) < 0) {
                weekAsset.add(assets.get(i));
            }
        }

        List<String> date = new ArrayList<>();
        List<BigDecimal> sp500 = new ArrayList<>();
        List<BigDecimal> gougu = new ArrayList<>();
        IndexValue baseIndex = weekIndex.get(0);
        BigDecimal initAsset = weekAsset.get(0).getBalance().add(weekAsset.get(0).getStock()).add(weekAsset.get(0).getCover());
        for (int i = 0; i < weekIndex.size(); i++) {
            IndexValue indexValue = weekIndex.get(i);
            String time = indexValue.getDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            date.add(time);
            BigDecimal sh = (indexValue.getSh().subtract(baseIndex.getSh())).divide(baseIndex.getSh(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            sp500.add(sh);
            BigDecimal asset = weekAsset.get(i).getBalance().add(weekAsset.get(i).getCover()).add(weekAsset.get(i).getStock());
            gougu.add((asset.subtract(initAsset)).divide(initAsset, 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)));
        }
        System.out.println("上证指数最大回撤:"+calcMaxDd(weekIndex.stream().map(a->a.getSh()).collect(Collectors.toList())));
        System.out.println("勾股指数最大回撤:"+calcMaxDd(weekAsset.stream().map(a->a.getBalance().add(a.getStock()).add(a.getCover())).collect(Collectors.toList())));
        return UsStock.builder().sp500(sp500).gougu(gougu).date(date).build();

    }


    public UsStock find2(LocalDate start, LocalDate end) {
        EntityWrapper<Ai> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<Ai> ais = aiMapper.selectList(ew);
        ais.sort(Comparator.comparing(Ai::getDate));
        Ai base = ais.get(0);
        List<String> date = new ArrayList<>();
        List<BigDecimal> sp500 = new ArrayList<>();
        List<BigDecimal> gougu = new ArrayList<>();
        for (int i = 0; i < ais.size(); i++) {
            Ai ai = ais.get(i);
            String time = ai.getDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            date.add(time);
            BigDecimal sp = (ai.getSz().subtract(base.getSz())).divide(base.getSz(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            sp500.add(sp);
            BigDecimal gg = (ai.getGougu().subtract(base.getGougu())).divide(base.getGougu(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            gougu.add(gg);
        }
        return UsStock.builder().sp500(sp500).gougu(gougu).date(date).build();
    }


    public UsStock find(LocalDate start, LocalDate end) {
        List<Aieq> aieqs = findAieq(start, end);
        aieqs.sort(Comparator.comparing(Aieq::getDate));
        List<AssetUs> assetUs = findAssetUs(start, end);
        assetUs.sort(Comparator.comparing(AssetUs::getDate));
        List<IndexUs> indexUs = findIndexUs(start, end);
        indexUs.sort(Comparator.comparing(IndexUs::getDate));
        List<String> date = new ArrayList<>();
        List<BigDecimal> sp500 = new ArrayList<>();
        List<BigDecimal> gougu = new ArrayList<>();
        List<BigDecimal> aieq = new ArrayList<>();

        Aieq baseAieq = aieqs.get(0);
        AssetUs baseAsset = assetUs.get(0);
        IndexUs baseIndexUs = indexUs.get(0);

        for (int i = 0; i < aieqs.size(); i++) {
            String time = aieqs.get(i).getDate().format(DateTimeFormatter.ofPattern("yy-MM-dd"));
            date.add(time);
            BigDecimal sp = indexUs.get(i).getSp500().subtract(baseIndexUs.getSp500()).divide(baseIndexUs.getSp500(), 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100));
            sp500.add(sp);
            AssetUs asset = assetUs.get(i);
            BigDecimal base = baseAsset.getBalance().add(baseAsset.getCover()).add(baseAsset.getStock());
            BigDecimal nowAsset = asset.getBalance().add(asset.getCover()).add(asset.getStock());
            gougu.add((nowAsset.subtract(base)).divide(base, 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)));
            aieq.add((aieqs.get(i).getClose().subtract(baseAieq.getClose())).divide(baseAieq.getClose(), 4, BigDecimal.ROUND_UP).multiply(new BigDecimal(100)));
        }
        return UsStock.builder().aieq(aieq).date(date).gougu(gougu).sp500(sp500).build();
    }

    /**
     * 上证指数回撤
     *
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calSzMaxDd(LocalDate start, LocalDate end) {
        EntityWrapper<IndexValue> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<IndexValue> indexUses = indexValueMapper.selectList(ew);
        indexUses.sort(Comparator.comparing(IndexValue::getDate));
        List<BigDecimal> collect = indexUses.stream().map(a -> a.getSh()).collect(Collectors.toList());
        return calcMaxDd(collect);
    }

    /**
     * A股账户最大回撤
     *
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calAMaxDd(LocalDate start, LocalDate end) {
        EntityWrapper<Asset> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<Asset> assets = assetMapper.selectList(ew);
        assets.sort(Comparator.comparing(Asset::getDate));
        List<BigDecimal> collect = assets.stream().map(a -> a.getBalance().add(a.getCover()).add(a.getStock())).collect(Collectors.toList());
        return calcMaxDd(collect);
    }

    /**
     * 美股账户最大回撤
     *
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calUsMaxDd(LocalDate start, LocalDate end) {
        EntityWrapper<AssetUs> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<AssetUs> assets = assetUsMapper.selectList(ew);
        assets.sort(Comparator.comparing(AssetUs::getDate));
        List<BigDecimal> collect = assets.stream().map(a -> a.getBalance().add(a.getCover()).add(a.getStock())).collect(Collectors.toList());
        return calcMaxDd(collect);
    }

    /**
     * 标普最大回撤
     *
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calSPMacDd(LocalDate start, LocalDate end) {
        EntityWrapper<IndexUs> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<IndexUs> indexUses = indexUsMapper.selectList(ew);
        indexUses.sort(Comparator.comparing(IndexUs::getDate));
        List<BigDecimal> collect = indexUses.stream().map(a -> a.getSp500()).collect(Collectors.toList());
        return calcMaxDd(collect);
    }

    /**
     * AIEQ最大回撤
     *
     * @param start
     * @param end
     * @return
     */
    public BigDecimal calAieqmaxDd(LocalDate start, LocalDate end) {
        EntityWrapper<Aieq> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        List<Aieq> aieqs = aieqMapper.selectList(ew);
        aieqs.sort(Comparator.comparing(Aieq::getDate));
        List<BigDecimal> collect = aieqs.stream().map(a -> a.getClose()).collect(Collectors.toList());
        return calcMaxDd(collect);
    }


    private BigDecimal calcMaxDd(List<BigDecimal> price) {
        BigDecimal max_unit_value = price.get(0);
        BigDecimal max_dd = BigDecimal.ZERO;
        BigDecimal dd;
        for (int i = 1; i < price.size(); i++) {
            max_unit_value = price.get(i).compareTo(max_unit_value) > 0 ? price.get(i) : max_unit_value;
            dd = price.get(i).divide(max_unit_value, 4, BigDecimal.ROUND_UP).subtract(new BigDecimal(1));
            max_dd = dd.compareTo(max_dd) < 0 ? dd : max_dd;
        }
        return max_dd.multiply(new BigDecimal(100));
    }


    private List<Aieq> findAieq(LocalDate start, LocalDate end) {
        EntityWrapper<Aieq> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        return aieqMapper.selectList(ew);
    }

    private List<AssetUs> findAssetUs(LocalDate start, LocalDate end) {
        EntityWrapper<AssetUs> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        return assetUsMapper.selectList(ew);
    }

    private List<IndexUs> findIndexUs(LocalDate start, LocalDate end) {
        EntityWrapper<IndexUs> ew = new EntityWrapper<>();
        ew.between("date", start, end);
        return indexUsMapper.selectList(ew);
    }


}
