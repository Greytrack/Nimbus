package com.cjc.nimbus.utils.rank;

import com.cjc.nimbus.utils.StringUtils;
import com.github.pravin.raha.lexorank4j.LexoRank;
import com.github.pravin.raha.lexorank4j.LexoRankBucket;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DefaultRank implements IRank, Comparable<IRank> {

    /**
     * subRank的长度是多少时，触发重排序！默认是大于9，每增加1位长度，最小可延缓5次触发重排序（非向后插入元素）
     *
     * rank的格式 ==> bucket|rank(6位36进制字符):subRank(n位36进制字符)
     *
     * 比如： 0|10000:
     */
    public static final int THRESHOLD_OF_SUBRANK = 9;

    private final LexoRank lexoRank;

    private DefaultRank(LexoRank lexoRank) {
        this.lexoRank = lexoRank;
    }

    public static IRank initialValue() {
        return toRank(LexoRank.min().genNext());
    }

    public static IRank parse(String rankStr) {
        return toRank(LexoRank.parse(rankStr));
    }

    public static String nextValue(String rankStr) {
        return toRank(LexoRank.parse(rankStr)).next().value();
    }

    public static String betweenValue(String rankStr1, String rankStr2) {
        return toRank(LexoRank.parse(rankStr1)).between(toRank(LexoRank.parse(rankStr2))).value();
    }

    private static IRank min(LexoRankBucket bucket) {
        return toRank(LexoRank.from(bucket, LexoRank.min().genNext().getDecimal()));
    }

    private static IRank max(LexoRankBucket bucket) {
        return toRank(LexoRank.from(bucket, LexoRank.max().genPrev().getDecimal()));
    }

    private static IRank toRank(LexoRank lexoRank) {
        return new DefaultRank(lexoRank);
    }

    @Override
    public IRank prev() {
        return toRank(lexoRank.genPrev());
    }

    @Override
    public IRank next() {
        return toRank(lexoRank.genNext());
    }

    @Override
    public IRank between(IRank otherRank) {
        return toRank(lexoRank.between(((DefaultRank)otherRank).lexoRank));
    }

    @Override
    public boolean needReorder() {
        return lexoRank.format().length() > THRESHOLD_OF_SUBRANK;
    }

    @Override
    public void reorder(RankReorder rankReorder) {
        LexoRankBucket currentBucket = this.lexoRank.getBucket();
        LexoRankBucket nextBucket = currentBucket.next();
        boolean isBucketForward = !nextBucket.equals(LexoRankBucket.min());
        IRank initialValue = toRank(LexoRank.initial(nextBucket));
        rankReorder.reorder(new RankReorderIterator(isBucketForward, initialValue));
    }

    @Override
    public boolean isBefore(IRank other) {
        return this.compareTo(other) < 0;
    }

    @Override
    public boolean isEqual(IRank other) {
        return this.compareTo(other) == 0;
    }

    @Override
    public boolean isAfter(IRank other) {
        return this.compareTo(other) > 0;
    }

    @Override
    public boolean isOutOfBound() {
        IRank minRankOfBucket = min(this.lexoRank.getBucket());
        IRank maxRankOfBucket = max(this.lexoRank.getBucket());
        return this.isAfter(maxRankOfBucket) || this.isBefore(minRankOfBucket);
    }

    @Override
    public String value() {
        return this.lexoRank.format();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        DefaultRank that = (DefaultRank) obj;
        return lexoRank.equals(that.lexoRank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexoRank);
    }

    @Override
    public String toString() {
        return value();
    }

    @Override
    public int compareTo(@NotNull IRank iRank) {
        return this.lexoRank.compareTo(((DefaultRank)iRank).lexoRank);
    }

    /**
     * 获取下一个rank
     * @return
     */
    public static String getNextRankByCurrentRank(String rank){
        if (StringUtils.isBlank(rank)) {
            rank=DefaultRank.initialValue().value();
        }else{
            rank= DefaultRank.parse(rank).next().value();
        }
        return rank;
    }
}
