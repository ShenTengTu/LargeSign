package com.roripantsu.largesign.manager;

/**
 * To quick invoke texture name.
 * @author ShenTeng Tu(RoriPantsu)
 */
public enum ETextureResource {

	Enttity_large_sign("largesign","large_sign",
			new String[]{"oak","spruce","birch","jungle","acacia","dark_oak"}),
	Item_large_sign("largesign","item_large_sign",
			new String[]{"oak","spruce","birch","jungle","acacia","dark_oak"});
	
	public final String[] textureName;
	public final String domain;
	public final String fileNamePrefix;
	public final String[] fileNameSuffix;
	
	/**
	 * @param domain Resource domain name.
	 * @param fileNamePrefix Prefix of file name.
	 * @param fileNameSuffix Prefix of file name.this is optional.
	 */
	private ETextureResource(String domain,String fileNamePrefix,String... fileNameSuffix){
		this.domain=domain;
		this.fileNamePrefix=fileNamePrefix;
		this.fileNameSuffix=fileNameSuffix;
		if(fileNameSuffix==null){
			this.textureName=new String[1];
			this.textureName[0]=this.domain+":"+this.fileNamePrefix;
		}else{
			this.textureName=new String[fileNameSuffix.length];
			for(int i=0;i<textureName.length;i++){
				this.textureName[i]=this.domain+":"+this.fileNamePrefix+"_"+this.fileNameSuffix[i];
			}
		}
		
	}
}
