package com.jmr.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class BaseBean implements Serializable {

	private static final long serialVersionUID = 8536674208672362677L;

	public Integer creator; // 创建人
	public Timestamp createDT;// 创建时间
	public Integer modifier; // 修改人
	public Timestamp modifyDT;// 修改时间
	
	public Integer getCreator() {
		return creator;
	}

	public void setCreator(Integer creator) {
		this.creator = creator;
	}

	public Integer getModifier() {
		return modifier;
	}

	public void setModifier(Integer modifier) {
		this.modifier = modifier;
	}

	public Timestamp getCreateDT() {
		return createDT;
	}

	public void setCreateDT(Timestamp createDT) {
		this.createDT = createDT;
	}

	public Timestamp getModifyDT() {
		return modifyDT;
	}

	public void setModifyDT(Timestamp modifyDT) {
		this.modifyDT = modifyDT;
	}

}
