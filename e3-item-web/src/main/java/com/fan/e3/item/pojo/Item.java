package com.fan.e3.item.pojo;

import com.fan.e3.pojo.TbItem;

public class Item extends TbItem {
	private static final long serialVersionUID = 1L;
	
	public Item(TbItem tbItem){
		this.setBarcode(tbItem.getBarcode());
		this.setCid(tbItem.getCid());
		this.setCreated(tbItem.getCreated());
		this.setId(tbItem.getId());
		this.setImage(tbItem.getImage());
		this.setNum(tbItem.getNum());
		this.setPrice(tbItem.getPrice());
		this.setSellPoint(tbItem.getSellPoint());
		this.setStatus(tbItem.getStatus());
		this.setTitle(tbItem.getTitle());
		this.setUpdated(tbItem.getUpdated());
	}
	
	public String[] getImages(){
		String string = this.getImage();
		if(string!=null && !"".equals(string)){
			String[] split = string.split(",");
			return split;
		}
		return null;
	}
}
