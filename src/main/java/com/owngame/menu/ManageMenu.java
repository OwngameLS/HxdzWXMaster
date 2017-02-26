package com.owngame.menu;

/**
 * 菜单
 *
 * @author Administrator
 */
public class ManageMenu {

    // 事件key值
    public static final String EVENTKEY_QUERY_PREFIX = "81";// 查询事件的前缀
    public static final String EVENTKEY_XXFS = "811";// 消息发送
    public static final String EVENTKEY_SQSQ = "812";// 授权申请


    // 为电站业务创建的菜单
    public static Menu createMenu() {
        Menu menu = new Menu();
        // 1
        ClickButton button1 = new ClickButton();
        button1.setName("消息发送");
        button1.setType("click");
        button1.setKey(EVENTKEY_XXFS);

        ClickButton button2 = new ClickButton();
        button2.setName("授权申请");
        button2.setType("click");
        button2.setKey(EVENTKEY_SQSQ);

//        ClickButton button12 = new ClickButton();
//        button12.setName("五项指标");
//        button12.setType("click");
//        button12.setKey(EVENTKEY_WXZB);
//
//        ClickButton button13 = new ClickButton();
//        button13.setName("今日概况");
//        button13.setType("click");
//        button13.setKey(EVENTKEY_JRGK);
//
//        Button button1 = new Button();
//        button1.setName("查询");
//        button1.setSub_button(new Button[]{button11, button12, button13});
//
//        // 2
//        ClickButton button21 = new ClickButton();
//        button21.setName("拍照上传");
//        button21.setType("pic_sysphoto");
//        button21.setKey("821");
//
//        ClickButton button22 = new ClickButton();
//        button22.setName("相册上传");
//        button22.setType("pic_photo_or_album");
//        button22.setKey("822");
//
//        Button button2 = new Button();
//        button2.setName("上传照片");
//        button2.setSub_button(new Button[]{button21, button22});


//		// 3
//		ClickButton button31 = new ClickButton();
//		button31.setName("保证金");
//		button31.setType("click");
//		button31.setKey("841");
//
//		ClickButton button32 = new ClickButton();
//		button32.setName("资金账户");
//		button32.setType("click");
//		button32.setKey("851");
//
//		Button button3 = new Button();
//		button3.setName("管理");
//		button3.setSub_button(new Button[]{button31, button32});

        menu.setButton(new Button[]{button1, button2});
        return menu;
    }

//    public void test() {
//        WeixinServiceImpl weixinService;
//        String token = weixinService.getAccessToken().getAccesstoken();
//        String jStr = JSONObject.toJSONString(ManageMenu.createMenu());
//
////        int r = weixinService.createMenu(token, jStr);
//        System.out.println("jStr:" + jStr + ": r:" );
//
//
////        System.out.println("r:" + r.getErrcode() + "," + r.getErrmsg() + "," + r.getUrl());
//
//
//    }
}
