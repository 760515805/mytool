package com.chenhj.mytool.util;


import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**  
 * @Title:  DateUtils.java   
 * @Description:  时间工具类
 * @author: chenhongjie     
 * @date:   2018年6月1日 下午5:50:03   
 * @version V1.0 
 */
public class DateUtils {
	  
	/**时间戳格式 */
  	private static final String DEFAULT_FORMAT = "yyyyMMddHHmmssSSS";
	//时 
	public static final String HOUR="h";
	//分
	public static final String MINUTE="m";
	//秒
	public static final String SECOND="s";
    private int year=Calendar.YEAR;
	private int month=Calendar.MONTH;
	private int day=Calendar.DAY_OF_MONTH;
	public void setYear(int year) {
		this.year = year;
	}
	public void setMonth(int month) {
		if(month>12)throw new IllegalArgumentException("month 不能大于12");
		this.month = month;
	}
	public void setDay(int day) {
		if(day>31)throw new IllegalArgumentException("day 不能大于31");
		this.day = day;
	}
	public int getYear(){
		 return year;
	}
	 public int getMonth(){
		 return month;
	}
	public int getDay(){
		 return day;
	}
	/**
	 * 获取当前long类型的的时间
	 * 
	 * @return long
	 */
	public static long getNowLongTime() {
		return System.currentTimeMillis();
	}
	/**
	 * 获取当前String类型的的时间 使用默认格式  yyyyMMddHHmmssSSS
	 * 
	 * @return String
	 */
	public static String getNowTime() {
		return getNowTime(DEFAULT_FORMAT);
	}
	/**
	 * 获取当前String类型的的时间(自定义格式)
	 * @param format  时间格式
	 * @return String
	 */
	public static String getNowTime(String format) {
		return new SimpleDateFormat(format).format(new Date());
	}
	/**
	 * 获取当前Timestamp类型的的时间
	 * @return Timestamp
	 */
	public static Timestamp getTNowTime() {
		return new Timestamp(getNowLongTime());
	}
	/**
	 * 获取设置的时间
	 * @param hour 时
	 * @param minute 分
	 * @param second 秒
	 * @return
	 */
	 @SuppressWarnings("static-access")
	public static Date getSetTime(int hour,int minute,int second){
		 Calendar calendar = Calendar.getInstance();
		 calendar.set(calendar.HOUR_OF_DAY, hour); // 控制时
		 calendar.set(calendar.MINUTE, minute); // 控制分
		 calendar.set(calendar.SECOND, second); // 控制秒
		return calendar.getTime();
	 }	
	/**
	 * long类型的时间转换成 yyyyMMddHHmmssSSS String类型的时间
	 * 
	 * @param lo long类型的时间
	 * @return
	 */
	public static String longTime2StringTime(long lo) {
		return longTime2StringTime(lo, DEFAULT_FORMAT);
	}
	/**
	 * long类型的时间转换成自定义时间格式
	 * 
	 * @param lo     long类型的时间
	 * @param format  时间格式
	 * @return String
	 */
	public static String longTime2StringTime(long lo, String format) {
		 if(StringUtils.isEmpty(format)){
			 throw new NullPointerException("format can not null or empty!!");
		 }
		return new SimpleDateFormat(format).format(lo);
	}

	/**
	 *  String类型的时间转换成 long
	 * @param time  时间字符串
	 * @return String 时间格式
	 * @throws ParseException 
	 */
	 public static long stringTime2LongTime(String time,String format) throws ParseException{
		 if(StringUtils.isEmpty(format)){
			 throw new NullPointerException("format can not null or empty!!");
		 }
		 if(StringUtils.isEmpty(time)){
			 throw new NullPointerException("time can not null or empty!!");
		 }
		 SimpleDateFormat sd= new SimpleDateFormat(format);
		 Date date=sd.parse(time);
		 return date.getTime();
	 }	 
		/**
		 * 字符串转时间转换成Date
		 */
	public  static Date stringTime2Date(String dateString,String format) throws ParseException{
		 if(StringUtils.isEmpty(format)){
			 throw new NullPointerException("format can not null or empty!!");
		 }
		SimpleDateFormat dateFormat; 
		dateFormat = new SimpleDateFormat(format);//设定格式 
		// dateFormat.setLenient(false); 
		java.util.Date timeDate = dateFormat.parse(dateString);//util类型 
		return timeDate; 
	}
	/**
	 * 获取的String类型的时间并更改时间
	 * @param time 时间字符串
	 * @param format  更改时间的格式  如yyyy-MM-dd HH:mm:ss
	 * @param number  要更改的的数值
	 * @param type   更改时间的类型    时:h; 分:m ;秒:s
	 * @return  String
	 */
	public static String changeTime(String time,String format,int number,String type) {
		 if(StringUtils.isEmpty(format)){
			 throw new NullPointerException("format can not null or empty!!");
		 }
		 if(StringUtils.isEmpty(time)){
			 throw new NullPointerException("time can not null or empty!!");
		 }
		return changeTime(number,format,type,time);
	}
	
