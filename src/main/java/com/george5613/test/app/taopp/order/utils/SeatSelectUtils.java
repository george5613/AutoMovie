package com.george5613.test.app.taopp.order.utils;

import com.george5613.test.app.taopp.model.Seat;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class SeatSelectUtils {

    private static final int OFFSET = 3;

    private static String getSeatTag(int row, int column) {
        return "" + row + "排" + column + "座";
    }

    public static Pair<Seat, Seat> selectSeats(ArrayList<Seat> SEAT_LIST, HashMap<String, Seat> SEAT_MAP, int row, int column) {
        Seat first = null;
        Seat second = null;

        Seat target = SEAT_MAP.get(getSeatTag(row, column));
        if (target == null) {
            return null;
        }
        first = target;
        do {
            if (first.isEnabled()) {
                second = findNextSeat(SEAT_MAP, first);
                if (isSeatEnable(second)) return new Pair<>(first, second);
                first.setEnable(false);
            }
            first = findNextEnableSeat(SEAT_MAP, target);
        } while (!isAllSeatDisabled(SEAT_LIST));
        return null;
    }

    private static boolean isAllSeatDisabled(ArrayList<Seat> seats) {
        for (Seat seat : seats) {
            if (seat.isInnerEnabled()) return false;
        }
        return false;
    }

    //找到下一个基准座位，优先座位号一致，后排大于前排
    private static Seat findNextEnableSeat(HashMap<String, Seat> SEAT_MAP, Seat seat) {
        Seat result = null;
        //前后
        for (int offset = 1; offset < OFFSET; offset++) {
            result = findNextEnabledSeat(SEAT_MAP, seat, offset);
            if (result != null) return result;
        }
        return null;
    }


    private static boolean isSeatEnable(Seat seat) {
        return seat != null && seat.isEnabled();
    }

    //寻找顺序从 正后、正前、右、左、右后、左后、右前、左前
    private static Seat findNextEnabledSeat(HashMap<String, Seat> SEAT_MAP, Seat seat, int offset) {
        //正后
        Seat next = SEAT_MAP.get(getSeatTag(seat.getRow() + offset, seat.getColumn()));
        if (isSeatEnable(next)) return next;
        //正前
        next = SEAT_MAP.get(getSeatTag(seat.getRow() - offset, seat.getColumn()));
        if (isSeatEnable(next)) return next;
        //右
        next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() + offset));
        if (isSeatEnable(next)) return next;
        //左
        next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() - offset));
        if (isSeatEnable(next)) return next;
        //右后
        next = SEAT_MAP.get(getSeatTag(seat.getRow() + offset, seat.getColumn() + offset));
        if (isSeatEnable(next)) return next;
        //左后
        next = SEAT_MAP.get(getSeatTag(seat.getRow() + offset, seat.getColumn() - offset));
        if (isSeatEnable(next)) return next;
        //右前
        next = SEAT_MAP.get(getSeatTag(seat.getRow() - offset, seat.getColumn() + offset));
        if (isSeatEnable(next)) return next;
        //左前
        next = SEAT_MAP.get(getSeatTag(seat.getRow() - offset, seat.getColumn() - offset));
        if (isSeatEnable(next)) return next;
        return null;
    }

    //找到连号的座位，优先右侧
    private static Seat findNextSeat(HashMap<String, Seat> SEAT_MAP, Seat seat) {
        Seat next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() + 1));
        if (isSeatEnable(next)) return next;
        next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() - 1));
        if (isSeatEnable(next)) return next;
        return null;
    }
}
