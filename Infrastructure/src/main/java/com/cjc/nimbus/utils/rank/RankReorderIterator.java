package com.cjc.nimbus.utils.rank;

import com.cjc.nimbus.exception.AppException;
import com.cjc.nimbus.result.GlobalResultCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RankReorderIterator {
    private final Boolean isBucketForward;
    private IRank rank;

    public RankReorderIterator(Boolean isBucketForward, IRank rank) {
        this.isBucketForward = isBucketForward;
        this.rank = rank;
    }

    public synchronized IRank next() {
        IRank iRank = this.rank;
        if (iRank.isOutOfBound()) {
            log.error("rank {} is out of bound!", this.rank.value());
            throw new AppException(GlobalResultCode.RANK_OVER_LIMIT);
        }
        if (Boolean.TRUE.equals(isBucketForward)) {
            this.rank = this.rank.prev();
        } else {
            this.rank = this.rank.next();
        }
        return iRank;
    }
}
