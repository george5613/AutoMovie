package com.george5613.test.app.taopp.order;

import com.george5613.test.app.global.Context;
import com.george5613.test.app.taopp.model.Seat;
import com.george5613.test.app.taopp.order.utils.SeatSelectUtils;
import com.george5613.test.base.BaseAction;
import com.george5613.test.callback.ElementStatusCallback;
import com.george5613.test.helper.*;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidElement;
import javafx.util.Pair;
import org.apache.commons.io.FileUtils;
import org.apache.http.util.TextUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.george5613.test.app.global.Constant.TAOPP.SELECT_SEAT_ACTIVITY;

/**
 * -----------------------------
 * 11 12 13 14 15 16 17 18
 * 9 [N][N][N][N][N][N][N][N]
 * 10 [N][N][N][N][N][N][N][N]
 * 11 [N][N][N][Y][Y][N][N][N]
 * 12 [N][N][N][N][N][N][N][N]
 * 13 [N][N][N][N][N][N][N][N]
 * <p>
 * 沙雕选座逻辑
 * 1、自动选择中间列&离开荧幕%70的排座位为基准
 * 2、仅支持选择2人连坐，如果选不到最中间会向周围辐射2行3列(共计35个座位中选横向2连)，
 * 3、选座优先级为:列>行、后大于前、左大于右边
 * 4、如果区域内完全找不到2连座位，直接放弃手动选座，使用淘票票推荐2人
 * 5、如果走的是淘票票官方推荐，(校验选择的座位，如果太偏，直接放弃)
 * 6、选座后跳转支付，点击支付锁座后，给对应手机号发短信通知付款
 */
public class SelectSeatAction extends BaseAction {

    private static final String TAG = SELECT_SEAT_ACTIVITY;

    private static final String MOVIE_NAME = Context.getConfig().getMovieName();

    private static final int MIN_GAP = 4;

    private SimpleDateFormat mSimpleDataFormat;

    private ArrayList<Seat> SEAT_LIST = new ArrayList<>();
    private HashMap<String, Seat> SEAT_MAP = new HashMap<>();

    public SelectSeatAction(Context context) {
        super(context);
        mSimpleDataFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    private void createSeatList(AndroidElement seatContainer, int centerRow, int centerColumn) {
        SEAT_LIST.clear();
        for (int row = centerRow - 2; row < centerRow + 2; row++) {
            for (int column = centerColumn - 3; column < centerColumn + 3; column++) {
                SEAT_LIST.add(new Seat(mContext, seatContainer, row, column));
            }
        }
        createSeatMap();
    }

    private void createSeatMap() {
        SEAT_MAP.clear();
        if (SEAT_LIST != null && !SEAT_LIST.isEmpty()) {
            for (Seat seat : SEAT_LIST) {
                SEAT_MAP.put(seat.getTag(), seat);
            }
        }
    }

    @Override
    public String getTag() {
        return TAG;
    }

    private void zoomOut(AndroidElement element) {
        Rectangle rectangle = element.getRect();
        Point center = GestureHelper.getCenter(rectangle);
        getDriver().perform(GestureHelper.zoomOut(center, rectangle.getWidth() / 2));
    }


    @Override
    public void doStep() {
        ElementHelper.waitForElementById(mContext, "com.taobao.movie.android:id/seattable", new ElementStatusCallback() {
            @Override
            public void onElementTouch(AndroidElement element) {
                zoomOut(element);
                element = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/seattable");
                List<MobileElement> seats = ElementHelper.findElementsByClassName(element, "android.view.View");
                if (seats == null) return;
                String lastSeatNum = getLastSeatNum(seats);
                Seat lastSeat = new Seat(lastSeatNum);
                int targetRow = lastSeat.getRow() * 7 / 10;
                //获取屏幕内显示的当前排座位
                List<MobileElement> relativeRowSeats = element.findElementsByAndroidUIAutomator(
                        UiSelectorHelper.getSelectKeyByDescContains("" + targetRow + "排"));
                int relativeColumnCount = relativeRowSeats.size();
                int relativeColumnCenterCount = relativeColumnCount / 2 - 1;
                boolean isEvenSeatCount = relativeColumnCount % 2 == 0;
                Rectangle container = element.getRect();
                int targetCenterX = container.getX() + container.getWidth() / 2;
                int targetColumn = -1;
                do {
                    MobileElement check = relativeRowSeats.get(relativeColumnCenterCount);
                    Rectangle checkRect = check.getRect();
                    int checkCenterX = checkRect.getX() + checkRect.getWidth() / 2;
                    int offset = checkCenterX - targetCenterX;
                    if (isEvenSeatCount) {
                        Seat seat = new Seat(check.getAttribute("contentDescription"));
                        targetColumn = seat.getColumn();
                        break;
                    } else {
                        if (Math.abs(offset) < checkRect.getWidth() / 2) {
                            Seat seat = new Seat(check.getAttribute("contentDescription"));
                            targetColumn = seat.getColumn();
                            break;
                        }
                        relativeColumnCenterCount += (offset > 0 ? 1 : -1);
                    }

                } while (relativeColumnCenterCount > 0 && relativeColumnCenterCount < relativeColumnCount && targetColumn < 0);
                createSeatList(element, targetRow, targetColumn);
                Pair<Seat, Seat> targetPair = SeatSelectUtils.selectSeats(SEAT_LIST, SEAT_MAP, targetRow, targetColumn);
                if (targetPair == null) {
                    autoSelect();
                }
                buyOrSaveScreenshot();
            }
        });
    }

    private void buyOrSaveScreenshot() {
        AndroidElement buy = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/btn_buy");
        if (buy != null) {
            buy.click();
        } else {
            getScreenshot();
            mContext.finish();
        }
    }

    private void autoSelect() {
        AndroidElement priceContainer = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/price_container_view");
        if (priceContainer != null) {
            List<MobileElement> price = priceContainer.findElementsById("com.taobao.movie.android:id/price_bg");
            if (price != null) {
                for (MobileElement element : price) {
                    element.click();
                }
            }
        }
        AndroidElement recommendContainer = ElementHelper.findElementById(mContext, "com.taobao.movie.android:id/recommend_container_view");
        if (priceContainer != null) {
            MobileElement recommend = priceContainer.findElementByAndroidUIAutomator(UiSelectorHelper.getSelectKeyByText("2人"));
            if (recommend != null) {
                recommend.click();
            }
        }
    }


    private String getLastSeatNum(List<MobileElement> seats) {
        String result = "";
        int count = seats.size();
        for (int i = (count - 1); i >= 0; i--) {
            MobileElement end = seats.get(i);
            result = end.getAttribute("contentDescription");
            if (!TextUtils.isEmpty(result)) break;
        }
        return result;
    }

    public void getScreenshot() {
        File file = getDriver().getScreenshotAs(OutputType.FILE);
        Date date = new Date();
        String dir = mSimpleDataFormat.format(date);
        File dest = new File(Context.getConfig().getScreenShotPath() + dir);
        try {
            FileUtils.forceMkdir(dest);
        } catch (IOException e) {
        }
        if (file.exists()) {
            File destFile = new File(dest, MOVIE_NAME);
            try {
                FileUtils.moveFile(file, destFile);
            } catch (IOException e) {
            }
        }
    }

}
