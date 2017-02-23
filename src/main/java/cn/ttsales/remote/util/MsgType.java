/**
 * Copyright (c) 2014 RATANSOFT.All rights reserved.
 * @filename MsgType.java
 * @package cn.ttsales.elec.web.wxapi
 * @author dandyzheng
 * @date 2014-9-15
 */
package cn.ttsales.remote.util;

/**
 * @author
 * 
 */
public enum MsgType {
	// 文本消息
	TEXT("text"),
	// 返回消息类型：音乐
	MUSIC("music"),
	// 返回消息类型：图文
	NEWS("news"),
	// 返回消息类型：图片
	IMAGE("image"),
	// 返回消息类型：视频
	VIDEO("video"),
	// 返回消息类型：语音
	VOICE("voice"),
	// 请求消息类型：链接
	LINK("link"),
	// 请求消息类型：地理位置
	LOCATION("location"),
	// 请求消息类型：MP图文
	MPNEWS("mpnews"),
	// 请求消息类型：推送
	EVENT("event"),
	//
	OTHER("other");
	private String name;

	private MsgType(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public static MsgType getByName(String name) {
		for (MsgType type : MsgType.values()) {
			if (type.getName().equals(name))
				return type;
		}
		return MsgType.OTHER;
	}
}