	/**
	 * 获取的String类型时间并更改时间
	 * @param number 要更改的的数值
     * @param format 更改时间的格式
	 * @param type   更改时间的类型 。时:h; 分:m ;秒:s 
	 * @param time	  更改的时间       没有则取当前时间
	 * @return String
	 */
	public static String changeTime(int number,String format,String type,String time) {
		if(StringUtils.isEmpty(time)){ //如果没有设置时间则取当前时间
			time=getNowTime(format);
		}
		SimpleDateFormat format1 = new SimpleDateFormat(format);
		Date d=null;
		try {
			d = format1.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
		}    
		Calendar ca = Calendar.getInstance();  //定义一个Calendar 对象
		ca.setTime(d);//设置时间
		if("h".equals(type)){
			ca.add(Calendar.HOUR, number);//改变时
		}else if("m".equals(type)){
			ca.add(Calendar.MINUTE, number);//改变分
		}else if("s".equals(type)){
			ca.add(Calendar.SECOND, number);//改变秒
		}
		String backTime = format1.format(ca.getTime());  //转化为String 的格式
		return backTime;
	}

	 /** 
     * 获取区间时间段的随机日期 
     *  
     * @param beginDate 
     *            起始日期
     * @param endDate 
     *            结束日期
     * @param format
     *  		  时间格式
     * @return 
	 * @throws Exception 
     */  
    public static String randomDate2String(String beginDate,String endDate,String format) throws Exception {  
        try {  
            SimpleDateFormat format1 = new SimpleDateFormat(format);  
            Date start = format1.parse(beginDate);// 构造开始日期  
            Date end = format1.parse(endDate);// 构造结束日期  
            // getTime()表示返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。  
            if (start.getTime() >= end.getTime()) {  
                return null;  
            }  
            long date = randomDate2Long(start.getTime(), end.getTime()) ;  
            return format1.format(new Date(date)) ;  
        } catch (Exception e) {  
            throw e;
        }  
    }  
  
    public static long randomDate2Long(long begin, long end) {  
        long rtn = begin + (long) (Math.random() * (end - begin));  
        // 如果返回的是开始时间和结束时间，则递归调用本函数查找随机值  
        if (rtn == begin || rtn == end) {  
            return randomDate2Long(begin, end);  
        }  
        return rtn;  
    }  
  
