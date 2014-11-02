package com.roripantsu.largesign.texture;


public enum ETextureResource {
	
	Enttity_large_sign(Domain.LargeSign,"large_sign"),
	Item_large_sign(Domain.LargeSign,"item_large_sign");
	
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
	
	public final String textureName;
	private final String domain;
	private final String fileName;
	
	private ETextureResource(String domain,String fileName){
		this.domain=domain;
		this.fileName=fileName;
		this.textureName=this.domain+":"+this.fileName;
	}
}
