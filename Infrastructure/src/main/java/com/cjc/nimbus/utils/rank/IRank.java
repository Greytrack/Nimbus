package com.cjc.nimbus.utils.rank;

public interface IRank {

    IRank prev();

    IRank next();

    IRank between(IRank otherRank);

    boolean needReorder();

    void reorder(RankReorder rankReorder);

    boolean isBefore(IRank other);

    boolean isEqual(IRank other);

    boolean isAfter(IRank other);

    boolean isOutOfBound();

    String value();

}
