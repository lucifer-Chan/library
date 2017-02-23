package cn.ttsales.remote.util;

public enum EventType {
	// 订阅
	SUBSCRIBE("subscribe"),
	// 取消订阅
	UNSUBSCRIBE("unsubscribe"),
	// 扫带参二维码关注
	SCAN("SCAN"),
	//上报地理位置
	LOCATION("LOCATION"),
	// 按钮点击
	CLICK("click"),
	// 上报进入应用事件
	EVENT_AGENT("enter_agent"),
	// 链接
	VIEW("view"),
	// 扫码
	SCANCODE_PUSH("scancode_push"),
	// 扫码等待消息
	SCANCODE_WAITMSG("scancode_waitmsg"),
	// 拍照
	PIC_SYSPHOTO("pic_sysphoto"),
	// 拍照或相册发图
	PIC_PHOTO_OR_ALBUM("pic_photo_or_album"),
	// 微信相册发图
	PIC_WEIXIN("pic_weixin"),
	// 选择地理位置
	LOCATION_SELECT("location_select"),
	//用户领取卡券
	USER_GET_CARD("user_get_card"),
	//卡券通过审核
	CARD_PASS_CHECK("card_pass_check"),
	//卡券未通过审核
	CARD_NOT_PASS_CHECK("card_not_pass_check"),
	//用户领取卡券
	USER_DEL_CARD("user_del_card"),
	//门店审核
	POI_CHECK_NOTIFY("poi_check_notify"),
	//
	OTHER("other")
;

	private String name;

	private EventType(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public static EventType getByName(String name) {
		for (EventType type : EventType.values()) {
			if (type.getName().equalsIgnoreCase(name))
				return type;
		}
		return EventType.OTHER;
	}
}
