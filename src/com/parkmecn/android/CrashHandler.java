package com.parkmecn.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.parkmecn.android.util.AndroidUtil;
import com.parkmecn.android.util.DateUtil;

/**
 * 捕获系统异常crash
 * @author Zhoujun
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {

	/** Debug Log Tag */  
    public static final String TAG = "CrashHandler";  
     
    /** CrashHandler实例 */  
    private static CrashHandler INSTANCE;  
    /** 程序的Context对象 */  
    private Context mContext;  
    /** 系统默认的UncaughtException处理类 */  
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
      
    /** 使用Properties来保存设备的信息和错误堆栈信息 */  
    private Properties mDeviceCrashInfo = new Properties();  
    private static final String VERSION_NAME = "versionName";  
    private static final String VERSION_CODE = "versionCode";  
    private static final String STACK_TRACE = "STACK_TRACE";  
    /** 错误报告文件的扩展名 */  
    private static final String CRASH_REPORTER_EXTENSION = ".txt";  
    //发送错误日志需要用到的 对象
    private Map webReqeust = new HashMap();
	private String userName;
	
      
    /** 保证只有一个CrashHandler实例 */  
    private CrashHandler() {  
    }  
  
    /** 获取CrashHandler实例 ,单例模式 */  
    public static CrashHandler getInstance() {  
        if (INSTANCE == null)  
            INSTANCE = new CrashHandler();  
        return INSTANCE;  
    }  
      
    /** 
     * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器 
     *  
     * @param ctx 
     */  
    public void init(Context ctx) {  
        mContext = ctx;  
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this); 
        SharedPreferences userInfo=ctx.getSharedPreferences("userInfo", 0);
        userName=userInfo.getString("username",null);
    }  
      
    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {  
        if (!handleException(ex) && mDefaultHandler != null) {  
            // 如果用户没有处理则让系统默认的异常处理器来处理  
            mDefaultHandler.uncaughtException(thread, ex);  
        } else {  
            // Sleep一会后结束程序  
            // 来让线程停止一会是为了显示Toast信息给用户，然后Kill程序  
            try {  
                Thread.sleep(3000);  
            } catch (InterruptedException e) {  
                Log.e(TAG, "Error : ", e);  
            }  
            AndroidUtil.exitApp(mContext); 
        }  
    }  
  
    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑 
     *  
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false 
     */  
    private boolean handleException(Throwable ex) {  
        if (ex == null) {  
            return true;  
        }  
//        ex.printStackTrace();
        final String msg = ex.getLocalizedMessage();  
        
        // 使用Toast来显示异常信息  
        new Thread() {  
            @Override  
            public void run() {  
                // Toast 显示需要出现在一个线程的消息队列中  
                Looper.prepare();  
                Toast.makeText(mContext, "程序出错啦:" + msg, Toast.LENGTH_LONG).show();  
                Looper.loop();  
            }  
        }.start();  
        // 收集设备信息  
        collectCrashDeviceInfo(mContext);  
        // 保存错误报告文件  
       // String crashFileName = saveCrashInfoToFile(ex);  
        saveCrashInfoToFile(ex);
        return true;  
    }  
  
    /** 
     * 收集程序崩溃的设备信息 
     *  
     * @param ctx 
     */  
    public void collectCrashDeviceInfo(Context ctx) {  
        try {  
            PackageManager pm = ctx.getPackageManager();  
           
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
            if (pi != null) {  
                mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);  
                mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode + "");  
            }  
        } catch (NameNotFoundException e) {  
            Log.e(TAG, "Error while collect package info", e);  
        }  
        // 使用反射来收集设备信息.在Build类中包含各种设备信息,  
        // 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息  
        // 返回 Field 对象的一个数组，这些对象反映此 Class 对象所表示的类或接口所声明的所有字段  
        Field[] fields = Build.class.getDeclaredFields();  
        for (Field field : fields) {  
            try {  
                // setAccessible(boolean flag)  
                // 将此对象的 accessible 标志设置为指示的布尔值。  
                // 通过设置Accessible属性为true,才能对私有变量进行访问，不然会得到一个IllegalAccessException的异常  
                field.setAccessible(true);  
                mDeviceCrashInfo.put(field.getName(), field.get(null) + "");  
            } catch (Exception e) {  
                Log.e(TAG, "Error while collect crash info", e);  
            }  
        }  
    }  
      
    /** 
     * 保存错误信息到文件中 
     *  
     * @param ex 
     * @return 
     */  
    private String saveCrashInfoToFile(Throwable ex) {  
        Writer info = new StringWriter();  
        PrintWriter printWriter = new PrintWriter(info);  
        // printStackTrace(PrintWriter s)  
        // 将此 throwable 及其追踪输出到指定的 PrintWriter  
        ex.printStackTrace(printWriter);  
  
        // getCause() 返回此 throwable 的 cause；如果 cause 不存在或未知，则返回 null。  
        Throwable cause = ex.getCause();  
        while (cause != null) {  
            cause.printStackTrace(printWriter);  
            cause = cause.getCause();  
        }  
  
        // toString() 以字符串的形式返回该缓冲区的当前值。  
        String result = info.toString();  
        printWriter.close();  
        mDeviceCrashInfo.put(STACK_TRACE, result);  
  
        try {  
            String fileName = "crash-" + DateUtil.dateToString("yyyy-MM-dd HH时mm分ss", new Date()) + CRASH_REPORTER_EXTENSION;  
            File fileDir = new File(Constants.LOG_DIR);
            File crashFile = new File(fileDir, fileName);
            if(!fileDir.exists())
            	fileDir.mkdirs();
            // 保存文件  
            FileOutputStream trace = new FileOutputStream(crashFile); 
            mDeviceCrashInfo.store(trace, "");  
            trace.flush();  
            trace.close();  
            return fileName;  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured while writing report file...", e);  
        }  
        return null;  
    }  
      
    /** 
     * 获取错误报告文件名 
     *  
     * @param ctx 
     * @return 
     */  
    private String[] getCrashReportFiles(Context ctx) {  
        File filesDir = ctx.getFilesDir();  
        // 实现FilenameFilter接口的类实例可用于过滤器文件名  
        FilenameFilter filter = new FilenameFilter() {  
            // accept(File dir, String name)  
            // 测试指定文件是否应该包含在某一文件列表中。  
            public boolean accept(File dir, String name) {  
                return name.endsWith(CRASH_REPORTER_EXTENSION);  
            }  
        };  
        // list(FilenameFilter filter)  
        // 返回一个字符串数组，这些字符串指定此抽象路径名表示的目录中满足指定过滤器的文件和目录  
        return filesDir.list(filter);  
    }  
  
}