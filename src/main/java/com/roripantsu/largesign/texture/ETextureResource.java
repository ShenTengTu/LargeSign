package com.roripantsu.largesign.texture;


public enum ETextureResource {

	Enttity_large_sign(Domain.LargeSign,"large_sign",
			new String[]{"oak","spruce","birch","jungle","acacia","dark_oak"}),
	Item_large_sign(Domain.LargeSign,"item_large_sign",
			new String[]{"oak","spruce","birch","jungle","acacia","dark_oak"});
	
	
	public static class BasePath{
		public static final String Blocks="textures/blocks";
		public static final String Colormap="textures/colormap";
		public static final String Effect="textures/effect";
		public static final String Entity="textures/entity";
		public static final String Environment="textures/environment";
		public static final String Font="textures/font";
		public static final String Gui="textures/gui";
		public static final String Items="textures/items";
		public static final String Map="textures/map";
		public static final String Misc="textures/misc";
		public static final String Models="textures/models";
		public static final String Painting="textures/painting";
		public static final String Particle="textures/particle";
	}
	
	private class Domain{
		static final String LargeSign="largesign";
	}
	
	public final String[] textureName;
	public final String domain;
	public final String fileNamePrefix;
	public final String[] fileNameSuffix;
	
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
