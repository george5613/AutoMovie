import com.george5613.test.app.SplashAction;
import com.george5613.test.app.config.MovieConfig;
import com.george5613.test.app.global.Context;
import com.george5613.test.app.taopp.MainAction;
import com.george5613.test.app.taopp.order.ChooseCinemaAction;
import com.george5613.test.app.taopp.order.OrderingAction;
import com.george5613.test.app.taopp.order.ScheduleAction;
import com.george5613.test.app.taopp.order.SelectSeatAction;
import com.george5613.test.app.taopp.search.SearchAction;
import com.george5613.test.base.BaseAppiumApp;
import com.george5613.test.helper.WaiterHelper;
import org.openqa.selenium.NoSuchElementException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 学习自动测试框架后写个购票程序试试
 * <p>
 * 淘票票自动选座，针对淘票票Android端V9.2.1开发
 * 是否可以无缝兼容之前和之后版本未经测试
 * <p>
 * Feature
 * 可以自动模拟人力循环检查某一部电影是否开始售票
 * 一旦开始售票直接沙雕算法选票锁座
 * 锁座成功后短信通知手机号付款
 * 如果锁座失败截图保存（留下证据），然后重复算法选座
 * <p>
 * 此代码修改自自动化测试框架
 * 需要手机支持Appium自动化测试
 * 安装淘票票Android端V9.2.1版本
 * 并且登录你自己的账号
 * 支持adb发送短信（发短信需要手机有sim卡支持）
 * 注意发送短信需要资费
 */
public class App extends BaseAppiumApp {

    static {
        Context.registerAction(SplashAction.class);
        Context.registerAction(MainAction.class);
        Context.registerAction(SearchAction.class);
        Context.registerAction(ChooseCinemaAction.class);
        Context.registerAction(ScheduleAction.class);
        Context.registerAction(SelectSeatAction.class);
        Context.registerAction(OrderingAction.class);
    }

    private static final String KEY_DEVICE_NAME = "DEVICE_NAME";
    private Properties mProperties;

    @Override
    protected void initProperties() {
        mProperties = new Properties();
        try {
            mProperties.load(new InputStreamReader(
                    App.class.getClassLoader().getResourceAsStream("config.properties"), "UTF-8"));
        } catch (IOException e) {
        }
    }

    @Override
    public void start() {
        final Context context = new Context(mDriver, new MovieConfig(mProperties));
        //循环模型
        do {
            context.updateCurrentActivity();
            WaiterHelper.wait(context, context.getNextStep());
            try {
                context.doStep();
            } catch (NoSuchElementException e) {
            }
        } while (!context.isFinished());
    }

    public static void main(String[] args) {
        App app = new App();
        app.init();
        app.start();
        app.quit();
    }

    @Override
    protected String getDeviceName() {
        if (mProperties == null) return "";
        return mProperties.getProperty(KEY_DEVICE_NAME);
    }
}
