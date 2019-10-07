package com.george5613.simulate;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    /**
     * -----------------------------
     * 11 12 13 14 15 16 17 18
     * 9  [Y][Y][Y][Y][Y][Y][Y][Y]
     * 10 [Y][Y][Y][N][Y][Y][Y][Y]
     * 11 [Y][Y][N][N][N][Y][Y][Y]
     * 12 [Y][Y][Y][N][Y][Y][Y][Y]
     * 13 [Y][Y][Y][Y][Y][Y][Y][Y]
     */
    private static ArrayList<Seat> SEAT_LIST = new ArrayList<>();
    private static HashMap<String, Seat> SEAT_MAP = new HashMap<>();

    private static final int LEFT = 0;
    private static final int RIGHT = 0;

    private static final int OFFSET = 3;

    static {
        SEAT_LIST.add(new Seat(9, 11));
        SEAT_LIST.add(new Seat(9, 12));
        SEAT_LIST.add(new Seat(9, 13));
        SEAT_LIST.add(new Seat(9, 14));
        SEAT_LIST.add(new Seat(9, 15));
        SEAT_LIST.add(new Seat(9, 16));
        SEAT_LIST.add(new Seat(9, 17));
        SEAT_LIST.add(new Seat(9, 18));

        SEAT_LIST.add(new Seat(10, 11));
        SEAT_LIST.add(new Seat(10, 12));
        SEAT_LIST.add(new Seat(10, 13));
        SEAT_LIST.add(new Seat(10, 14, false));
        SEAT_LIST.add(new Seat(10, 15));
        SEAT_LIST.add(new Seat(10, 16));
        SEAT_LIST.add(new Seat(10, 17));
        SEAT_LIST.add(new Seat(10, 18));

        SEAT_LIST.add(new Seat(11, 11));
        SEAT_LIST.add(new Seat(11, 12));
        SEAT_LIST.add(new Seat(11, 13, false));
        SEAT_LIST.add(new Seat(11, 14, false));
        SEAT_LIST.add(new Seat(11, 15, false));
        SEAT_LIST.add(new Seat(11, 16));
        SEAT_LIST.add(new Seat(11, 17));
        SEAT_LIST.add(new Seat(11, 18));

        SEAT_LIST.add(new Seat(12, 11, false));
        SEAT_LIST.add(new Seat(12, 12, false));
        SEAT_LIST.add(new Seat(12, 13, false));
        SEAT_LIST.add(new Seat(12, 14, false));
        SEAT_LIST.add(new Seat(12, 15, false));
        SEAT_LIST.add(new Seat(12, 16, false));
        SEAT_LIST.add(new Seat(12, 17, false));
        SEAT_LIST.add(new Seat(12, 18, false));

        SEAT_LIST.add(new Seat(13, 11));
        SEAT_LIST.add(new Seat(13, 12));
        SEAT_LIST.add(new Seat(13, 13));
        SEAT_LIST.add(new Seat(13, 14));
        SEAT_LIST.add(new Seat(13, 15));
        SEAT_LIST.add(new Seat(13, 16));
        SEAT_LIST.add(new Seat(13, 17));
        SEAT_LIST.add(new Seat(13, 18));
        init();
    }

    private static void init() {
        SEAT_MAP.clear();
        if (SEAT_LIST != null && !SEAT_LIST.isEmpty()) {
            for (Seat seat : SEAT_LIST) {
                SEAT_MAP.put(seat.getTag(), seat);
            }
        }
    }

    public static void main(String[] args) {
        int targetRow = 11;
        int targetColumn = 14;
        Pair<Seat, Seat> seatPair = selectSeats(targetRow, targetColumn);
        if (seatPair != null) {
            System.out.println(seatPair);
        }
    }

    private static String getSeatTag(int row, int column) {
        return "" + row + "排" + column + "座";
    }

    private static Pair<Seat, Seat> selectSeats(int row, int column) {
        Seat first = null;
        Seat second = null;

        Seat target = SEAT_MAP.get(getSeatTag(row, column));
        if (target == null) {
            return null;
        }
        first = target;
        do {
            if (first.isEnabled()) {
                second = findNextSeat(first);
                if (isSeatEnable(second)) return new Pair<>(first, second);
                first.setEnable(false);
            }
            first = findNextEnableSeat(target);
        } while (true);
    }

    //找到下一个基准座位，优先座位号一致，后排大于前排
    private static Seat findNextEnableSeat(Seat seat) {
        Seat result = null;
        //前后
        for (int offset = 1; offset < OFFSET; offset++) {
            result = findNextEnabledSeat(seat, offset);
            if (result != null) return result;
        }
        return null;
    }


    private static boolean isSeatEnable(Seat seat) {
        return seat != null && seat.isEnabled();
    }

    //寻找顺序从 正后、正前、右、左、右后、左后、右前、左前
    private static Seat findNextEnabledSeat(Seat seat, int offset) {
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
    private static Seat findNextSeat(Seat seat) {
        Seat next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() + 1));
        if (isSeatEnable(next)) return next;
        next = SEAT_MAP.get(getSeatTag(seat.getRow(), seat.getColumn() - 1));
        if (isSeatEnable(next)) return next;
        return null;
    }

}