	/**
	 * 两个日期带时间比较
	 * @param time1 第一个时间
	 * @param time2 需要对比的时间
	 * @param format 时间格式
	 * @return boolean 第二个时间大于第一个则为true，否则为false
	 */
    public static boolean isCompareDay(String time1,String time2,String format) {
		  if(StringUtils.isEmpty(format)){//如果没有设置格式使用默认格式
				format=DEFAULT_FORMAT;
			}
		    SimpleDateFormat s1 = new SimpleDateFormat(format);
			Date t1=null;
			Date t2 =null;
			try {
				t1 = s1.parse(time1);
				t2=s1.parse(time2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return t2.after(t1);//当 t2 大于 t1 时，为 true，否则为 false
	 }
    /**
     * 计算是否是季度末
     * @param date
     * @return
     */
    public static boolean isSeason(String date){
      int getMonth = Integer.parseInt(date.substring(5,7));
      boolean sign = false;
      if (getMonth==3)
        sign = true;
      if (getMonth==6)
        sign = true;
      if (getMonth==9)
        sign = true;
      if (getMonth==12)
        sign = true;
      return sign;
    }
    /**
     * 计算从现在开始几天后的时间
     * @param afterDay 天数
     * @return
     */
    public static String getDateFromNow(int afterDay){
       GregorianCalendar calendar = new GregorianCalendar();
       Date date = calendar.getTime();

       SimpleDateFormat df = new SimpleDateFormat(DEFAULT_FORMAT);

       calendar.set(Calendar.DATE,calendar.get(Calendar.DATE)+afterDay);
       date = calendar.getTime();

       return df.format(date);
    }
    /**
     * 计算从现在开始几天后的时间
     * @param afterDay 天数
     * @param format 自定义时间格式
     * @return
     */
    public static String getDateFromNow(int afterDay, String format)
    {	
		 if(StringUtils.isEmpty(format)){
			 throw new NullPointerException("format can not null or empty!!");
		 }
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat(format);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + afterDay);
        date = calendar.getTime();

        return df.format(date);
    }
  /**
   * 比较日期，与现在的日期对比     
   * @param limitDate 
   * @return
   */
//    public int getDateCompare(String date){
//       GregorianCalendar calendar = new GregorianCalendar();
//       calendar.set(getYear(),getMonth()-1,getDay());
//       Date date1 = calendar.getTime();
//
//       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//       Date nowDate = new Date();
//       return date.compareTo(nowDate);
//    }
//  //==============================================================================
//  //比较日期，与现在的日期对比  得到天数
//  //==============================================================================
//    public long getLongCompare(String limitDate){
//
//       iDate=limitDate;
//       GregorianCalendar calendar = new GregorianCalendar();
//       calendar.set(getYear(),getMonth()-1,getDay());
//       Date date = calendar.getTime();
//       long datePP=date.getTime();
//       Date nowDate = new Date();
//       long dateNow = nowDate.getTime();
//       return ((dateNow-datePP)/(24*60*60*1000));
//
//    }
//  //==============================================================================
//  //比较日期，与现在的日期对比  得到信息
//  //==============================================================================
//    public String getStringCompare(String limitDate,int openDay){
//           iDate=limitDate;
//           GregorianCalendar calendar = new GregorianCalendar();
//           calendar.set(getYear(),getMonth()-1,getDay());
//           Date date = calendar.getTime();
//           long datePP=date.getTime();
//           Date nowDate = new Date();
//           long dateNow = nowDate.getTime();
//           long overDay = ((dateNow-datePP)/(24*60*60*1000));
//           String info="";
//           return info;
//
//    }
	public static void main(String[] args) throws Exception {
		/*
		 *  时间
		 */
		long start=getNowLongTime();
		System.out.println("getNowTime():"+getNowTime());		//getNowTime():2017-09-26 17:46:44
		System.out.println("getNowLongTime():"+getNowLongTime());  //getNowLongTime():1506419204920
		System.out.println("getNowTime(sdfm):"+getNowTime(DEFAULT_FORMAT)); //getNowTime(sdfm):2017-09-26 17:46:44 920
		System.out.println("当时时间向前推移30秒:"+ changeTime("20130812084040555",DEFAULT_FORMAT,-30,DateUtils.SECOND));            //2017-09-26 17:46:14 
		System.out.println("时间比较:"+isCompareDay(getNowTime(DEFAULT_FORMAT),changeTime("20130812084040555",DEFAULT_FORMAT,-30,DateUtils.SECOND),"")); //时间比较:false
		System.out.println("getTNowTime():"+getTNowTime());	//getTNowTime():2017-09-26 17:46:44.921
		System.out.println("LongTime2StringTime():"+longTime2StringTime(start, DEFAULT_FORMAT)); //LongTime2StringTime():20170926174644
		
		System.out.println("long TO String"+longTime2StringTime(1527853818611L));   
		System.out.println("long TO String"+longTime2StringTime(1513330097L));   
		
		
		System.out.println("王者测试："+stringTime2LongTime("2018-05-15 14:10:43.000066","yyyy-MM-dd HH:mm:ss.SSSSSS"));
		
		System.out.println(randomDate2String("2018-05-15 14:10:43","2018-05-17 14:10:43","yyyy-MM-dd HH:mm:ss"));
	}
}
